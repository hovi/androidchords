package eu.karelhovorka.zpevnik.util

import eu.karelhovorka.zpevnik.music.Chord
import eu.karelhovorka.zpevnik.music.ChordType
import eu.karelhovorka.zpevnik.music.Scale
import eu.karelhovorka.zpevnik.music.ScaleType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class SectionTest {

    val cMajorScale = Scale(
            tone = Tone.C,
            type = ScaleType.IONIAN
    )

    val eMinor = Chord(
            tone = Tone.E,
            type = ChordType.MINOR_TRIAD
    )

    val eMajor = Chord(
            tone = Tone.E,
            type = ChordType.MAJOR_TRIAD
    )

    @Test
    fun emiInCmajor() {
        assertTrue(cMajorScale.contains(eMinor))
    }

    @Test
    fun eNotInCMajor() {
        assertFalse(cMajorScale.contains(eMajor))
    }

    @Test
    fun slunecniHrob() {
        val keyDetector = KeyDetector(
                text = "E F#m G#m C#m A H",
                scaleTypes = ScaleType.MAJOR_AND_MINOR
        )
        val detected = keyDetector.detect().filter { it.scale.type == ScaleType.IONIAN }
        assertEquals(1, detected.size)
        assertEquals(Tone.E, detected.first().scale.tone)
    }

    @Test
    fun slunecniHrobE7() {
        val keyDetector = KeyDetector(
                text = "E F#m G#m C#m A H E7",
                scaleTypes = ScaleType.MAJOR_AND_MINOR
        )
        val detected = keyDetector.detect().filter { it.scale.type == ScaleType.IONIAN }
        println(detected)
        assertEquals(1, detected.size)
        assertEquals(Tone.E, detected.first().scale.tone)
    }

    @Test
    fun detectCMajor() {
        val keyDetector = KeyDetector(
                text = "C Dm Em F G Am",
                scaleTypes = ScaleType.MAJOR_AND_MINOR
        )
        val detected = keyDetector.detect()
        println(detected)
        assertEquals(2, detected.size)
        val major = detected.first { it.scale.type == ScaleType.IONIAN }.scale
        val minor = detected.first { it.scale.type == ScaleType.AEOLIAN }.scale
        assertEquals(Tone.C, major.tone)
        assertEquals(Tone.A, minor.tone)

    }
}