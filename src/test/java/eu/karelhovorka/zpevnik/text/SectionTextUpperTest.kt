package eu.karelhovorka.zpevnik.text

import eu.karelhovorka.zpevnik.text.section.parseSectionLineUpper
import eu.karelhovorka.zpevnik.text.section.sectionLine
import org.junit.Assert.assertEquals
import org.junit.Test

class SectionTextUpperTest {

    @Test
    fun textLine() {
        assertEquals(sectionLine {
            chordPair {
                chord = null
                text = "no chords there"
            }

        }, parseSectionLineUpper(
                """
                    |
                    |no chords there
            """.trimMargin()))
    }

    @Test
    fun textLine1Chord() {
        assertEquals(sectionLine {
            chordPair {
                chord = "[A]"
                text = "1 chord there"
            }

        }, parseSectionLineUpper(
                """
                    |[A]
                    |1 chord there
            """.trimMargin()))
    }

    @Test
    fun textLine2Chords() {
        assertEquals(sectionLine {
            chordPair {
                chord = "[A]"
                text = "2"
            }
            chordPair {
                chord = "[E]"
                text = " "
            }
            chordPair {
                chord = "[D]"
                text = "chords there"
            }


        }, parseSectionLineUpper(
                """
                    |[A][E][D]
                    |2 chords there
            """.trimMargin()))
    }

    @Test
    fun textLine2ChordsB() {
        assertEquals(sectionLine {
            chordPair {
                chord = "[A]"
                text = "2 chords"
            }
            chordPair {
                chord = "[E]"
                text = " there"
            }

        }, parseSectionLineUpper(
                """
                    |[A]       [E]
                    |2 chords there
            """.trimMargin()))
    }

    @Test
    fun textLine2ChordsC() {
        assertEquals(sectionLine {
            chordPair {
                chord = "[A]"
                text = "2 chords"
            }
            chordPair {
                chord = "[E]"
                text = " there"
            }

        }, parseSectionLineUpper(
                """
                    |[A       E]
                    |2 chords there
            """.trimMargin()))
    }

    @Test
    fun textLine2ChordsD() {
        assertEquals(sectionLine {
            chordPair {
                chord = "[A]"
                text = "2 chords"
            }
            chordPair {
                chord = "[E]"
                text = " there"
            }

        }, parseSectionLineUpper(
                """
                    |A       E
                    |2 chords there
            """.trimMargin()))
    }

    @Test
    fun textLongChords() {
        assertEquals(sectionLine {
            chordPair {
                chord = "[E]"
                text = "-"
            }
            chordPair {
                chord = "[E]"
                text = ""
            }
            chordPair {
                chord = "[E]"
                text = ""
            }
            chordPair {
                chord = "[E]"
                text = ""
            }

        }, parseSectionLineUpper("[E][E][E][E]\n-")
        )

    }

    @Test
    fun testStartingSpaces() {
        assertEquals(sectionLine {
            chordPair {
                chord = null
                text = " "
            }
            chordPair {
                chord = "[Dm]"
                text = "Gim"
            }

        }, parseSectionLineUpper(" [Dm]\n Gim")
        )

    }


}
