package eu.karelhovorka.zpevnik.util

import eu.karelhovorka.zpevnik.music.Interval
import eu.karelhovorka.zpevnik.util.Tone.CountryCategory
import eu.karelhovorka.zpevnik.util.Tone.ModificationAbbreviation
import mock.JvmStatic
import mock.toPattern

class ToneTransposer(private val type: CountryCategory, private val htt: ModificationAbbreviation) {

    fun transposeAll(text: String, step: Interval): String {
        return transposeAll(text, step, type, htt)
    }

    fun fromChordString(chord: String): Tone {
        return fromChordString(chord, type)
    }

    fun fromToneString(tone: String): Tone {
        return fromToneString(tone, type)
    }

    fun transpose(tone: String, step: Interval): String {
        return transpose(tone, type, htt, step)
    }

    fun normalize(tone: String): String {
        return normalize(tone, type, htt)
    }

    companion object {

        private val TONE_PATTERN = ("\\[(" + Transposer.SINGLE_TONE + ").*").toPattern()

        private val CHORD_REGEX_PATTERN = ("\\[(" + Transposer.FULL_CHORD + ")\\]").toPattern()

        @JvmStatic
        fun transposeAll(text: String, step: Interval, type: CountryCategory, htt: ModificationAbbreviation): String {
            val result = Transposer.merge(text)
            if (step.step % Tone.values().size == 0) {
                return result
            }
            val m = CHORD_REGEX_PATTERN.matcher(result)
            var sb = ""
            while (m.find()) {
                try {
                    val tone = ToneTransposer.fromChordString("[" + m.group(1) + "]", type)
                    if (!m.group(5).isNullOrBlank() && !m.group(4).isNullOrBlank()) {
                        val tone2 = ToneTransposer.fromChordString("[" + m.group(5) + "]", type)
                        var tempChord = tone.transpose(step).getValue(type, htt) + m.group(3) + m.group(4) + tone2.transpose(step).getValue(type, htt)
                        if (!m.group(6).isNullOrBlank()) {
                            tempChord += m.group(6)
                        }
                        sb += m.appendReplacement("[$tempChord]")
                    } else {
                        if (!m.group(6).isNullOrBlank() && !m.group(4).isNullOrBlank()) {
                            sb += m.appendReplacement("[" + tone.transpose(step).getValue(type, htt) + m.group(3) + m.group(4) + m.group(6) + "]")
                        } else {
                            sb += m.appendReplacement("[" + tone.transpose(step).getValue(type, htt) + m.group(3) + "]")
                        }
                    }
                } catch (e: IllegalArgumentException) {
                    //tone not found, we will not transpose
                    sb += m.appendReplacement(m.group())
                }
            }
            sb += m.appendTail()
            return sb
        }

        fun removeBrackets(text: String): String {
            return text.replace(Transposer.CHORD_REGEX.toRegex(), "$1")
        }

        @JvmStatic
        fun fromChordString(chord: String, type: CountryCategory): Tone {
            val matcher = TONE_PATTERN.matcher(chord)
            if (matcher.find()) {
                return fromToneString(matcher.group(1)!!, type)
            }
            throw IllegalArgumentException("Chord not found: " + chord)
        }

        fun fromToneString(tone: String, type: CountryCategory): Tone {
            for (cn in Tone.values()) {
                if (cn.getAreaTone(type).aliases.contains(tone)) {
                    return cn
                }
            }
            throw IllegalArgumentException("Tone not found: " + tone)
        }

        fun transpose(tone: String, type: CountryCategory, halfToneType: ModificationAbbreviation, step: Interval): String {
            val c = fromToneString(tone, type)
            return c.transpose(step).getValue(type, halfToneType)
        }

        fun normalize(tone: String, type: CountryCategory, halfToneType: ModificationAbbreviation): String {
            return fromToneString(tone, type).getValue(type, halfToneType)
        }
    }
}
