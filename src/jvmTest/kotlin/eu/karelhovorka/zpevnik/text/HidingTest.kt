package eu.karelhovorka.zpevnik.text

import eu.karelhovorka.zpevnik.formatter.HtmlSnapshots
import eu.karelhovorka.zpevnik.text.section.parseSectionText
import eu.karelhovorka.zpevnik.util.ChordDetector
import eu.karelhovorka.zpevnik.util.Comments
import eu.karelhovorka.zpevnik.util.I18N
import eu.karelhovorka.zpevnik.util.ToneTransposer
import eu.karelhovorka.zpevnik.util.Transposer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Display settings used to delete text before parsing; now everything is parsed and the stylesheet hides
 * by class. Here the hiding rules of style.scss are applied to the legacy rows (which show the whole song)
 * and must leave what the old text filtering left.
 */
class HidingTest {

    private val settings = mapOf(
        "hide-chords" to SongDisplaySettings(isDisplayChords = false),
        "hide-text" to SongDisplaySettings(isDisplayText = false),
        "hide-comments" to SongDisplaySettings(showComments = false),
        "show-comments" to SongDisplaySettings(showComments = true),
        "hide-identical-sequences" to SongDisplaySettings(isHideIdenticalSequences = true, showComments = true)
    )

    /** What SongSettingsFormatter did before display settings moved to CSS. */
    private fun oldFilteredText(source: String, s: SongDisplaySettings): String {
        // the old filtering removed text before "[C G Am]" was split into chords and lost those chords,
        // the comparison uses split chords to check the rest
        var text = Transposer.merge(ChordDetector.replacePlainChords(source))
        if (!s.isDisplayText) {
            text = Transposer.removeNonChords(text)
        }
        if (s.isHideIdenticalSequences) {
            text = Transposer.removeDuplicateChordSequences(text)
        }
        text = if (s.isDisplayChords) {
            ToneTransposer.transposeAll(text, s.interval, s.countryCategory, s.modificationAbbreviation)
        } else {
            Transposer.removeChords(text)
        }
        text = if (s.showComments) Comments.markComments(text) else Comments.hideComments(text)
        return text.trimEnd()
    }

    /** The rules of style.scss for the legacy layout, applied to row and part classes. */
    private fun visibleLegacyText(songText: SongText, s: SongDisplaySettings): String {
        val root = s.css.split(" ").toSet()
        return songText.legacyRows.filter { row ->
            val css = row.css.split(" ").toSet()
            val header = "section-header" in css
            val chordOnly = "song-line-has-chords" in css && "song-line-no-text" in css && !header
            val chordRow = "chord-row" in css && !header
            !("hide-chords" in root && (chordRow || chordOnly)) &&
                !("hide-text" in root && ("text-row" in css || "song-line-has-chords" !in css)) &&
                !("hide-comments" in root && "comment-line" in css) &&
                !("hide-identical-sequences" in root && "repeated-chords" in css && (chordRow || chordOnly))
        }.joinToString("\n") { row ->
            val css = row.css.split(" ").toSet()
            row.parts.filter { part ->
                !(part.css == "legacy-chord" && ("hide-chords" in root || ("hide-identical-sequences" in root && "repeated-chords" in css))) &&
                    !(part.css == "legacy-text" && "hide-text" in root)
            }.joinToString("") { it.text }
        }
    }

    private fun normalize(text: String) = text.replace("\r", "").lines()
        .joinToString("\n") { ToneTransposer.removeBrackets(it).replace(Regex("\\s+"), " ").trim() }
        .replace(Regex("\n{2,}"), "\n\n").trim()

    @Test
    fun cssHidingLeavesWhatTextFilteringLeft() {
        for (song in HtmlSnapshots.songs()) {
            for ((name, s) in settings) {
                val source = song.readText()
                val songText = SongText.fromRawText(source, song.name, s, I18N())
                var expected = normalize(oldFilteredText(source, s))
                var actual = normalize(visibleLegacyText(songText, s))
                if (s.isHideIdenticalSequences) {
                    // the old filtering left an empty line where a repeated line had only chords, now it is hidden
                    expected = expected.replace(Regex("\n+"), "\n")
                    actual = actual.replace(Regex("\n+"), "\n")
                }
                assertEquals("${song.name} $name", expected, actual)
            }
        }
    }

    @Test
    fun commentLines() {
        val lines = parseSectionText(null, Comments.markComments("# a comment\ntext # not a comment")).lines
        assertEquals(listOf(true, false), lines.map { it.isComment })
        assertTrue("comment-line" in lines[0].css())
    }

    @Test
    fun repeatedChordSequencesAcrossSections() {
        val sections = SectionTokenizer().getSections(
            """
            1.
            [C]a [G]b
            [C]c
            [C]d
            R:
            [C]e [G]f
            [G]g [C]h
            """.trimIndent()
        )
        val flags = sections.flatMap { section -> section.content.lines.map { it.repeatedChords } }
        // one chord is never a sequence; same chords in another order are another sequence
        assertEquals(listOf(false, false, false, true, false), flags)
        assertTrue("repeated-chords" in sections[1].content.lines[0].css())
    }

    @Test
    fun repeatedFlagIsNotPartOfEquality() {
        val sections = SectionTokenizer().getSections("R:\n[C]a [G]b\n1.\nx\nR:\n[C]a [G]b")
        assertTrue(sections[2].isCopy)
        assertFalse(sections[2].isRepeatOnly)
    }

    @Test
    fun rootClasses() {
        assertEquals("theme-light font-normal hide-comments", SongDisplaySettings().css)
        assertEquals(
            "theme-dark font-serif bold big-chords double-column hide-chords hide-text hide-identical-sequences",
            SongDisplaySettings(
                isDisplayText = false, isDisplayChords = false, isHideIdenticalSequences = true, isUseBold = true,
                isDoubleColumn = true, isResizeChords = true, showComments = true, theme = "dark", fontFamily = "serif"
            ).css
        )
    }
}
