package eu.karelhovorka.zpevnik.text

import eu.karelhovorka.zpevnik.text.SectionTokenizer.Companion.TYPE_REGEX
import eu.karelhovorka.zpevnik.text.section.section
import eu.karelhovorka.zpevnik.text.section.sections
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

public class KotlinSectionTokenizerTest {

    @Test
    fun testRegex() {
        assertTrue("1.".matches(TYPE_REGEX.toRegex()))
        assertTrue("100.".matches(TYPE_REGEX.toRegex()))
        assertTrue("Recital:".matches(TYPE_REGEX.toRegex()))
        assertTrue("Recital: ".matches(TYPE_REGEX.toRegex()))
    }

    @Test
    fun testSimpleSectionVerse() {
        val sectionTokenizer = SectionTokenizer()
        val sectionList = sectionTokenizer.getSections("1.")

        assertEquals(
                section {
                    type = SectionType.VERSE
                    sectionText {

                    }
                },
                sectionList.first()
        )
    }

    @Test
    fun testSimpleSectionIntro() {
        val sectionTokenizer = SectionTokenizer()
        val sectionList = sectionTokenizer.getSections("Intro:")

        assertEquals(
                sections {
                    section {
                        type = SectionType.INTRO
                        sectionText {

                        }
                    }
                },
                sectionList
        )
    }

    @Test
    fun testCopySectionsVerse() {
        val sectionTokenizer = SectionTokenizer()
        val sectionList = sectionTokenizer.getSections("""|1.
            |První
            |
            |2.
            |Druhá
            |
            |1.
        """.trimMargin())

        assertEquals(
                sections {
                    section {
                        type = SectionType.VERSE
                        sectionText {
                            sectionLine {
                                chordPair {
                                    text = "První"
                                }
                            }
                        }
                    }
                    section {
                        type = SectionType.VERSE
                        index = 1
                        sectionText {
                            sectionLine {
                                chordPair {
                                    text = "Druhá"
                                }
                            }
                        }
                    }
                    section {
                        type = SectionType.VERSE
                        copyIndex = 1
                        sectionText {
                            sectionLine {
                                chordPair {
                                    text = "První"
                                }
                            }
                        }
                    }
                },
                sectionList
        )
    }


    @Test
    fun testNoCommentUpper() {
        val sectionTokenizer = SectionTokenizer()
        val sectionList = sectionTokenizer.getSections("""
R:
[A E]
Radka
        """.trimMargin().trimStart())

        assertEquals(
                sections {
                    section {
                        type = SectionType.CHORUS
                        sectionText {
                            sectionLine {
                                chordPair {
                                    chord = "[A]"
                                    text = "Ra"
                                }
                                chordPair {
                                    chord = "[E]"
                                    text = "dka"
                                }
                            }
                        }
                    }
                },
                sectionList
        )
    }

    @Test
    fun testCopySectionsChorus() {
        val sectionTokenizer = SectionTokenizer()
        val sectionList = sectionTokenizer.getSections("""|R:
            |První
            |
            |R:
            |Druhá
            |
            |R:
        """.trimMargin())

        assertEquals(
                sections {
                    section {
                        type = SectionType.CHORUS
                        sectionText {
                            sectionLine {
                                chordPair {
                                    text = "První"
                                }
                            }
                        }
                    }
                    section {
                        type = SectionType.CHORUS
                        index = 1
                        sectionText {
                            sectionLine {
                                chordPair {
                                    text = "Druhá"
                                }
                            }
                        }
                    }
                    section {
                        type = SectionType.CHORUS
                        copyIndex = 1
                        sectionText {
                            sectionLine {
                                chordPair {
                                    text = "První"
                                }
                            }
                        }
                    }
                },
                sectionList
        )
    }

