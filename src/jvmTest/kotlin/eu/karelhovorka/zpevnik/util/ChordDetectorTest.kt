package eu.karelhovorka.zpevnik.util

import eu.karelhovorka.zpevnik.music.generateCodeChordType
import eu.karelhovorka.zpevnik.music.generateCodeScaleType
import eu.karelhovorka.zpevnik.music.generateIntervalType
import org.junit.Assert.*
import org.junit.Test
import java.io.File

class ChordDetectorTest {

    //@Test
    fun generateIntervalChords() {
        File("src/commonMain/kotlin/eu/karelhovorka/zpevnik/music/BasicInterval.kt").writeText(
                generateIntervalType()
        )
        File("src/commonMain/kotlin/eu/karelhovorka/zpevnik/music/ChordType.kt").writeText(
                generateCodeChordType()
        )

        File("src/commonMain/kotlin/eu/karelhovorka/zpevnik/music/ScaleType2.kt").writeText(
                generateCodeScaleType()
        )
    }

    @Test
    fun testFixInvalidChords() {
        assertEquals("Eb", ChordDetector.fixChord("Es"))
        assertEquals("F#", ChordDetector.fixChord("Fis"))
        assertEquals("Am", ChordDetector.fixChord("AMOL"))
        assertEquals("Am", ChordDetector.fixChord("Ami"))
        assertEquals("Csus4", ChordDetector.fixChord("C4sus"))
        assertEquals("Dmaj7", ChordDetector.fixChord("D7maj"))
        for (tone in arrayOf("C", "D", "F", "G")) {
            assertEquals(tone + "b", ChordDetector.fixChord(tone + "es"))
            assertEquals(tone + "#", ChordDetector.fixChord(tone + "is"))
        }

        assertEquals("Invalid", ChordDetector.fixChord("Invalid"))
    }

    @Test
    fun testInvalidChords() {
        assertArrayEquals(arrayOf("Es"), ChordDetector.invalidChords("[Es]"))
    }


    @Test
    fun testHasInvalidChords() {

        assertTrue(ChordDetector.hasInvalidChords("[Es]"))
        assertTrue(ChordDetector.hasInvalidChords("[Ais]"))
        assertTrue(ChordDetector.hasInvalidChords("[fsdfdsf]"))
        assertTrue(ChordDetector.hasInvalidChords("[C4sus]"))


        assertFalse(ChordDetector.hasInvalidChords("[Csus4]"))
        assertFalse(ChordDetector.hasInvalidChords("[E]"))
        assertFalse(ChordDetector.hasInvalidChords("[E] hello [A]"))

    }


    @Test
    fun testLinaHasChords() {
        // [G]V duši zbylo zbylo [D]světlo z jedný hol[Ami]ky

        assertTrue(ChordDetector.hasPlainChords("Dadd13 "))

        assertTrue(ChordDetector.hasPlainChords("D+ "))

        assertTrue(ChordDetector.hasPlainChords("Dadd6 "))
        assertTrue(ChordDetector.hasPlainChords("H C#mi E H C#mi E "))
        assertTrue(ChordDetector.hasPlainChords("C Dadd6 Emi "))

        assertTrue(ChordDetector.hasPlainChords("A G D A G D A G D A G D"))
        assertTrue(ChordDetector.hasPlainChords("C#mi7, Am"))
        assertTrue(ChordDetector.hasPlainChords("2x A A G D"))
        assertTrue(ChordDetector.hasPlainChords("C, Fmi, C, Fmi, G"))
        assertTrue(ChordDetector.hasPlainChords("Bb D"))
        assertTrue(ChordDetector.hasPlainChords("Bb,D"))
        assertTrue(ChordDetector.hasPlainChords("Bb , D"))
        assertTrue(ChordDetector.hasPlainChords("C#m E F# C#m E F# C#m"))
        assertTrue(ChordDetector.hasPlainChords("Em - C - Em - C - Em - C - Em - C"))

    }

    @Test
    fun testReplacePlainChordsInline() {
        assertEquals("[E]", ChordDetector.replacePlainChords("E"))
        assertEquals("[E] [Adim7]", ChordDetector.replacePlainChords("E Adim7"))
        assertEquals("[C#mi7], [Am]", ChordDetector.replacePlainChords("C#mi7, Am"))
        assertEquals("2x [A] [A] [G] [D]", ChordDetector.replacePlainChords("2x A A G D"))
        assertEquals("\t[Em] - [C] - [Em] - [C] - [Em] - [C] - [Em] - [C]", ChordDetector.replacePlainChords("\tEm - C - Em - C - Em - C - Em - C"))
    }

    @Test
    fun testReplacePlainChordsInlineSpaces() {
        assertEquals("[E] ", ChordDetector.replacePlainChordsInline("E "))
        assertEquals("[E] [A]", ChordDetector.replacePlainChordsInline("E A"))
    }

    @Test
    fun testChordsInline() {
        assertArrayEquals(ChordDetector.getBracketChordsInLine("[A] [C] [D]"), arrayOf("A", "C", "D"))
        assertArrayEquals(ChordDetector.getBracketChordsInLine("[A#5] [C7/5] [Dadd9]"), arrayOf("A#5", "C7/5", "Dadd9"))
        assertArrayEquals(ChordDetector.getBracketChordsInLine("Some text[A#5], some more text [C7/5] - [Dadd9]"), arrayOf("A#5", "C7/5", "Dadd9"))
    }

    @Test
    fun testLineContainsChords() {
        assertTrue(ChordDetector.lineContainsChords("Some text[A#5], some more text [C7/5] - [Dadd9]", arrayOf("A#5", "C7/5", "Dadd9")))
    }

}
