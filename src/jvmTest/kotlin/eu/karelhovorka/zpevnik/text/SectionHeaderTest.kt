package eu.karelhovorka.zpevnik.text

import eu.karelhovorka.zpevnik.text.section.SectionLine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SectionHeaderTest {

    private fun sections(text: String) = SectionTokenizer().getSectionsOrSingleSection(text)

    private fun show(line: SectionLine): String =
        line.chordToText.joinToString(" | ") { "${it.chord ?: "-"}:${it.text}" }

    private fun lines(section: Section) = section.content.lines.map { show(it) }

    @Test
    fun accentedHeaders() {
        val result = sections(
            """
            Předehra:
            aaa
            Úvod:
            bbb
            Recitál:
            ccc
            Refrén:
            ddd
            """.trimIndent()
        )
        assertEquals(listOf(SectionType.INTRO, SectionType.INTRO, SectionType.SPEECH, SectionType.CHORUS), result.map { it.type })
        assertEquals(listOf("-:aaa"), lines(result[0]))
    }

    @Test
    fun headersWithSpaceBeforeNumber() {
        val result = sections(
            """
            Verse 1:
            aaa
            Ref. 2:
            bbb
            Chorus:
            ccc
            Sloka 3:
            ddd
            """.trimIndent()
        )
        assertEquals(listOf(SectionType.VERSE, SectionType.CHORUS, SectionType.CHORUS, SectionType.VERSE), result.map { it.type })
        assertEquals(listOf(0, 1, 1, 2), result.map { it.index })
    }

    @Test
    fun unknownCapitalizedHeaderOnOwnLine() {
        val result = sections(
            """
            Pozn:
            aaa
            R:
            bbb
            """.trimIndent()
        )
        assertEquals("Pozn", result[0].type.name.trimEnd(':'))
        assertEquals(SectionType.CHORUS, result[1].type)
    }

    @Test
    fun headerWithTextOnSameLine() {
        val result = sections(
            """
            1. Když jsem šel
            do lesa
            R: La la la
            hej hou
            2. Druhá sloka
            """.trimIndent()
        )
        assertEquals(listOf(SectionType.VERSE, SectionType.CHORUS, SectionType.VERSE), result.map { it.type })
        assertEquals(listOf("-:Když jsem šel", "-:do lesa"), lines(result[0]))
        assertEquals(listOf("-:La la la", "-:hej hou"), lines(result[1]))
        assertEquals(listOf("-:Druhá sloka"), lines(result[2]))
        assertEquals(listOf(0, 0, 1), result.map { it.index })
    }

    @Test
    fun unknownLabelWithTextOnSameLineIsNotHeader() {
        val result = sections(
            """
            Pozor: tohle je text
            Bůh: taky text
            """.trimIndent()
        )
        assertEquals(1, result.size)
        assertEquals(SectionType.EMPTY, result[0].type)
        assertEquals(listOf("-:Pozor: tohle je text", "-:Bůh: taky text"), lines(result[0]))
    }

    @Test
    fun headerWithChordsOnSameLine() {
        val result = sections(
            """
            R: [C]     [G]
               La la la la
            """.trimIndent()
        )
        assertEquals(SectionType.CHORUS, result[0].type)
        assertEquals(listOf("[C]:La la  | [G]:la la"), lines(result[0]))
    }

    @Test
    fun chordLineAboveSameLineHeaderBelongsToNewSection() {
        val result = sections(
            """
            1. Prvni
               [Am]   [C]
            2. Druha sloka
            """.trimIndent()
        )
        assertEquals(listOf("-:Prvni"), lines(result[0]))
        assertEquals(listOf("[Am]:Druha | [C]: sloka"), lines(result[1]))
    }

    @Test
    fun numberedLyricWithoutSpaceIsNotHeader() {
        val result = sections("1.text\n2.text")
        assertEquals(SectionType.EMPTY, result[0].type)
    }

    @Test
    fun sectionWithDifferentContentIsNotCopy() {
        val result = sections(
            """
            1.
            foo
            2.
            bar
            1.
            baz
            """.trimIndent()
        )
        assertEquals(listOf("-:foo"), lines(result[0]))
        assertEquals(listOf("-:baz"), lines(result[2]))
        assertFalse(result[2].isCopy)
    }

    @Test
    fun emptySectionIsCopyOfSameIndex() {
        val result = sections(
            """
            R:
            foo
            1.
            x
            R:
            """.trimIndent()
        )
        assertEquals(listOf("-:foo"), lines(result[2]))
        assertTrue(result[2].isCopy)
        assertTrue(result[2].isRepeatOnly)
        assertEquals(1, result[2].copyIndex)
        assertFalse(result[0].isCopy)
    }

    @Test
    fun sameContentIsCopy() {
        val result = sections(
            """
            R:
            foo
            1.
            x
            R:
            foo
            R:
            different
            """.trimIndent()
        )
        assertTrue(result[2].isCopy)
        assertFalse(result[2].isRepeatOnly)
        assertFalse(result[3].isCopy)
        assertEquals(listOf("-:different"), lines(result[3]))
    }

    @Test
    fun explicitChorusIndexCopy() {
        val result = sections(
            """
            R1:
            foo
            R2:
            bar
            R1:
            """.trimIndent()
        )
        assertEquals(listOf("-:foo"), lines(result[2]))
        assertTrue(result[2].isCopy)
    }

    @Test
    fun explicitChorusIndexWithDifferentContentIsNotCopy() {
        val result = sections(
            """
            R1:
            foo
            1.
            x
            R1:
            bar
            """.trimIndent()
        )
        assertEquals(listOf("-:bar"), lines(result[2]))
        assertFalse(result[2].isCopy)
    }

    @Test
    fun plainChordsOnHeaderLine() {
        val result = sections("Intro: C G Am F\n1.\ntext")
        assertEquals(SectionType.INTRO, result[0].type)
        assertEquals(listOf("[C]", "[G]", "[Am]", "[F]"), result[0].content.lines.flatMap { line -> line.chordToText.mapNotNull { it.chord } })
    }

    @Test
    fun plainChordsOnHeaderLineAboveText() {
        val result = sections(
            """
            R: C       G
               Refrén s akordy
            """.trimIndent()
        )
        assertEquals(listOf("[C]:Refrén s | [G]: akordy"), lines(result[0]))
    }

    @Test
    fun headerChordsStayInTheirSection() {
        val result = sections(
            """
            Mezihra: [Am] [F]
            R: La la
            """.trimIndent()
        )
        assertEquals(listOf(SectionType.INTERMEZZO, SectionType.CHORUS), result.map { it.type })
        assertEquals(listOf("[Am]", "[F]"), result[0].content.lines.flatMap { line -> line.chordToText.mapNotNull { it.chord } })
        assertEquals(listOf("-:La la"), lines(result[1]))
    }
}
