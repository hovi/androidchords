package eu.karelhovorka.zpevnik.music


import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.A4
import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.M2
import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.M3
import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.M6
import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.M7
import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.P1
import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.P4
import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.P5
import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.d5
import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.m2
import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.m3
import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.m6
import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.m7

enum class ScaleType(val aliases: Array<String>, override val intervals: Array<Interval>) : IntervalGroup {
    HARMONIC_MINOR(arrayOf("harmonic_minor"), arrayOf(P1, M2, m3, P4, P5, m6, M7)),
    MELODIC_MINOR(arrayOf("melodic_minor"), arrayOf(P1, M2, m3, P4, P5, M6, M7)),
    MAJOR_PENTATONIC(arrayOf("major_pentatonic"), arrayOf(P1, M2, M3, P5, M6)),
    MINOR_PENTATONIC(arrayOf("minor_pentatonic"), arrayOf(P1, m3, P4, P5, m7)),
    IONIAN(arrayOf("ionian", "major"), arrayOf(P1, M2, M3, P4, P5, M6, M7)),
    DORIAN(arrayOf("dorian"), arrayOf(P1, M2, m3, P4, P5, M6, m7)),
    PHRYGIAN(arrayOf("phrygian"), arrayOf(P1, m2, m3, P4, P5, m6, m7)),
    LYDIAN(arrayOf("lydian"), arrayOf(P1, M2, M3, A4, P5, M6, M7)),
    MIXOLYDIAN(arrayOf("mixolydian"), arrayOf(P1, M2, M3, P4, P5, M6, m7)),
    AEOLIAN(arrayOf("aeolian", "natural_minor"), arrayOf(P1, M2, m3, P4, P5, m6, m7)),
    LOCRIAN(arrayOf("locrian"), arrayOf(P1, m2, m3, P4, d5, m6, m7));


    companion object {
        val MAJOR_AND_MINOR = arrayOf(IONIAN, AEOLIAN)
    }

}