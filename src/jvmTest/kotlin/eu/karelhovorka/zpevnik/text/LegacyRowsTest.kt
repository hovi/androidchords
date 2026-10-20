package eu.karelhovorka.zpevnik.text

import eu.karelhovorka.zpevnik.formatter.HtmlSnapshots
import eu.karelhovorka.zpevnik.util.I18N
import eu.karelhovorka.zpevnik.util.ToneTransposer
import eu.karelhovorka.zpevnik.util.expandTabs
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * The legacy layout used to print the preprocessed text as is. It is now rebuilt from the parsed
 * sections and must show the same text.
 */
class LegacyRowsTest {

    // tabs become spaces, plain chords in front of a header line get brackets, trailing spaces are invisible
    private fun normalize(text: String) = text.replace("\r", "").lines()
        .joinToString("\n") { ToneTransposer.removeBrackets(expandTabs(it)).trimEnd() }

    @Test
    fun legacyRowsShowTheTextAsWritten() {
        for (song in HtmlSnapshots.songs()) {
            for ((variant, settings) in HtmlSnapshots.variants) {
                val songText = SongText.fromRawText(song.readText(), song.name, settings, I18N())
                val rows = songText.legacyRows.joinToString("") { it.text + it.end }
                assertEquals("${song.name} $variant", normalize(songText.originalText), normalize(rows))
            }
        }
    }

    @Test
    fun rowsSplitIntoChordAndTextParts() {
        val row = SongText.fromRawText("[C]Hej [G]hou", "", SongDisplaySettings(), I18N()).legacyRows.single()
        assertEquals(listOf("legacy-chord:[C]", "legacy-text:Hej ", "legacy-chord:[G]", "legacy-text:hou"), row.parts.map { "${it.css}:${it.text}" })
    }

    @Test
    fun upperLineIsTwoRows() {
        val rows = SongText.fromRawText("  C     G\n  Hej hou hej", "", SongDisplaySettings(), I18N()).legacyRows
        assertEquals(listOf("  [C]     [G]", "  Hej hou hej"), rows.map { it.text })
        assertEquals(listOf(true, false), rows.map { it.css.contains("chord-row") })
    }

    @Test
    fun bareRepeatShowsOnlyItsHeader() {
        val rows = SongText.fromRawText("R:\nla la\n1.\nx\nR:", "", SongDisplaySettings(), I18N()).legacyRows
        assertEquals(listOf("R:", "la la", "1.", "x", "R:"), rows.map { it.text })
    }

    @Test
    fun headerOnSameLineAsText() {
        val rows = SongText.fromRawText("1. Když jsem šel\n   do lesa\nR: [C]  [G]\n   La la la", "", SongDisplaySettings(), I18N()).legacyRows
        assertEquals(listOf("1. Když jsem šel", "   do lesa", "R: [C]  [G]", "   La la la"), rows.map { it.text })
    }

    @Test
    fun lastRowHasNoLineBreak() {
        val rows = SongText.fromRawText("a\nb", "", SongDisplaySettings(), I18N()).legacyRows
        assertEquals(listOf("\n", ""), rows.map { it.end })
    }
}
