package eu.karelhovorka.zpevnik.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TransposerTest {

    @Test
    fun testRemoveChords() {
        assertEquals("[Es]", Transposer.removeChords("[Es]"))

        assertEquals("", Transposer.removeChords("[C][D]"))
        assertEquals("", Transposer.removeChords("[C D]"))
        assertEquals("", Transposer.removeChords("[C,D]"))

        assertEquals("", Transposer.removeChords("[C]\n[D]\n"))

        assertEquals("\n\nx", Transposer.removeChords("\n\nx"))
        assertEquals("\n\nx", Transposer.removeChords("\n\nx\n\n"))
        assertEquals("x", Transposer.removeChords("[C]\n[D]\nx"))
        assertEquals("x", Transposer.removeChords("[C]\n[D]\nx\n\n"))

        assertEquals("", Transposer.removeChords(""))
    }

    @Test
    fun removeChordsDoesNotLeaveDoubleSpaces() {
        assertEquals("more text", Transposer.removeChords("more [Am] text"))
        assertEquals("text here", Transposer.removeChords("[C] text here"))
        assertEquals("a b", Transposer.removeChords("a [C][G] b"))
        assertEquals("a b", Transposer.removeChords("a [C] [G] b"))
        assertEquals("text", Transposer.removeChords("te[C]xt"))
        assertEquals("text", Transposer.removeChords("text [C]"))
        assertEquals("word, word", Transposer.removeChords("word, [C]word"))
        assertEquals("keep  original  spacing", Transposer.removeChords("keep  original  spacing"))
        assertEquals("  indented [x] text", Transposer.removeChords("  indented [x] text"))
    }

    @Test
    fun testRemoveNonChords() {
        assertEquals("[C]", Transposer.removeNonChords("[C]text songu"))
        assertEquals("[C]", Transposer.removeNonChords("[C]"))
        assertEquals("[C][D]", Transposer.removeNonChords("[C]text songu[D]"))

        assertEquals("", Transposer.removeNonChords(""))
    }

    @Test
    fun testRemoveBrackets() {
        assertEquals("C D", ToneTransposer.removeBrackets("[C] [D]"))

        assertEquals("", ToneTransposer.removeBrackets(""))
    }

    @Test
    fun testRemoveDuplicateChordSequences() {
        assertEquals("xx[A][C]\nxx", Transposer.removeDuplicateChordSequences("xx[A][C]\nxx[A][C]"))
        assertEquals(
            "Hello,[A] lucky boy[C]\n[C][A]\nHow are you today",
            Transposer.removeDuplicateChordSequences(
                """
                Hello,[A] lucky boy[C]
                [C][A]
                How are you today[A][C]
                """.trimIndent()
            )
        )

        // minimum is sequence of 2 chords to remove
        assertEquals("xx[A]\nxx[A]", Transposer.removeDuplicateChordSequences("xx[A]\nxx[A]"))

        assertEquals("", Transposer.removeDuplicateChordSequences(""))
    }

    @Test
    fun testMerge() {
        assertEquals("[D#mi][C#][G#mi]", Transposer.mergeLine("[D#mi,C#,G#mi]"))
        assertEquals("[H7]raduje[Emi][Dmi][D#mi][Emi]:/[D]", Transposer.mergeLine("[H7]raduje[Emi][Dmi,D#mi,Emi]:/[D]"))
        assertEquals(
            "[H7]raduje[Emi][Dmi][D#mi][Emi]:/[D]",
            Transposer.mergeLine(Transposer.mergeLine("[H7]raduje[Emi][Dmi,D#mi,Emi]:/[D]"))
        )
        assertEquals(
            "[C]šlapu dál a [D]nezbejvá než [C]klít než [D]klít než [G]klít. [F][F#][G]",
            Transposer.mergeLine("[C]šlapu dál a [D]nezbejvá než [C]klít než [D]klít než [G]klít. [F,F#,G]")
        )
        assertEquals(
            """
            [C]Docela jsem [D]zapomněl, kam [C]chtěl jsem [D]vlastně [G]jít [F][F#][G]
            [C]šlapu dál a [D]nezbejvá než [C]klít než [D]klít než [G]klít. [F][F#][G]
            """.trimIndent(),
            Transposer.mergeLine(
                """
                [C]Docela jsem [D]zapomněl, kam [C]chtěl jsem [D]vlastně [G]jít [F,F#,G]
                [C]šlapu dál a [D]nezbejvá než [C]klít než [D]klít než [G]klít. [F,F#,G]
                """.trimIndent()
            )
        )

        assertEquals("", Transposer.mergeLine(""))
    }

    @Test
    fun testMergeSpaces() {
        assertEquals("[Emi] [D] [Ami] ", Transposer.mergeLine("[Emi D Ami ]"))
        assertEquals(" [Emi] [D] [Ami]", Transposer.mergeLine("[ Emi D Ami]"))
        assertEquals("[Emi] [D] [Ami]", Transposer.mergeLine("[Emi D Ami]"))
        assertEquals("[Emi] [D] [Ami]", Transposer.mergeLine("[Emi, D, Ami]"))
        assertEquals("[Emi][D][Ami]", Transposer.mergeLine("[Emi,D,Ami]"))
    }

    @Test
    fun testReplacePlainChordsInlineSpaces() {
        assertEquals("[E ]", Transposer.mergeLine("[E ]"))
        assertEquals("[E] [A]", Transposer.mergeLine("[E A]"))
        assertEquals("[E][A]", Transposer.mergeLine("[E,A]"))
        assertEquals("[E][A]    [F]", Transposer.mergeLine("[E,A    F]"))
    }

    @Test
    fun testContainsChordsOnly() {
        assertTrue(Transposer.lineContainsChordsOnly("[A] [A]"))
        assertTrue(Transposer.lineContainsChordsOnly("[A A]"))
        assertTrue(Transposer.lineContainsChordsOnly("[A]"))
        assertTrue(Transposer.lineContainsChordsOnly("[F#][H][C#]"))

        assertFalse(Transposer.lineContainsChordsOnly("only text and stuff"))
        assertFalse(Transposer.lineContainsChordsOnly(""))
    }

    @Test
    fun testContainsTextOnly() {
        assertTrue(Transposer.lineContainsTextOnly("only text and stuff"))

        assertFalse(Transposer.lineContainsTextOnly("[A]only text and stuff"))
    }

    @Test
    fun testContainsTextAndChords() {
        assertTrue(Transposer.lineContainsChordsAndText("[C]only text and stuff"))

        assertFalse(Transposer.lineContainsChordsAndText("[C]"))
        assertFalse(Transposer.lineContainsChordsAndText(" "))
        assertFalse(Transposer.lineContainsChordsAndText(""))
    }
}
