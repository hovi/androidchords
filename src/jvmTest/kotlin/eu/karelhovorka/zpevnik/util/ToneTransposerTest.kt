package eu.karelhovorka.zpevnik.util

import eu.karelhovorka.zpevnik.music.Interval.Companion.of
import eu.karelhovorka.zpevnik.util.Tone.CountryCategory
import eu.karelhovorka.zpevnik.util.Tone.ModificationAbbreviation
import eu.karelhovorka.zpevnik.util.ToneTransposer.Companion.transposeAll
import junit.framework.TestCase

class ToneTransposerTest : TestCase() {
    fun test7Sus4() {
        assertEquals("[F7sus4]", transpose("[E7sus4]", 1))
    }

    fun testSpecialCharChords() {
        assertEquals("[F-]", transpose("[E-]", 1))
        assertEquals("[F°]", transpose("[E°]", 1))
        assertEquals("[FΔ]", transpose("[EΔ]", 1))
    }

    fun testTransposeB() {
        val countryCategory = CountryCategory.EASTERN
        val modificationAbbreviation = ModificationAbbreviation.FLAT
        assertEquals("[Ab]", transpose("[Bb]", -1, countryCategory, modificationAbbreviation))
        assertEquals("[A]", transpose("[B]", -1, countryCategory, modificationAbbreviation))
        assertEquals("[B]", transpose("[Bb]", 1, countryCategory, modificationAbbreviation))
    }

    fun testTransposePlus() {
        assertEquals("[G#6+]", transpose("[A6+]", -1))
        assertEquals("[G#6+]", transpose("[G#6+]", 0))
        assertEquals("[G#6+]", transpose("[G6+]", 1))
    }

    fun testTransposeDoubles() {
        assertEquals("[Bmi/Hmi]", transpose("[Bmi/Hmi]", 0))

        assertEquals("[Ami/A#mi]", transpose("[Bmi/Hmi]", -1))

        assertEquals("[Gmi/Ami]", transpose("[G#mi/A#mi]", -1))

        assertEquals("[H/G#]", transpose("[C/A]", -1))

        assertEquals("[A#mi/G#]", transpose("[Hmi/A]", -1))

        assertEquals("[G] [E] [F#/H]", transpose("[G# F G/C]", -1))
    }

    fun testTranspose() {
        assertEquals("[Cmi7/5]", transpose("[Hmi7/5]", 1))

        assertEquals("[Fmi7/11]", transpose("[F#mi7/11]", 11))
        assertEquals("[Fmi7/11]", transpose("[F#mi7/11]", -1))

        assertEquals("[F5/13]", transpose("[F5/13]", 12))

        assertEquals("[A#]", transpose("[A#]", 0))

        assertEquals("[B]", transpose("[B]", 0))

        assertEquals("[Hadd13]", transpose("[Cadd13]", -1))

        assertEquals("[Emaj7]", transpose("[Fmaj7]", -1))

        assertEquals("[E]", transpose("[F]", -1))

        assertEquals("[Em]", transpose("[Fm]", -1))

        assertEquals("[Em7]", transpose("[Fm7]", -1))

        assertEquals("[Emi7]", transpose("[Fmi7]", -1))

        assertEquals("[Esus]", transpose("[Fsus]", -1))

        assertEquals("[Esus2]", transpose("[Fsus2]", -1))

        assertEquals("[Esus4]", transpose("[Fsus4]", -1))

        assertEquals("[F#maj7]", transpose("[Gmaj7]", -1))

        assertEquals("[G]", transpose("[Ab]", -1))

        assertEquals("[G#]", transpose("[G]", 1))

        assertEquals("[G] [E] [F#]", transpose("[G# F G]", -1))

        assertEquals(
            "1",
            "1,2".split("[,\\s*]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        )
    }

    fun testMultiBracket() {
        assertEquals("[D#mi] [C#] [G#mi] X", transpose("[Emi D Ami ]X", -1))

        assertEquals(" [D#mi] [C#] [G#mi]", transpose("[ Emi D Ami]", -1))

        assertEquals("[D#mi] [C#] [G#mi]", transpose("[Emi D Ami]", -1))

        assertEquals("[D#mi] [C#] [G#mi]", transpose("[Emi, D, Ami]", -1))

        assertEquals("[D#mi][C#][G#mi]", transpose("[Emi,D,Ami]", -1))
    }

    fun testFailedValues() {
        assertEquals("[x#]", transpose("[x#]", 0))
        assertEquals("[X#]", transpose("[X#]", 0))
        assertEquals("[x#]", transpose("[x#]", 1))
        assertEquals("[X#]", transpose("[X#]", 1))
        assertEquals("[XFDFDFDSFDS#]", transpose("[XFDFDFDSFDS#]", 0))
        assertEquals("[XFDFDFDSFDS#]", transpose("[XFDFDFDSFDS#]", 1))
    }

    companion object {
        @JvmOverloads
        fun transpose(
            text: String,
            step: Int,
            countryCategory: CountryCategory = CountryCategory.EASTERN,
            modificationAbbreviation: ModificationAbbreviation = ModificationAbbreviation.SHARP
        ): String {
            return transposeAll(text, of(step), countryCategory, modificationAbbreviation)
        }
    }
}
