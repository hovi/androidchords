package eu.karelhovorka.zpevnik.util

import eu.karelhovorka.zpevnik.music.Interval
import eu.karelhovorka.zpevnik.util.Tone.CountryCategory.EASTERN
import eu.karelhovorka.zpevnik.util.Tone.ModificationAbbreviation.SHARP
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ChordRegexTest {

    private val chordRegex = Transposer.CHORD_REGEX.toRegex()

    private fun isChord(chord: String) = chordRegex.matches("[$chord]")

    @Test
    fun acceptsCommonChords() {
        val chords = listOf(
            "C", "Am", "Ami", "Amin", "H7", "Hmi7", "C#", "C#m7", "Bb", "Eb", "Ebmaj7", "Hb",
            "Cmaj7", "Cmimaj7", "Dsus4", "Dsus2", "E7sus4", "Cadd9", "Caug", "Cdim", "C+", "C-", "C°", "CΔ",
            "C6", "C9", "C11", "C13", "C/E", "E7/G#", "Dsus2/F#", "C#m/G#", "C/9"
        )
        for (chord in chords) {
            assertTrue(chord, isChord(chord))
        }
    }

    @Test
    fun acceptsAlteredChords() {
        for (chord in listOf("Am7b5", "Hm7b5", "C7#9", "C7b9", "G7#5", "D7#11", "E7b13", "C7b5#9")) {
            assertTrue(chord, isChord(chord))
        }
    }

    @Test
    fun acceptsStackedExtensions() {
        for (chord in listOf("Hmadd11", "Hmadd9", "Dm7add11", "Am7sus4", "C9sus4", "C13sus", "C7sus", "Cmaj9", "Cmaj7#11", "Csus2add9", "E7sus4/G#", "C6/9", "Fmaj7/A")) {
            assertTrue(chord, isChord(chord))
        }
    }

    @Test
    fun acceptsParenthesizedExtensions() {
        for (chord in listOf("Cm(maj7)", "C(add9)", "G7(b9)", "Am(add11)")) {
            assertTrue(chord, isChord(chord))
        }
    }

    @Test
    fun rejectsCzechWordsStartingWithToneLetters() {
        for (word in listOf("Ahoj", "Bude", "Cesta", "Dnes", "Ema", "Ada", "Fakt", "Gaga", "Hele", "Bus", "Add", "Dim", "Hmm")) {
            assertFalse(word, isChord(word))
        }
    }

    @Test
    fun stackedExtensionsTranspose() {
        assertEquals("[C#madd11]", transposeUp2("[Hmadd11]"))
        assertEquals("[Dm(maj7)]", transposeUp2("[Cm(maj7)]"))
        assertEquals("[Em7add11]", transposeUp2("[Dm7add11]"))
    }

    @Test
    fun rejectsLowercaseRoot() {
        for (chord in listOf("a", "am", "e7", "h", "b5", "c/E")) {
            assertFalse(chord, isChord(chord))
        }
    }

    @Test
    fun rejectsNonChords() {
        for (chord in listOf("Es", "Fis", "X", "", "C D", "Ref", "2x", "Hello", "C4sus", "C2sus")) {
            assertFalse(chord, isChord(chord))
        }
    }

    @Test
    fun alteredChordIsNotSplitByMerge() {
        assertEquals("[Am7b5]", Transposer.merge("[Am7b5]"))
        assertEquals("[C7#9]", Transposer.merge("[C7#9]"))
    }

    @Test
    fun alteredChordTransposesAsOneChord() {
        assertEquals("[Hm7b5]", transposeUp2("[Am7b5]"))
        assertEquals("[D7#9]", transposeUp2("[C7#9]"))
        assertEquals("[F#7b9/A#]", transposeUp2("[E7b9/G#]"))
    }

    @Test
    fun transposesCommonChords() {
        assertEquals("[D/F#]", transposeUp2("[C/E]"))
        assertEquals("[Hmi7]", transposeUp2("[Ami7]"))
        assertEquals("[D#m/A#]", transposeUp2("[C#m/G#]"))
        assertEquals("[C#mi]", transposeUp2("[Hmi]"))
        assertEquals("[H]", transposeUp2("[Bb]"))
        assertEquals("[Dadd9]", transposeUp2("[Cadd9]"))
        assertEquals("[Fmaj7]", transposeUp2("[Ebmaj7]"))
        assertEquals("[Esus4]", transposeUp2("[Dsus4]"))
        assertEquals("[F#7/A#]", transposeUp2("[E7/G#]"))
    }

    @Test
    fun lowercaseLyricLinesAreNotChords() {
        assertEquals("a e a\nh a d", ChordDetector.replacePlainChords("a e a\nh a d"))
    }

    @Test
    fun plainChordLinesAreDetected() {
        assertEquals("[Am7b5] [E7]", ChordDetector.replacePlainChords("Am7b5 E7"))
        assertEquals("[Ebmaj7] [Cadd9]", ChordDetector.replacePlainChords("Ebmaj7 Cadd9"))
        assertEquals("[Ami] [C] 2x", ChordDetector.replacePlainChords("Ami C 2x"))
        assertEquals("[C] - [G] - [Am]", ChordDetector.replacePlainChords("C - G - Am"))
        assertEquals("[E], [A], [E]", ChordDetector.replacePlainChords("E, A, E"))
        assertEquals("A je to", ChordDetector.replacePlainChords("A je to"))
    }

    private fun transposeUp2(text: String) = ToneTransposer.transposeAll(text, Interval.of(2), EASTERN, SHARP)
}
