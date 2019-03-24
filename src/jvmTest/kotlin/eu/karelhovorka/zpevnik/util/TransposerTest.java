package eu.karelhovorka.zpevnik.util;

import junit.framework.TestCase;

import org.junit.Test;

public class TransposerTest extends TestCase {

    public void testRemoveChords() {
        assertEquals("[Es]", Transposer.INSTANCE.removeChords("[Es]"));

        assertEquals("", Transposer.INSTANCE.removeChords("[C][D]"));
        assertEquals("", Transposer.INSTANCE.removeChords("[C D]"));
        assertEquals("", Transposer.INSTANCE.removeChords("[C,D]"));

        assertEquals("", Transposer.INSTANCE.removeChords("[C]\n[D]\n"));

        assertEquals("\n\nx", Transposer.INSTANCE.removeChords("\n\nx"));
        assertEquals("\n\nx", Transposer.INSTANCE.removeChords("\n\nx\n\n"));
        assertEquals("x", Transposer.INSTANCE.removeChords("[C]\n[D]\nx"));
        assertEquals("x", Transposer.INSTANCE.removeChords("[C]\n[D]\nx\n\n"));

        assertEquals("", Transposer.INSTANCE.removeChords(""));

    }

    public void testRemoveNonChords() {
        assertEquals("[C]", Transposer.INSTANCE.removeNonChords("[C]text songu"));
        assertEquals("[C]", Transposer.INSTANCE.removeNonChords("[C]"));
        assertEquals("[C][D]", Transposer.INSTANCE.removeNonChords("[C]text songu[D]"));

        assertEquals("", Transposer.INSTANCE.removeNonChords(""));
    }

    public void testRemoveBrackets() {
        assertEquals("C D", ToneTransposer.Companion.removeBrackets("[C] [D]"));

        assertEquals("", ToneTransposer.Companion.removeBrackets(""));
    }

    public void testRemoveDuplicateChordSequences() {
        assertEquals("xx[A][C]\nxx", Transposer.removeDuplicateChordSequences("xx[A][C]\nxx[A][C]"));
        assertEquals("Hello,[A] lucky boy[C]\n[C][A]\nHow are you today",
                Transposer.removeDuplicateChordSequences("Hello,[A] lucky boy[C]\n" +
                        "[C][A]\n" +
                        "How are you today[A][C]"));

        //minimum is sequence of 2 chords to remove
        assertEquals("xx[A]\nxx[A]", Transposer.removeDuplicateChordSequences("xx[A]\nxx[A]"));

        assertEquals("", Transposer.removeDuplicateChordSequences(""));
    }


    public void testMerge() {
        assertEquals("[D#mi][C#][G#mi]", Transposer.INSTANCE.mergeLine("[D#mi,C#,G#mi]"));
        assertEquals("[H7]raduje[Emi][Dmi][D#mi][Emi]:/[D]", Transposer.INSTANCE.mergeLine("[H7]raduje[Emi][Dmi,D#mi,Emi]:/[D]"));
        assertEquals("[H7]raduje[Emi][Dmi][D#mi][Emi]:/[D]", Transposer.INSTANCE.mergeLine(Transposer.INSTANCE.mergeLine("[H7]raduje[Emi][Dmi,D#mi,Emi]:/[D]")));
        assertEquals("[C]šlapu dál a [D]nezbejvá než [C]klít než [D]klít než [G]klít. [F][F#][G]", Transposer.INSTANCE.mergeLine("[C]šlapu dál a [D]nezbejvá než [C]klít než [D]klít než [G]klít. [F,F#,G]"));
        assertEquals("[C]Docela jsem [D]zapomněl, kam [C]chtěl jsem [D]vlastně [G]jít [F][F#][G]\n[C]šlapu dál a [D]nezbejvá než [C]klít než [D]klít než [G]klít. [F][F#][G]",
                Transposer.INSTANCE.mergeLine("[C]Docela jsem [D]zapomněl, kam [C]chtěl jsem [D]vlastně [G]jít [F,F#,G]\n[C]šlapu dál a [D]nezbejvá než [C]klít než [D]klít než [G]klít. [F,F#,G]"));

        assertEquals("", Transposer.INSTANCE.mergeLine(""));
    }

    public void testMergeSpaces() {
        assertEquals("[Emi] [D] [Ami] ", Transposer.INSTANCE.mergeLine("[Emi D Ami ]"));

        assertEquals(" [Emi] [D] [Ami]", Transposer.INSTANCE.mergeLine("[ Emi D Ami]"));

        assertEquals("[Emi] [D] [Ami]", Transposer.INSTANCE.mergeLine("[Emi D Ami]"));

        assertEquals("[Emi] [D] [Ami]", Transposer.INSTANCE.mergeLine("[Emi, D, Ami]"));

        assertEquals("[Emi][D][Ami]", Transposer.INSTANCE.mergeLine("[Emi,D,Ami]"));
    }


    @Test
    public void testReplacePlainChordsInlineSpaces() {
        assertEquals("[E ]", Transposer.INSTANCE.mergeLine("[E ]"));
        assertEquals("[E] [A]", Transposer.INSTANCE.mergeLine("[E A]"));
        assertEquals("[E][A]", Transposer.INSTANCE.mergeLine("[E,A]"));
        assertEquals("[E][A]    [F]", Transposer.INSTANCE.mergeLine("[E,A    F]"));
    }

    public void testContainsChordsOnly() {
        assertTrue(Transposer.INSTANCE.lineContainsChordsOnly("[A] [A]"));
        assertTrue(Transposer.INSTANCE.lineContainsChordsOnly("[A A]"));
        assertTrue(Transposer.INSTANCE.lineContainsChordsOnly("[A]"));
        assertTrue(Transposer.INSTANCE.lineContainsChordsOnly("[F#][H][C#]"));

        assertFalse(Transposer.INSTANCE.lineContainsChordsOnly("only text and stuff"));
        assertFalse(Transposer.INSTANCE.lineContainsChordsOnly(""));

    }

    public void testContainsTextOnly() {
        assertTrue(Transposer.INSTANCE.lineContainsTextOnly("only text and stuff"));

        assertFalse(Transposer.INSTANCE.lineContainsTextOnly("[A]only text and stuff"));
    }

    public void testContainsTextAndChords() {
        assertTrue(Transposer.INSTANCE.lineContainsChordsAndText("[C]only text and stuff"));

        assertFalse(Transposer.INSTANCE.lineContainsChordsAndText("[C]"));
        assertFalse(Transposer.INSTANCE.lineContainsChordsAndText(" "));
        assertFalse(Transposer.INSTANCE.lineContainsChordsAndText(""));
    }


}
