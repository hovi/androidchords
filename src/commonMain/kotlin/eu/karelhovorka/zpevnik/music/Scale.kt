package eu.karelhovorka.zpevnik.music

import eu.karelhovorka.zpevnik.util.Tone

data class Scale(
        val tone: Tone,
        val type: ScaleType
) {

    val tones: Array<Tone> by lazy {
        type.intervals.toTones(tone)
    }

    fun contains(chord: Chord): Boolean {
        return chord.tones.subtract(tones.asIterable()).isEmpty()
    }

    fun unsharedTones(chord: Chord): Int {
        return chord.tones.subtract(tones.asIterable()).size
    }

}