    val fullText = "Intro:\n" +
            "Příklad možného zápisu sekcí Zpěvníku. Opakující sekce se nemusí opakovat (stačí zopakovat značku).\n" +
            "\n" +
            "R:\n" +
            "Tohle je refrén.\n" +
            "\n" +
            "1.\n" +
            "Tohle je první sloka, která se vyskytuje víckrát stejně, ale je zapsána jen jednou.\n" +
            "\n" +
            "Bridge:\n" +
            "Po tomhle bridge dáme zase první refrén.\n" +
            "\n" +
            "R:\n" +
            "\n" +
            "2.\n" +
            "Tohle je druhá sloka, jiná než ta první.\n" +
            "\n" +
            "Solo:\n" +
            "Sem si třeba dáme solo a potom zase první sloku (ale nezapisujeme jí celou znovu).\n" +
            "\n" +
            "1.\n" +
            "\n" +
            "Bridge:\n" +
            "A po tomhle bridge dáme už druhý refrén.\n" +
            "\n" +
            "R2:\n" +
            "Tohle je trochu jiný refrén.\n" +
            "\n" +
            "Bridge:\n" +
            "Potomto bridge si zase dáme první refrén.\n" +
            "\n" +
            "R:\n" +
            "\n" +
            "Outro:\n" +
            "A závěr."
    @Test
    fun sectionMixTest() {
        val fullText = "Intro:\n" +
                "Příklad možného zápisu sekcí Zpěvníku. Opakující sekce se nemusí opakovat (stačí zopakovat značku).\n" +
                "\n" +
                "R:\n" +
                "Tohle je refrén.\n" +
                "\n" +
                "1.\n" +
                "Tohle je první sloka, která se vyskytuje víckrát stejně, ale je zapsána jen jednou.\n" +
                "\n" +
                "Bridge:\n" +
                "Po tomhle bridge dáme zase první refrén.\n" +
                "\n" +
                "R:\n" +
                "\n" +
                "2.\n" +
                "Tohle je druhá sloka, jiná než ta první.\n" +
                "\n" +
                "Solo:\n" +
                "Sem si třeba dáme solo a potom zase první sloku (ale nezapisujeme jí celou znovu).\n" +
                "\n" +
                "1.\n" +
                "\n" +
                "Bridge:\n" +
                "A po tomhle bridge dáme už druhý refrén.\n" +
                "\n" +
                "R2:\n" +
                "Tohle je trochu jiný refrén.\n" +
                "\n" +
                "Bridge:\n" +
                "Po tomto bridge si zase dáme první refrén.\n" +
                "\n" +
                "R:\n" +
                "\n" +
                "Outro:\n" +
                "A závěr."


        val sectionTokenizer = SectionTokenizer()
        val sectionList = sectionTokenizer.getSections(fullText)
        assertEquals(
                sections {
                    section {
                        type = SectionType.INTRO
                        sectionText {
                            sectionLine {
                                chordPair {
                                    text = "Příklad možného zápisu sekcí Zpěvníku. Opakující sekce se nemusí opakovat (stačí zopakovat značku)."
                                }
                            }
                        }
                    }
                    section {
                        type = SectionType.CHORUS
                        sectionText {
                            sectionLine {
                                chordPair {
                                    text = "Tohle je refrén."
                                }
                            }
                        }
                    }
                    section {
                        type = SectionType.VERSE
                        sectionText {
                            sectionLine {
                                chordPair {
                                    text = "Tohle je první sloka, která se vyskytuje víckrát stejně, ale je zapsána jen jednou."
                                }
                            }
                        }
                    }
                    section {
                        type = SectionType.BRIDGE
                        sectionText {
                            sectionLine {
                                chordPair {
                                    text = "Po tomhle bridge dáme zase první refrén."
                                }
                            }
                        }
                    }
                    section {
                        type = SectionType.CHORUS
                        copyIndex = 1
                        sectionText {
                            sectionLine {
                                chordPair {
                                    text = "Tohle je refrén."
                                }
                            }
                        }
                    }
                    section {
                        type = SectionType.VERSE
                        index = 1
                        sectionText {
                            sectionLine {
                                chordPair {
                                    text = "Tohle je druhá sloka, jiná než ta první."
                                }
                            }
                        }
                    }
                    section {
                        type = SectionType.SOLO
                        sectionText {
                            sectionLine {
                                chordPair {
                                    text = "Sem si třeba dáme solo a potom zase první sloku (ale nezapisujeme jí celou znovu)."
                                }
                            }
                        }
                    }
                    section {
                        type = SectionType.VERSE
                        copyIndex = 1
                        sectionText {
                            sectionLine {
                                chordPair {
                                    text = "Tohle je první sloka, která se vyskytuje víckrát stejně, ale je zapsána jen jednou."
                                }
                            }
                        }
                    }
                    section {
                        type = SectionType.BRIDGE
                        index = 1
                        sectionText {
                            sectionLine {
                                chordPair {
                                    text = "A po tomhle bridge dáme už druhý refrén."
                                }
                            }
                        }
                    }
                    section {
                        type = SectionType.CHORUS
                        index = 1
                        sectionText {
                            sectionLine {
                                chordPair {
                                    text = "Tohle je trochu jiný refrén."
                                }
                            }
                        }
                    }
                    section {
                        type = SectionType.BRIDGE
                        index = 2
                        sectionText {
                            sectionLine {
                                chordPair {
                                    text = "Po tomto bridge si zase dáme první refrén."
                                }
                            }
                        }
                    }
                    section {
                        type = SectionType.CHORUS
                        copyIndex = 2
                        sectionText {
                            sectionLine {
                                chordPair {
                                    text = "Tohle je refrén."
                                }
                            }
                        }
                    }
                    section {
                        type = SectionType.OUTRO
                        sectionText {
                            sectionLine {
                                chordPair {
                                    text = "A závěr."
                                }
                            }
                        }
                    }
                },
                sectionList
        )
    }
}