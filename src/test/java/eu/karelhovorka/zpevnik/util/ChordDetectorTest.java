package eu.karelhovorka.zpevnik.util;

import org.junit.Test;

import eu.karelhovorka.zpevnik.util.Tone.CountryCategory;
import eu.karelhovorka.zpevnik.util.Tone.ModificationAbbreviation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

public class ChordDetectorTest {


    @Test
    public void testLinaHasChords() {
        // [G]V duši zbylo zbylo [D]světlo z jedný hol[Ami]ky

        assertTrue(ChordDetector.hasPlainChords("Dadd13 "));

        assertTrue(ChordDetector.hasPlainChords("D+ "));

        assertTrue(ChordDetector.hasPlainChords("Dadd6 "));
        assertTrue(ChordDetector.hasPlainChords("H C#mi E H C#mi E "));
        assertTrue(ChordDetector.hasPlainChords("C Dadd6 Emi "));

        assertTrue(ChordDetector.hasPlainChords("A G D A G D A G D A G D"));
        assertTrue(ChordDetector.hasPlainChords("C#mi7, Am"));
        assertTrue(ChordDetector.hasPlainChords("2x A A G D"));
        assertTrue(ChordDetector.hasPlainChords("C, Fmi, C, Fmi, G"));
        assertTrue(ChordDetector.hasPlainChords("Bb D"));
        assertTrue(ChordDetector.hasPlainChords("Bb,D"));
        assertTrue(ChordDetector.hasPlainChords("Bb , D"));
        assertTrue(ChordDetector.hasPlainChords("C#m E F# C#m E F# C#m"));
        assertTrue(ChordDetector.hasPlainChords("Em - C - Em - C - Em - C - Em - C"));

    }

    @Test
    public void testReplacePlainChordsInline() {
        assertEquals("[E]", ChordDetector.replacePlainChords("E"));
        assertEquals("[E] [Adim7]", ChordDetector.replacePlainChords("E Adim7"));
        assertEquals("[C#mi7], [Am]", ChordDetector.replacePlainChords("C#mi7, Am"));
        assertEquals("2x [A] [A] [G] [D]", ChordDetector.replacePlainChords("2x A A G D"));
        assertEquals("\t[Em] - [C] - [Em] - [C] - [Em] - [C] - [Em] - [C]", ChordDetector.replacePlainChords("\tEm - C - Em - C - Em - C - Em - C"));
    }

    @Test
    public void testChordsInline() {
        assertArrayEquals(ChordDetector.getBracketChordsInLine("[A] [C] [D]"), new String[]{"A", "C", "D"});
        assertArrayEquals(ChordDetector.getBracketChordsInLine("[A#5] [C7/5] [Dadd9]"), new String[]{"A#5", "C7/5", "Dadd9"});
        assertArrayEquals(ChordDetector.getBracketChordsInLine("Some text[A#5], some more text [C7/5] - [Dadd9]"), new String[]{"A#5", "C7/5", "Dadd9"});
    }

    @Test
    public void testLineContainsChords() {
        assertTrue(ChordDetector.lineContainsChords("Some text[A#5], some more text [C7/5] - [Dadd9]", new String[]{"A#5", "C7/5", "Dadd9"}));
    }

}
