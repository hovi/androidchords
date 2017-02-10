package eu.karelhovorka.zpevnik.util;

import junit.framework.TestCase;

import eu.karelhovorka.zpevnik.util.Tone.CountryCategory;
import eu.karelhovorka.zpevnik.util.Tone.ModificationAbbreviation;

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

    public void testTransposeDoubles() {
        assertEquals("[Bmi/Hmi]", transpose("[Bmi/Hmi]", 0));

        assertEquals("[Ami/A#mi]", transpose("[Bmi/Hmi]", -1));

        assertEquals("[Gmi/Ami]", transpose("[G#mi/A#mi]", -1));

        assertEquals("[H/G#]", transpose("[C/A]", -1));

        assertEquals("[A#mi/G#]", transpose("[Hmi/A]", -1));

        assertEquals("[G][E][F#/H]", transpose("[G# F G/C]", -1));

    }

    public void testTranspose() {

        assertEquals("[Cmi7/5]", transpose("[Hmi7/5]", 1));

        assertEquals("[Fmi7/11]", transpose("[F#mi7/11]", -1));

        assertEquals("[F5/13]", transpose("[F5/13]", 12));

        assertEquals("[A#]", transpose("[A#]", 0));

        assertEquals("[B]", transpose("[B]", 0));

        assertEquals("[Hadd13]", transpose("[Cadd13]", -1));

        assertEquals("[Emaj7]", transpose("[Fmaj7]", -1));

        assertEquals("[E]", transpose("[F]", -1));

        assertEquals("[Em]", transpose("[Fm]", -1));

        assertEquals("[Em7]", transpose("[Fm7]", -1));

        assertEquals("[Emi7]", transpose("[Fmi7]", -1));

        assertEquals("[Esus]", transpose("[Fsus]", -1));

        assertEquals("[Esus2]", transpose("[Fsus2]", -1));

        assertEquals("[Esus4]", transpose("[Fsus4]", -1));

        assertEquals("[F#maj7]", transpose("[Gmaj7]", -1));

        assertEquals("[G]", transpose("[Ab]", -1));

        assertEquals("[G#]", transpose("[G]", 1));

        assertEquals("[G][E][F#]", transpose("[G# F G]", -1));

        assertEquals("1", "1,2".split("[,\\s*]")[0]);

        assertEquals("[D#mi][C#][G#mi]", transpose("[Emi D Ami]", -1));

        assertEquals("[D#mi][C#][G#mi]", transpose("[Emi, D, Ami]", -1));
    }

    public void testFailedValues() {
        assertEquals("[x#]", transpose("[x#]", 0));
        assertEquals("[X#]", transpose("[X#]", 0));
        assertEquals("[x#]", transpose("[x#]", 1));
        assertEquals("[X#]", transpose("[X#]", 1));
        assertEquals("[XFDFDFDSFDS#]", transpose("[XFDFDFDSFDS#]", 0));
        assertEquals("[XFDFDFDSFDS#]", transpose("[XFDFDFDSFDS#]", 1));

    }

    public void testMerge() {
        assertEquals("[D#mi][C#][G#mi]", Transposer.merge("[D#mi,C#,G#mi]"));
        assertEquals("[H7]raduje[Emi][Dmi][D#mi][Emi]:/[D]", Transposer.merge("[H7]raduje[Emi][Dmi,D#mi,Emi]:/[D]"));
        assertEquals("[H7]raduje[Emi][Dmi][D#mi][Emi]:/[D]", Transposer.merge(Transposer.merge("[H7]raduje[Emi][Dmi,D#mi,Emi]:/[D]")));
        assertEquals("[C]šlapu dál a [D]nezbejvá než [C]klít než [D]klít než [G]klít. [F][F#][G]", Transposer.merge("[C]šlapu dál a [D]nezbejvá než [C]klít než [D]klít než [G]klít. [F,F#,G]"));
        assertEquals("[C]Docela jsem [D]zapomněl, kam [C]chtěl jsem [D]vlastně [G]jít [F][F#][G]\n[C]šlapu dál a [D]nezbejvá než [C]klít než [D]klít než [G]klít. [F][F#][G]",
                Transposer.merge("[C]Docela jsem [D]zapomněl, kam [C]chtěl jsem [D]vlastně [G]jít [F,F#,G]\n[C]šlapu dál a [D]nezbejvá než [C]klít než [D]klít než [G]klít. [F,F#,G]"));

    }

    public static String transpose(String text, int step) {
        return ToneTransposer.transposeAll(text, step, CountryCategory.EASTERN, ModificationAbbreviation.SHARP);
    }

}
