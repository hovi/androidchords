package eu.karelhovorka.zpevnik.music

import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.A5
import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.M13
import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.M2
import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.M3
import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.M6
import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.M7
import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.M9
import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.P1
import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.P11
import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.P4
import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.P5
import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.d5
import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.d7
import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.m3
import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.m7
import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.m9

enum class ChordType(val longName: String, val shortcuts: Array<String>, override val intervals: Array<Interval>) : IntervalGroup {

    MAJOR_TRIAD(
            longName = "Major triad",
            shortcuts = arrayOf("dur", "", "M", "maj", "major"),
            intervals = arrayOf(P1, M3, P5)
    ),
    MINOR_TRIAD(
            longName = "Minor triad",
            shortcuts = arrayOf("m", "-", "mi", "min", "minor"),
            intervals = arrayOf(P1, m3, P5)
    ),
    AUGMENTED_TRIAD(
            longName = "Augmented triad",
            shortcuts = arrayOf("aug", "+5", "#5"),
            intervals = arrayOf(P1, M3, A5)
    ),
    DIMINISHED_TRIAD(
            longName = "Diminished triad",
            shortcuts = arrayOf("dim", "-5", "b5"),
            intervals = arrayOf(P1, m3, d5)
    ),
    MAJOR_SIXTH(
            longName = "Major sixth",
            shortcuts = arrayOf("6", "add6"),
            intervals = arrayOf(P1, M3, P5, M6)
    ),
    MINOR_SIXTH(
            longName = "Minor sixth",
            shortcuts = arrayOf("m6", "m(add6)"),
            intervals = arrayOf(P1, m3, P5, M6)
    ),
    DOMINANT_SEVENTH(
            longName = "Dominant seventh",
            shortcuts = arrayOf("7"),
            intervals = arrayOf(P1, M3, P5, m7)
    ),
    MAJOR_SEVENTH(
            longName = "Major seventh",
            shortcuts = arrayOf("maj7", "Δ7", "Δ", "M7"),
            intervals = arrayOf(P1, M3, P5, M7)
    ),
    MINOR_MAJOR_SEVENTH(
            longName = "Minor major seventh",
            shortcuts = arrayOf("mM7", "m#7", "-M7", "-Δ7", "-Δ", "m(maj7)", "-(maj7)"),
            intervals = arrayOf(P1, m3, P5, M7)
    ),
    MINOR_SEVENTH(
            longName = "Minor seventh",
            shortcuts = arrayOf("m7", "m-7"),
            intervals = arrayOf(P1, m3, P5, m7)
    ),
    AUGMENTED_MAJOR_SEVENTH(
            longName = "Augmented major seventh",
            shortcuts = arrayOf("Δ(#5)", "Δ(+5)", "M7(+5)", "M7(#5)", "maj7(+5)", "maj7(#5)"),
            intervals = arrayOf(P1, M3, A5, M7)
    ),
    AUGMENTED_SEVENTH(
            longName = "Augmented seventh",
            shortcuts = arrayOf("aug7", "7(#5)", "7(+5)"),
            intervals = arrayOf(P1, M3, A5, m7)
    ),
    HALF_DIMINISHED_SEVENTH(
            longName = "Half diminished seventh",
            shortcuts = arrayOf("ø", "ø7", "m7(dim5)", "mi7(dim5)", "min7(dim5)", "m7(-5)", "m7(b5)", "m7(o5)"),
            intervals = arrayOf(P1, m3, d5, m7)
    ),
    DIMINISHED_SEVENTH(
            longName = "Diminished seventh",
            shortcuts = arrayOf("o(7)", "dim", "(dim7)"),
            intervals = arrayOf(P1, m3, d5, d7)
    ),
    SEVENTH_FLAT_FIVE(
            longName = "Seventh flat five",
            shortcuts = arrayOf("7(b5)", "7(-5)", "7(dim5)"),
            intervals = arrayOf(P1, M3, d5, m7)
    ),
    MAJOR_NINTH(
            longName = "Major ninth",
            shortcuts = arrayOf("(M9)", "Δ(9)", "(maj9)"),
            intervals = arrayOf(P1, M3, P5, M7, M9)
    ),
    DOMINANT_NINTH(
            longName = "Dominant ninth",
            shortcuts = arrayOf("(9)"),
            intervals = arrayOf(P1, M3, P5, m7, M9)
    ),
    DOMINANT_MINOR_NINTH(
            longName = "Dominant minor ninth",
            shortcuts = arrayOf("7(-9)", "7(b9)", "(b9)"),
            intervals = arrayOf(P1, M3, P5, m7, m9)
    ),
    MINOR_MAJOR_NINTH(
            longName = "Minor major ninth",
            shortcuts = arrayOf("-(maj9)", "m(maj9)", "mi(maj9)", "min(maj9)"),
            intervals = arrayOf(P1, m3, P5, M7, M9)
    ),
    MINOR_NINTH(
            longName = "Minor ninth",
            shortcuts = arrayOf("m(9)", "(-(9)", "mi(9)", "min(9)"),
            intervals = arrayOf(P1, m3, P5, m7, M9)
    ),
    AUGMENTED_MAJOR_NINTH(
            longName = "Augmented major ninth",
            shortcuts = arrayOf("+(M9)", "aug(M9)", "+(maj9)", "aug(maj9)"),
            intervals = arrayOf(P1, M3, A5, M7, M9)
    ),
    AUGMENTED_DOMINANT_NINTH(
            longName = "Augmented dominant ninth",
            shortcuts = arrayOf("+(9)", "(9#5)", "aug(9)"),
            intervals = arrayOf(P1, M3, A5, m7, M9)
    ),
    HALF_DIMINISHED_NINTH(
            longName = "Half diminished ninth",
            shortcuts = arrayOf("ø(9)"),
            intervals = arrayOf(P1, m3, d5, m7, M9)
    ),
    HALF_DIMINISHED_MINOR_NINTH(
            longName = "Half diminished minor ninth",
            shortcuts = arrayOf("ø(b9)"),
            intervals = arrayOf(P1, m3, d5, m7, m9)
    ),
    DIMINISHED_NINTH(
            longName = "Diminished ninth",
            shortcuts = arrayOf("o(9)", "dim(9)"),
            intervals = arrayOf(P1, m3, d5, d7, M9)
    ),
    DIMINISHED_MINOR_NINTH(
            longName = "Diminished minor ninth",
            shortcuts = arrayOf("o(b9)", "dim(b9)"),
            intervals = arrayOf(P1, m3, d5, d7, m9)
    ),
    MAJOR_SIXTH_NINTH(
            longName = "Major sixth ninth",
            shortcuts = arrayOf("69"),
            intervals = arrayOf(P1, M3, P5, M6, M9)
    ),
    MINOR_SIXTH_NINTH(
            longName = "minor sixth ninth",
            shortcuts = arrayOf("m69"),
            intervals = arrayOf(P1, m3, P5, M6, M9)
    ),
    ELEVENTH(
            longName = "Eleventh",
            shortcuts = arrayOf("11"),
            intervals = arrayOf(P1, M3, P5, m7, M9, P11)
    ),
    MAJOR_ELEVENTH(
            longName = "Major eleventh",
            shortcuts = arrayOf("M11", "maj11"),
            intervals = arrayOf(P1, M3, P5, M7, M9, P11)
    ),
    MINOR_MAJOR_ELEVENTH(
            longName = "Minor major eleventh",
            shortcuts = arrayOf("m(M11)", "m(maj11)"),
            intervals = arrayOf(P1, m3, P5, M7, M9, P11)
    ),
    MINOR_ELEVENTH(
            longName = "Minor eleventh",
            shortcuts = arrayOf("m11", "-11", "mi11", "min11"),
            intervals = arrayOf(P1, m3, P5, m7, M9, P11)
    ),
    AUGMENTED_MAJOR_ELEVENTH(
            longName = "Augmented major eleventh",
            shortcuts = arrayOf("aug(M11)", "aug(maj11)"),
            intervals = arrayOf(P1, M3, A5, M7, M9, P11)
    ),
    AUGMENTED_ELEVENTH(
            longName = "Augmented eleventh",
            shortcuts = arrayOf("aug11"),
            intervals = arrayOf(P1, M3, A5, m7, M9, P11)
    ),
    HALF_DIMINISHED_ELEVENTH(
            longName = "Half diminished eleventh",
            shortcuts = arrayOf("ø11"),
            intervals = arrayOf(P1, m3, d5, m7, M9, P11)
    ),
    DIMINISHED_ELEVENTH(
            longName = "Diminished eleventh",
            shortcuts = arrayOf("o11", "dim11"),
            intervals = arrayOf(P1, m3, d5, d7, M9, P11)
    ),
    MAJOR_THIRTEENTH(
            longName = "Major thirteenth",
            shortcuts = arrayOf("M13", "Δ(13)", "maj(13)"),
            intervals = arrayOf(P1, M3, P5, M7, M9, P11, M13)
    ),
    THIRTEENTH(
            longName = "Thirteenth",
            shortcuts = arrayOf("13"),
            intervals = arrayOf(P1, M3, P5, m7, M9, P11, M13)
    ),
    MINOR_MAJOR_THIRTEENTH(
            longName = "Minor major thirteenth",
            shortcuts = arrayOf("M13", "Δ(13)", "maj(13)"),
            intervals = arrayOf(P1, m3, P5, M7, M9, P11, M13)
    ),
    MINOR_THIRTEENTH(
            longName = "Minor thirteenth",
            shortcuts = arrayOf("M13", "Δ(13)", "maj(13)"),
            intervals = arrayOf(P1, m3, P5, m7, M9, P11, M13)
    ),
    AUGMENTED_MAJOR_THIRTEENTH(
            longName = "Augmented major thirteenth",
            shortcuts = arrayOf("aug(maj13)", "+M13", "+maj(13)", "+Δ(13)"),
            intervals = arrayOf(P1, M3, A5, M7, M9, P11, M13)
    ),
    AUGMENTED__THIRTEENTH(
            longName = "Augmented  thirteenth",
            shortcuts = arrayOf("+13", "13#5", "13+5", "aug13"),
            intervals = arrayOf(P1, M3, A5, m7, M9, P11, M13)
    ),
    HALF_DIMINISHED_THIRTEENTH(
            longName = "Half-diminished thirteenth",
            shortcuts = arrayOf("ø13"),
            intervals = arrayOf(P1, m3, d5, m7, M9, P11, M13)
    ),
    SUS2(
            longName = "Sus2",
            shortcuts = arrayOf("sus2"),
            intervals = arrayOf(P1, M2, P5)
    ),
    SUS4(
            longName = "Sus4",
            shortcuts = arrayOf("sus4", "sus"),
            intervals = arrayOf(P1, P4, P5)
    ),
    ADD2(
            longName = "Add2",
            shortcuts = arrayOf("add2", "add9"),
            intervals = arrayOf(P1, M2, M3, P5)
    ),
    MADD2(
            longName = "MAdd2",
            shortcuts = arrayOf("m(add2)", "m(add9)"),
            intervals = arrayOf(P1, M2, m3, P5)
    ),
    ADD4(
            longName = "Add4",
            shortcuts = arrayOf("add4", "add11"),
            intervals = arrayOf(P1, M3, P4, P5)
    ),
    OPEN_FIVE(
            longName = "Open five",
            shortcuts = arrayOf("5"),
            intervals = arrayOf(P1, P5)
    ),
    UNKNOWN_CHORD(
            longName = "Unknown chord",
            shortcuts = arrayOf("unknown"),
            intervals = arrayOf(P1)
    );


    val semiToneArray: ByteArray = ByteArray(size = 12).also { b ->
        intervals.forEach { b[it.step] = 1 }
    }

    val steps: IntArray = IntArray(size = intervals.size).also { intArray ->
        intervals.forEachIndexed { index, interval ->
            intArray[index] = interval.step
        }
    }

    companion object {
        fun fromString(type: String): ChordType {
            return fromStringOrNull(type) ?: UNKNOWN_CHORD
        }

        fun fromStringOrNull(type: String): ChordType? {
            return values().firstOrNull { type == it.name || type == it.longName || type in it.shortcuts.map { it.replace("[()]".toRegex(), "") } }
        }
    }
}