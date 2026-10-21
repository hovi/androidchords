package eu.karelhovorka.zpevnik.text

import eu.karelhovorka.zpevnik.util.CsHardcoded
import org.junit.Assert.assertEquals
import org.junit.Test

class SectionNamesTest {

    private val song = """
        Intro:
        [C] [G]
        1.
        [C]Ráno
        R:
        [F]Zahraj
        2.
        [C]Večer
        R:
    """.trimIndent()

    private fun names(sectionNames: Map<String, String> = emptyMap()) =
        SongText.fromRawText(song, "", SongDisplaySettings(sectionNames = sectionNames), CsHardcoded).sections.map { it.localName }

    @Test
    fun defaultNamesAreNumberedFromTheSecondSectionOfAType() {
        assertEquals(listOf("Předehra", "Sloka", "Refrén", "Sloka (2)", "Refrén"), names())
    }

    @Test
    fun templatesReplaceTheNamesOfTheirTypes() {
        val names = names(mapOf("verse" to "{index1}.", "chorus" to "Ref. {index0}/{index1}"))
        // a repeated chorus is the same section, so it keeps its number
        assertEquals(listOf("Předehra", "1.", "Ref. 0/1", "2.", "Ref. 0/1"), names)
    }

    @Test
    fun everySectionTypeHasADefaultName() {
        val types = SectionType.values().filter { it != SectionType.EMPTY }.map { it.css() }.toSet()
        assertEquals(types, StyleDefaults.sectionNames.keys)
    }
}
