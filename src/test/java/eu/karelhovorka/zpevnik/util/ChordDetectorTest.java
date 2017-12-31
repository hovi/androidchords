package eu.karelhovorka.zpevnik.util;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChordDetectorTest {


    @Test
    public void testLinaHasChords() {
        // [G]V duši zbylo zbylo [D]světlo z jedný hol[Ami]ky

        assertTrue(ChordDetector.INSTANCE.hasPlainChords("Dadd13 "));

        assertTrue(ChordDetector.INSTANCE.hasPlainChords("D+ "));

        assertTrue(ChordDetector.INSTANCE.hasPlainChords("Dadd6 "));
        assertTrue(ChordDetector.INSTANCE.hasPlainChords("H C#mi E H C#mi E "));
        assertTrue(ChordDetector.INSTANCE.hasPlainChords("C Dadd6 Emi "));

        assertTrue(ChordDetector.INSTANCE.hasPlainChords("A G D A G D A G D A G D"));
        assertTrue(ChordDetector.INSTANCE.hasPlainChords("C#mi7, Am"));
        assertTrue(ChordDetector.INSTANCE.hasPlainChords("2x A A G D"));
        assertTrue(ChordDetector.INSTANCE.hasPlainChords("C, Fmi, C, Fmi, G"));
        assertTrue(ChordDetector.INSTANCE.hasPlainChords("Bb D"));
        assertTrue(ChordDetector.INSTANCE.hasPlainChords("Bb,D"));
        assertTrue(ChordDetector.INSTANCE.hasPlainChords("Bb , D"));
        assertTrue(ChordDetector.INSTANCE.hasPlainChords("C#m E F# C#m E F# C#m"));
        assertTrue(ChordDetector.INSTANCE.hasPlainChords("Em - C - Em - C - Em - C - Em - C"));

    }

    @Test
    public void testReplacePlainChordsInline() {
        assertEquals("[E]", ChordDetector.INSTANCE.replacePlainChords("E"));
        assertEquals("[E] [Adim7]", ChordDetector.INSTANCE.replacePlainChords("E Adim7"));
        assertEquals("[C#mi7], [Am]", ChordDetector.INSTANCE.replacePlainChords("C#mi7, Am"));
        assertEquals("2x [A] [A] [G] [D]", ChordDetector.INSTANCE.replacePlainChords("2x A A G D"));
        assertEquals("\t[Em] - [C] - [Em] - [C] - [Em] - [C] - [Em] - [C]", ChordDetector.INSTANCE.replacePlainChords("\tEm - C - Em - C - Em - C - Em - C"));
    }

    @Test
    public void testReplacePlainChordsInlineSpaces() {
        assertEquals("[E] ", ChordDetector.INSTANCE.replacePlainChordsInline("E "));
        assertEquals("[E] [A]", ChordDetector.INSTANCE.replacePlainChordsInline("E A"));
    }

    @Test
    public void testChordsInline() {
        assertArrayEquals(ChordDetector.INSTANCE.getBracketChordsInLine("[A] [C] [D]"), new String[]{"A", "C", "D"});
        assertArrayEquals(ChordDetector.INSTANCE.getBracketChordsInLine("[A#5] [C7/5] [Dadd9]"), new String[]{"A#5", "C7/5", "Dadd9"});
        assertArrayEquals(ChordDetector.INSTANCE.getBracketChordsInLine("Some text[A#5], some more text [C7/5] - [Dadd9]"), new String[]{"A#5", "C7/5", "Dadd9"});
    }

    @Test
    public void testLineContainsChords() {
        assertTrue(ChordDetector.INSTANCE.lineContainsChords("Some text[A#5], some more text [C7/5] - [Dadd9]", new String[]{"A#5", "C7/5", "Dadd9"}));
    }

}
