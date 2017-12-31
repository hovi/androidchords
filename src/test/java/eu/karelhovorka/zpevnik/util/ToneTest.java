package eu.karelhovorka.zpevnik.util;

import org.junit.Test;

import eu.karelhovorka.zpevnik.music.Interval;
import eu.karelhovorka.zpevnik.util.Tone.CountryCategory;
import eu.karelhovorka.zpevnik.util.Tone.ModificationAbbreviation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class ToneTest {

    @Test
    public void testTranspose() {
        ToneTransposer tt = new ToneTransposer(CountryCategory.EASTERN, ModificationAbbreviation.SHARP);

        assertEquals("A", tt.transpose("B", Interval.Companion.of(-1)));

        assertEquals("G", tt.transpose("G#", Interval.Companion.of(-1)));

        assertEquals("H", tt.transpose("C", Interval.Companion.of(-1)));

        assertEquals("H", tt.transpose("H#", Interval.Companion.of(-1)));
        assertEquals("H", tt.transpose("C", Interval.Companion.of(-1)));
        assertNotSame("H", tt.transpose("H", Interval.Companion.of(-1)));

        assertEquals("Bb", ToneTransposer.Companion.transpose("B", CountryCategory.WESTERN, ModificationAbbreviation.FLAT, Interval.Companion.of(-1)));
        assertEquals("A#", ToneTransposer.Companion.transpose("B", CountryCategory.WESTERN, ModificationAbbreviation.SHARP, Interval.Companion.of(-1)));
        assertEquals("Ais", ToneTransposer.Companion.transpose("B", CountryCategory.WESTERN, ModificationAbbreviation.IS, Interval.Companion.of(-1)));
        assertEquals("Bb", ToneTransposer.Companion.transpose("B", CountryCategory.WESTERN, ModificationAbbreviation.ES, Interval.Companion.of(-1)));

    }

    @Test
    public void testNormalize() {
        ToneTransposer tt = new ToneTransposer(CountryCategory.EASTERN, ModificationAbbreviation.SHARP);

        assertEquals("H", tt.normalize("Cb"));
        assertEquals("H", tt.normalize("Ces"));

        assertEquals("E", tt.normalize("Fes"));

        assertNotSame("Es", tt.normalize("F"));
    }

    @Test
    public void testFromChordString() {
        ToneTransposer tt = new ToneTransposer(CountryCategory.EASTERN, ModificationAbbreviation.SHARP);

        assertEquals("A#", tt.fromChordString("[Bmi/Hmi]").getValue(CountryCategory.EASTERN, ModificationAbbreviation.SHARP));

        assertEquals("D", tt.fromChordString("[Dsus4]").getValue(CountryCategory.EASTERN, ModificationAbbreviation.SHARP));

    }

}
