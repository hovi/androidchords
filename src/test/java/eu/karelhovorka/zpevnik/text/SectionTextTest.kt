package eu.karelhovorka.zpevnik.text

import eu.karelhovorka.zpevnik.text.section.parseSectionText
import eu.karelhovorka.zpevnik.text.section.sectionText
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


}
