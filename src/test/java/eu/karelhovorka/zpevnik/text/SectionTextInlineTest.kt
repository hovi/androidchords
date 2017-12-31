package eu.karelhovorka.zpevnik.text

import eu.karelhovorka.zpevnik.text.section.parseSectionLineInline
import eu.karelhovorka.zpevnik.text.section.sectionLine
import org.junit.Assert.assertEquals
import org.junit.Test

class SectionTextInlineTest {

    @Test
    fun textLine() {
        val line_ = "no chords there"
        assertEquals(sectionLine {
            chordPair {
                chord = null
                text = "no chords there"
            }

        }, parseSectionLineInline(line_))
    }

    @Test
    fun textLine1Chord() {
        val line_ = "[A]1 chord there"
        assertEquals(sectionLine {
            chordPair {
                chord = "[A]"
                text = "1 chord there"
            }

        }, parseSectionLineInline(line_))
    }

    @Test
    fun textLine2Chords() {
        assertEquals(sectionLine {
            chordPair {
                chord = "[A]"
                text = "2 chords"
            }
            chordPair {
                chord = "[E]"
                text = " there"
            }

        }, parseSectionLineInline("[A]2 chords[E] there"))
    }

    @Test
    fun textLineNotStartingWithChord() {
        assertEquals(sectionLine {
            chordPair {
                chord = null
                text = "1 chord"
            }
            chordPair {
                chord = "[E]"
                text = " there"
            }
        }, parseSectionLineInline("1 chord[E] there"))
    }


    @Test
    fun textLineEndingWithChord() {
        assertEquals(sectionLine {
            chordPair {
                chord = null
                text = "1 chord"
            }
            chordPair {
                chord = "[E]"
                text = " there"
            }
            chordPair {
                chord = "[A]"
                text = ""
            }

        }, parseSectionLineInline("1 chord[E] there[A]"))
    }

    @Test
    fun textChordOnly() {
        val line = "[E]"
        val sectionLine = parseSectionLineInline(line)
        assertEquals(1, sectionLine.chordToText.size)
        val firstPair = sectionLine.chordToText[0]
        assertEquals("[E]", firstPair.chord)
        assertEquals("", firstPair.text)
    }

    @Test
    fun testBuilder() {
        assertEquals(
                sectionLine {
                    chordPair {
                        chord = "[E]"
                        text = ""
                    }

                }, parseSectionLineInline("[E]")
        )

    }


}
