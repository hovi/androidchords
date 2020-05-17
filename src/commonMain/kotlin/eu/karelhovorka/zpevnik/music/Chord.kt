package eu.karelhovorka.zpevnik.music

import eu.karelhovorka.zpevnik.util.Tone
import eu.karelhovorka.zpevnik.util.Transposer

fun Array<Interval>.toTones(base: Tone): Array<Tone> {
    return map { base.transpose(it) }.toTypedArray()
}

fun Chord.percentShared(scale: Scale): Double {
    val chordsUnshared = scale.unsharedTones(this)
    return (tones.size - chordsUnshared).toDouble() / tones.size.toDouble()
}


data class Chord(
        val tone: Tone,
        val type: ChordType
) {

    val tones: Array<Tone> by lazy {
        type.intervals.toTones(tone)
    }

    companion object {
        fun fromString(text: String): Chord {
            return Transposer.FULL_CHORD_REGEX.find(text)?.let {
                fromMatchResult(it)
            }?: error("not a valid chord '$text'")
        }

        fun fromMatchResult(matchResult: MatchResult): Chord {
            val values = matchResult.groupValues
            val tone = Tone.fromString(values[1]) ?: error("not a tone '${values[1]}'")
            val type = ChordType.fromStringOrNull(values[2]) ?: error("not a chordType '${values[2]}'")
            return Chord(
                    tone = tone,
                    type = type
            )
        }
    }

    override fun toString(): String {
        return "${tone}${type.shortcuts.first()}"
    }
}
