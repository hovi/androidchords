import eu.karelhovorka.zpevnik.music.Interval
import eu.karelhovorka.zpevnik.text.SongDisplaySettings
import eu.karelhovorka.zpevnik.text.SongText
import eu.karelhovorka.zpevnik.util.ChordDetector
import eu.karelhovorka.zpevnik.util.CsHardcoded
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Runs the main code paths on JS: regexes there use the unicode flag, which rejects some
 * escapes the JVM accepts, so a pattern can work in every JVM test and fail in the browser.
 */
class JsSmokeTest {

    private val song = """
        Intro: C G Am7b5 F
        1. [C]Když jsem [G]šel
           do [Am]lesa (2x)
        R:
        C       Hmadd11
        /: Hej hou hej :/
        # komentář
        R:
    """.trimIndent()

    private fun songText(settings: SongDisplaySettings = SongDisplaySettings()) =
        SongText.fromRawText(song, "Title", settings, CsHardcoded)

    @Test
    fun parsesSections() {
        val types = songText().sections.map { it.type.name }
        assertEquals(listOf("INTRO", "VERSE", "CHORUS", "CHORUS"), types)
    }

    @Test
    fun parsesChordsAndRepeats() {
        val lines = songText().sections.flatMap { it.content.lines.toList() }
        val chords = lines.flatMap { line -> line.chordToText.mapNotNull { it.chord } }
        assertTrue("[Am7b5]" in chords && "[Hmadd11]" in chords, chords.toString())
        assertEquals(2, lines.mapNotNull { it.repeatCount }.first())
        assertTrue(lines.any { line -> line.chordToText.any { pair -> pair.segments.any { it.css == "repeat-start" } } })
    }

    @Test
    fun transposes() {
        val chords = songText(SongDisplaySettings(interval = Interval.of(2))).sections.first().content.lines
            .flatMap { line -> line.chordToText.mapNotNull { it.chord } }
        assertEquals(listOf("[D]", "[A]", "[Hm7b5]", "[G]"), chords)
    }

    @Test
    fun hidesChordsTextAndRepeatedSequences() {
        songText(SongDisplaySettings(isDisplayChords = false))
        songText(SongDisplaySettings(isDisplayText = false))
        songText(SongDisplaySettings(isHideIdenticalSequences = true, showComments = true))
    }

    @Test
    fun legacyRows() {
        val rows = songText().legacyRows
        assertEquals("Intro: [C] [G] [Am7b5] [F]", rows.first().text)
        assertTrue(rows.first().parts.any { it.css == "legacy-chord" })
    }

    @Test
    fun detectsInvalidChords() {
        assertEquals(listOf("Es"), ChordDetector.invalidChords("[Es] [C]").toList())
        assertEquals("[C] [G]", ChordDetector.replacePlainChords("C G"))
    }

    @Test
    fun malformedChordFailsFast() {
        // the web editor parses the song on every key press
        val start = kotlin.js.Date.now()
        SongText.fromRawText("[C" + "1".repeat(40) + "z] [C" + "sus4".repeat(12) + "z]", "", SongDisplaySettings(), CsHardcoded)
        val millis = kotlin.js.Date.now() - start
        assertTrue(millis < 500, "took $millis ms")
    }
}
