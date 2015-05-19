package eu.karelhovorka.zpevnik.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

import eu.karelhovorka.zpevnik.util.Tone.ModificationAbbreviation;
import eu.karelhovorka.zpevnik.util.Tone.CountryCategory;

public class ToneTest {

	@Test
	public void testTranspose() {
		ToneTransposer tt = new ToneTransposer(CountryCategory.EASTERN, ModificationAbbreviation.SHARP);

		assertEquals("A", tt.transpose("B", -1));

		assertEquals("G", tt.transpose("G#", -1));

		assertEquals("H", tt.transpose("C", -1));

		assertEquals("H", tt.transpose("H#", -1));
		assertEquals("H", tt.transpose("C", -1));
		assertNotSame("H", tt.transpose("H", -1));

		assertEquals("Bb", ToneTransposer.transpose("B", CountryCategory.WESTERN, ModificationAbbreviation.FLAT, -1));
		assertEquals("A#", ToneTransposer.transpose("B", CountryCategory.WESTERN, ModificationAbbreviation.SHARP, -1));
		assertEquals("Ais", ToneTransposer.transpose("B", CountryCategory.WESTERN, ModificationAbbreviation.IS, -1));
		assertEquals("Bb", ToneTransposer.transpose("B", CountryCategory.WESTERN, ModificationAbbreviation.ES, -1));

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
