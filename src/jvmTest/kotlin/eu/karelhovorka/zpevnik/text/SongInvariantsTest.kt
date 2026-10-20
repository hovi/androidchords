package eu.karelhovorka.zpevnik.text

import eu.karelhovorka.zpevnik.music.Interval
import eu.karelhovorka.zpevnik.text.section.splitRepeatCount
import eu.karelhovorka.zpevnik.util.ChordDetector
import eu.karelhovorka.zpevnik.util.Tone.CountryCategory
import eu.karelhovorka.zpevnik.util.Tone.ModificationAbbreviation
import eu.karelhovorka.zpevnik.util.ToneTransposer
import eu.karelhovorka.zpevnik.util.Transposer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * Properties that must hold for every song, whatever its format.
 * Drop more songs into src/jvmTest/resources/songs to cover them too.
 */
class SongInvariantsTest {

    private val songs: Map<String, String> = listOf("songs", "tokenizer")
        .flatMap { dir -> resourceDir(dir).listFiles()!!.filter { it.extension == "txt" } }
        .associate { it.name to it.readText() }

    private fun resourceDir(name: String): File =
        listOf("src/jvmTest/resources/$name", "chords/src/jvmTest/resources/$name").map { File(it) }.first { it.isDirectory }

    private fun prepare(text: String) = Transposer.merge(ChordDetector.replacePlainChords(text))

    private val chordRegex = Transposer.CHORD_REGEX.toRegex()

    private fun writtenSections(text: String) = SectionTokenizer().getSections(prepare(text)).filterNot { it.isRepeatOnly }

    @Test
    fun songsAreLoaded() {
        assertTrue(songs.size >= 10)
    }

    @Test
    fun parsingDoesNotFail() {
        for ((name, text) in songs) {
            try {
                SectionTokenizer().getSections(prepare(text))
            } catch (e: Exception) {
                throw AssertionError("$name: ${e.message}", e)
            }
        }
    }

    @Test
    fun noChordIsLostOrReordered() {
        for ((name, text) in songs) {
            val expected = prepare(text).lines().flatMap { line -> chordRegex.findAll(expectedLine(line)).map { it.value } }
            val actual = writtenSections(text).flatMap { section ->
                section.content.lines.flatMap { line -> line.chordToText.mapNotNull { it.chord } }
            }
            assertEquals(name, expected, actual)
        }
    }

    @Test
    fun noLyricWordIsLostOrAdded() {
        for ((name, text) in songs) {
            val expected = prepare(text).lines().flatMap { expectedWords(it) }
            val actual = writtenSections(text).flatMap { section ->
                section.content.lines.flatMap { line -> words(line.chordToText.joinToString("") { it.text }) }
            }
            assertEquals(name, expected, actual)
        }
    }

    @Test
    fun transpositionRoundTripIsStable() {
        for ((name, text) in songs) {
            for (category in CountryCategory.values()) {
                for (abbreviation in ModificationAbbreviation.values()) {
                    val transpose = { t: String, step: Int -> ToneTransposer.transposeAll(t, Interval.of(step), category, abbreviation) }
                    val normalized = transpose(transpose(prepare(text), 1), -1)
                    for (step in 2..11) {
                        assertEquals("$name $category $abbreviation +$step", normalized, transpose(transpose(prepare(text), step), -step))
                    }
                }
            }
        }
    }

    @Test
    fun transpositionKeepsChordCount() {
        for ((name, text) in songs) {
            val prepared = prepare(text)
            val count = chordRegex.findAll(prepared).count()
            for (step in 0..11) {
                val transposed = ToneTransposer.transposeAll(prepared, Interval.of(step), CountryCategory.EASTERN, ModificationAbbreviation.SHARP)
                assertEquals("$name +$step", count, chordRegex.findAll(transposed).count())
            }
        }
    }

    @Test
    fun preparingIsIdempotent() {
        for ((name, text) in songs) {
            assertEquals(name, prepare(text), prepare(prepare(text)))
        }
    }

    private fun words(text: String) = text.split("\\s+".toRegex()).filter { it.isNotEmpty() }

    private fun expectedWords(line: String): List<String> = words(Transposer.removeChords(expectedLine(line)))

    /** The line as the parser should see it: without a header label and repeat count, plain chords bracketed. */
    private fun expectedLine(line: String): String {
        if (line.trim().matches(SectionTokenizer.SECTION_TYPE_BASIC_REGEX)) {
            return ""
        }
        val text = splitRepeatCount(line).first
        val label = LEADING_LABEL.find(text)
        if (label != null && SectionType.fromName(label.groupValues[1]) is SectionType) {
            return ChordDetector.replacePlainChordsInline(text.substring(label.range.last + 1))
        }
        return text
    }

    companion object {
        private val LEADING_LABEL = "^\\s*(\\S+:|[0-9]+\\.)\\s+".toRegex()
    }
}
