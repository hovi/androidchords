package eu.karelhovorka.zpevnik.text

import eu.karelhovorka.zpevnik.text.section.ChordPair
import eu.karelhovorka.zpevnik.text.section.SectionLine
import eu.karelhovorka.zpevnik.text.section.TextSegment
import eu.karelhovorka.zpevnik.text.section.parseSectionText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RepeatTest {

    private fun lines(text: String) = parseSectionText(null, text).lines

    private fun show(line: SectionLine): String =
        line.chordToText.joinToString(" | ") { "${it.chord ?: "-"}:${it.text}" }

    private fun segments(text: String) = ChordPair(null, text).segments

    @Test
    fun repeatCountAtEndOfTextLine() {
        for (text in listOf("Hej hou 2x", "Hej hou (2x)", "Hej hou x2", "Hej hou 2×", "Hej hou  2x  ", "Hej hou (x2)")) {
            val line = lines(text).single()
            assertEquals(text, 2, line.repeatCount)
            assertEquals(text, "-:Hej hou", show(line))
        }
    }

    @Test
    fun repeatCountNeedsSpaceBefore() {
        val line = lines("Hej hou2x").single()
        assertNull(line.repeatCount)
        assertEquals("-:Hej hou2x", show(line))
    }

    @Test
    fun repeatCountOnTwoDigits() {
        assertEquals(12, lines("Hej 12x").single().repeatCount)
    }

    @Test
    fun noRepeatCount() {
        assertNull(lines("Hej hou").single().repeatCount)
        assertNull(lines("Malcolm X").single().repeatCount)
    }

    @Test
    fun repeatCountOnChordLineAboveText() {
        val line = lines(
            """
            [C]   [G]   2x
            Hej hou hej hou
            """.trimIndent()
        ).single()
        assertEquals(2, line.repeatCount)
        assertEquals("[C]:Hej  | [G]:hou hej hou", show(line))
    }

    @Test
    fun repeatCountOnChordOnlyLine() {
        val line = lines("[C] [G] [Am] 2x").single()
        assertEquals(2, line.repeatCount)
        assertEquals(listOf("[C]", "[G]", "[Am]"), line.chordToText.map { it.chord })
    }

    @Test
    fun repeatCountOnInlineChordLine() {
        val line = lines("[C]Hej [G]hou 3x").single()
        assertEquals(3, line.repeatCount)
        assertEquals("[C]:Hej  | [G]:hou", show(line))
    }

    @Test
    fun repeatMarksInText() {
        assertEquals(
            listOf(TextSegment.REPEAT_START, TextSegment.text(" Hej hou "), TextSegment.REPEAT_END),
            segments("/: Hej hou :/")
        )
        assertEquals(
            listOf(TextSegment.REPEAT_START, TextSegment.text(" Hej "), TextSegment.REPEAT_END),
            segments("|: Hej :|")
        )
        assertEquals(listOf(TextSegment.REPEAT_START, TextSegment.text("Hej")), segments("/:Hej"))
        assertEquals(listOf(TextSegment.text("hou"), TextSegment.REPEAT_END), segments("hou:/"))
    }

    @Test
    fun plainTextIsOneSegment() {
        assertEquals(listOf(TextSegment.text("Hej hou")), segments("Hej hou"))
        assertEquals(listOf(TextSegment.text("")), segments(""))
    }

    @Test
    fun urlIsNotRepeatMark() {
        assertEquals(listOf(TextSegment.text("see http://example.com")), segments("see http://example.com"))
    }

    @Test
    fun repeatMarksOnChordLineKeepAlignment() {
        val line = lines(
            """
            /: [C]     [G] :/
            /: Hej hou hej :/
            """.trimIndent()
        ).single()
        assertEquals(listOf("[C]", "[G]"), line.chordToText.mapNotNull { it.chord })
        assertEquals("-:/:  | [C]:Hej ho | [G]:u hej :/", show(line))
    }

    @Test
    fun repeatBarsOnChordLineBelongToTheLine() {
        val line = lines("/: [C] [G] :/\nHello world").single()
        assertEquals(true, line.repeatStart)
        assertEquals(true, line.repeatEnd)
        assertEquals(listOf("[C]", "[G]"), line.chordToText.mapNotNull { it.chord })
    }

    @Test
    fun repeatBarsOnBothLinesAreOneRepeat() {
        val line = lines("/: [C] [G] :/\n/: Hello world :/").single()
        assertEquals(false, line.repeatStart)
        assertEquals(false, line.repeatEnd)
        assertEquals(listOf(TextSegment.REPEAT_START), line.chordToText.flatMap { it.segments }.filter { it.css == "repeat-start" })
    }

    @Test
    fun onlyStartBarOnChordLine() {
        val line = lines("|: [C]    [G]\nHello world :|").single()
        assertEquals(true, line.repeatStart)
        assertEquals(false, line.repeatEnd)
    }
}
