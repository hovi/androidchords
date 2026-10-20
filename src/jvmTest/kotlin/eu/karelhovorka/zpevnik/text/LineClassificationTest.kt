package eu.karelhovorka.zpevnik.text

import eu.karelhovorka.zpevnik.text.section.SectionLine
import eu.karelhovorka.zpevnik.text.section.parseSectionText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LineClassificationTest {

    private fun lines(text: String) = parseSectionText(null, text).lines.map { show(it) }

    private fun show(line: SectionLine): String =
        line.chordToText.joinToString(" | ") { "${it.chord ?: "-"}:${it.text}" }

    @Test
    fun chordLineAboveInlineChordLineKeepsItsChords() {
        assertEquals(
            listOf("[C]:  | [G]:", "[Am]:Text  | [F]:dál"),
            lines(
                """
                [C] [G]
                [Am]Text [F]dál
                """.trimIndent()
            )
        )
    }

    @Test
    fun chordLineAboveBlankLineStandsAlone() {
        assertEquals(listOf("[C]:  | [G]:", "-:", "-:text"), lines("[C] [G]\n\ntext"))
    }

    @Test
    fun chordLineAboveChordLine() {
        assertEquals(listOf("[C]:  | [G]:", "[Am]:"), lines("[C] [G]\n[Am]"))
    }

    @Test
    fun chordLineAboveTextIsOneLine() {
        assertEquals(listOf("[C]:Hej | [G]: hou"), lines("[C]  [G]\nHej hou"))
    }

    @Test
    fun lastChordLine() {
        assertEquals(listOf("-:text", "[C]:  | [G]:"), lines("text\n[C] [G]"))
    }

    @Test
    fun textLines() {
        assertEquals(listOf("-:one", "-:two"), lines("one\ntwo"))
    }

    @Test
    fun sourceOfUpperLine() {
        val line = parseSectionText(null, "  [C]  [G]\n  Hej hou 2x").lines.single()
        assertEquals("  [C]  [G]", line.source!!.chordLine)
        assertEquals("  ", line.source!!.indent)
        assertEquals(" 2x", line.source!!.repeatSuffix)
    }

    @Test
    fun sourceOfInlineLine() {
        val line = parseSectionText(null, "[C]Hej (3x)").lines.single()
        assertNull(line.source!!.chordLine)
        assertEquals(" (3x)", line.source!!.repeatSuffix)
    }
}
