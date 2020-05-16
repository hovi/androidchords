package eu.karelhovorka.zpevnik.music

import eu.karelhovorka.zpevnik.util.Tone
import eu.karelhovorka.zpevnik.util.ToneTransposer
import eu.karelhovorka.zpevnik.util.Transposer

fun Array<Interval>.toTones(base: Tone): Array<Tone> {
    return map { base.transpose(it) }.toTypedArray()
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
            val regex = Transposer.FULL_CHORD.toRegex()
            val result = regex.find(text) ?: error("not valid chord $text")
            print(result.groupValues.toList())
            val values = result.groupValues
            val tone = Tone.fromString(values[1]) ?: error("not a tone")
            val type  = ChordType.fromString(values[2])
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
