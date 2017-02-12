package eu.karelhovorka.zpevnik.util;

import junit.framework.TestCase;

public class TransposerTest extends TestCase {

    public void testRemoveChords() {
        assertEquals("", Transposer.removeChords("[C][D]"));
        assertEquals("", Transposer.removeChords("[C D]"));
        assertEquals("", Transposer.removeChords("[C,D]"));

        assertEquals("", Transposer.removeChords("[C]\n[D]\n"));

        assertEquals("\n\nx", Transposer.removeChords("\n\nx"));
        assertEquals("\n\nx", Transposer.removeChords("\n\nx\n\n"));
        assertEquals("x", Transposer.removeChords("[C]\n[D]\nx"));
        assertEquals("x", Transposer.removeChords("[C]\n[D]\nx\n\n"));

    }

    public void testRemoveNonChords() {
        assertEquals("[C]", Transposer.removeNonChords("[C]text songu"));
        assertEquals("[C]", Transposer.removeNonChords("[C]"));
        assertEquals("[C][D]", Transposer.removeNonChords("[C]text songu[D]"));
    }

    public void testRemoveBrackets() {
        assertEquals("C D", ToneTransposer.removeBrackets("[C] [D]"));
    }

    public void testRemoveDuplicateChordSequences() {
        assertEquals("xx[A][C]\nxx", Transposer.removeDuplicateChordSequences("xx[A][C]\nxx[A][C]"));
        assertEquals("Hello,[A] lucky boy[C]\n[C][A]\nHow are you today",
                Transposer.removeDuplicateChordSequences("Hello,[A] lucky boy[C]\n" +
                        "[C][A]\n" +
                        "How are you today[A][C]"));

        //minimum is sequence of 2 chords to remove
        assertEquals("xx[A]\nxx[A]", Transposer.removeDuplicateChordSequences("xx[A]\nxx[A]"));
    }


    public void testMerge() {
        assertEquals("[D#mi][C#][G#mi]", Transposer.merge("[D#mi,C#,G#mi]"));
        assertEquals("[H7]raduje[Emi][Dmi][D#mi][Emi]:/[D]", Transposer.merge("[H7]raduje[Emi][Dmi,D#mi,Emi]:/[D]"));
        assertEquals("[H7]raduje[Emi][Dmi][D#mi][Emi]:/[D]", Transposer.merge(Transposer.merge("[H7]raduje[Emi][Dmi,D#mi,Emi]:/[D]")));
        assertEquals("[C]šlapu dál a [D]nezbejvá než [C]klít než [D]klít než [G]klít. [F][F#][G]", Transposer.merge("[C]šlapu dál a [D]nezbejvá než [C]klít než [D]klít než [G]klít. [F,F#,G]"));
        assertEquals("[C]Docela jsem [D]zapomněl, kam [C]chtěl jsem [D]vlastně [G]jít [F][F#][G]\n[C]šlapu dál a [D]nezbejvá než [C]klít než [D]klít než [G]klít. [F][F#][G]",
                Transposer.merge("[C]Docela jsem [D]zapomněl, kam [C]chtěl jsem [D]vlastně [G]jít [F,F#,G]\n[C]šlapu dál a [D]nezbejvá než [C]klít než [D]klít než [G]klít. [F,F#,G]"));

    }


}
