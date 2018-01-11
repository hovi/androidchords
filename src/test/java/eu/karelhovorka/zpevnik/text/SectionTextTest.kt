package eu.karelhovorka.zpevnik.text

import eu.karelhovorka.zpevnik.music.Interval
import eu.karelhovorka.zpevnik.text.section.parseSectionText
import eu.karelhovorka.zpevnik.text.section.sectionText
import eu.karelhovorka.zpevnik.util.ChordDetector
import eu.karelhovorka.zpevnik.util.StringUtil
import eu.karelhovorka.zpevnik.util.Tone
import eu.karelhovorka.zpevnik.util.ToneTransposer
import org.junit.Assert.assertEquals
import org.junit.Test

class SectionTextTest {

    @Test
    fun textLine() {
        assertEquals(
                sectionText {
                    sectionLine {
                        chordPair {
                            chord = null
                            text = "no chords there"
                        }

                    }
                }
                , parseSectionText("no chords there"))
    }

    @Test
    fun textInLine() {
        assertEquals(
                sectionText {
                    sectionLine {
                        chordPair {
                            chord = "[A]"
                            text = "no chords there"
                        }

                    }
                }
                , parseSectionText("[A]no chords there"))
    }

    @Test
    fun textUpper() {
        assertEquals(
                sectionText {
                    sectionLine {
                        chordPair {
                            chord = "[A]"
                            text = "no chords there"
                        }
                    }
                }
                , parseSectionText("[A]\nno chords there"))
    }

    @Test
    fun textMixed() {
        assertEquals(
                sectionText {
                    sectionLine {
                        chordPair {
                            chord = "[A]"
                            text = "no chords there"
                        }
                    }
                    sectionLine {
                        chordPair {
                            text = ""
                        }
                    }
                    sectionLine {
                        chordPair {
                            chord = "[A]"
                            text = "inline text"
                        }
                    }
                }
                , parseSectionText("[A]\nno chords there\n\n[A]inline text"))
    }

    @Test
    fun textEmptyLines1() {
        assertEquals(
                sectionText {
                    sectionLine {
                        chordPair {
                            chord = "[A]"
                            text = "no chords there"
                        }
                    }
                    sectionLine {
                        chordPair {
                            text = "X"
                        }
                    }
                    sectionLine {
                        chordPair {
                            text = "Y"
                        }
                    }
                    sectionLine {
                        chordPair {
                            chord = "[A]"
                            text = "inline text"
                        }
                    }
                }
                , parseSectionText("[A]\nno chords there\nX\nY\n[A]inline text"))
    }

    @Test
    fun textEmptyLines2() {
        assertEquals(
                sectionText {
                    sectionLine {
                        chordPair {
                            chord = "[A]"
                            text = "no chords there"
                        }
                    }
                    sectionLine {
                        chordPair {
                            text = ""
                        }
                    }
                    sectionLine {
                        chordPair {
                            text = ""
                        }
                    }
                    sectionLine {
                        chordPair {
                            chord = "[A]"
                            text = "inline text"
                        }
                    }
                }
                , parseSectionText("[A]\nno chords there\n\n\n[A]inline text"))
    }


    @Test
    fun testEmptyLine() {
        assertEquals(
                sectionText {
                    sectionLine {
                        chordPair {
                            chord = "[A]"
                            text = ""
                        }
                    }
                    sectionLine {
                        chordPair {
                            text = ""
                        }
                    }
                    sectionLine {
                        chordPair {
                            text = "A"
                        }
                    }
                }
                , parseSectionText("[A]\n\nA"))

    }

    @Test
    fun testEmptyLine2() {
        var text2 = "[C G Am D]\n" +
                "\n" +
                "Blue skyes are long gone"
        println(text2)
        println(text2)
        text2 = ChordDetector.replacePlainChords(text2)
        println(text2)
        text2 = ToneTransposer.transposeAll(text2, Interval.PERFECT_UNISON, Tone.CountryCategory.EASTERN, Tone.ModificationAbbreviation.SHARP)
        println(text2)
        text2 = StringUtil.removeTrailingWhitespace(text2)
        println(text2)
        assertEquals(
                sectionText {
                    sectionLine {
                        chordPair {
                            chord = "[C]"
                            text = " "
                        }
                        chordPair {
                            chord = "[G]"
                            text = " "
                        }
                        chordPair {
                            chord = "[Am]"
                            text = " "
                        }
                        chordPair {
                            chord = "[D]"
                            text = ""
                        }
                    }
                    sectionLine {
                        chordPair {
                            text = ""
                        }
                    }
                    sectionLine {
                        chordPair {
                            text = "Blue skyes are long gone"
                        }
                    }
                }
                , parseSectionText(text2))

    }

}
