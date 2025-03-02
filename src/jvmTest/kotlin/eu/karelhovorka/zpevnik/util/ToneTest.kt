package eu.karelhovorka.zpevnik.util

import eu.karelhovorka.zpevnik.music.Interval.Companion.of
import eu.karelhovorka.zpevnik.util.Tone.CountryCategory
import eu.karelhovorka.zpevnik.util.Tone.ModificationAbbreviation
import eu.karelhovorka.zpevnik.util.ToneTransposer.Companion.transpose
import org.junit.Assert
import org.junit.Test

class ToneTest {
    @Test
    fun testWestern() = with(ToneTransposer(CountryCategory.WESTERN, ModificationAbbreviation.SHARP)) {

    }

    @Test
    fun testTranspose() {
        val tt = ToneTransposer(CountryCategory.EASTERN, ModificationAbbreviation.SHARP)

        Assert.assertEquals("A", tt.transpose("B", of(-1)))

        Assert.assertEquals("G", tt.transpose("G#", of(-1)))

        Assert.assertEquals("H", tt.transpose("C", of(-1)))

        Assert.assertEquals("H", tt.transpose("H#", of(-1)))
        Assert.assertEquals("H", tt.transpose("C", of(-1)))
        Assert.assertNotSame("H", tt.transpose("H", of(-1)))

        Assert.assertEquals(
            "Bb",
            transpose("B", CountryCategory.WESTERN, ModificationAbbreviation.FLAT, of(-1))
        )
        Assert.assertEquals(
            "A#",
            transpose("B", CountryCategory.WESTERN, ModificationAbbreviation.SHARP, of(-1))
        )
        Assert.assertEquals(
            "Ais",
            transpose("B", CountryCategory.WESTERN, ModificationAbbreviation.IS, of(-1))
        )
        Assert.assertEquals(
            "Bb",
            transpose("B", CountryCategory.WESTERN, ModificationAbbreviation.ES, of(-1))
        )
    }

    @Test
    fun testNormalize() {
        val tt = ToneTransposer(CountryCategory.EASTERN, ModificationAbbreviation.SHARP)

        Assert.assertEquals("H", tt.normalize("Cb"))
        Assert.assertEquals("H", tt.normalize("Ces"))

        Assert.assertEquals("E", tt.normalize("Fes"))

        Assert.assertNotSame("Es", tt.normalize("F"))
    }

    @Test
    fun testFromChordString() {
        val tt = ToneTransposer(CountryCategory.EASTERN, ModificationAbbreviation.SHARP)

        Assert.assertEquals(
            "A#",
            tt.fromChordString("[Bmi/Hmi]")
                .getValue(CountryCategory.EASTERN, ModificationAbbreviation.SHARP)
        )

        Assert.assertEquals(
            "D",
            tt.fromChordString("[Dsus4]")
                .getValue(CountryCategory.EASTERN, ModificationAbbreviation.SHARP)
        )
    }
}
