package eu.karelhovorka.zpevnik.text

import eu.karelhovorka.zpevnik.util.ChordDetector
import eu.karelhovorka.zpevnik.util.Transposer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * Compares the parsed structure of every song in src/jvmTest/resources/songs with its reviewed `.parsed` file.
 * Run with UPDATE_GOLDEN=1 to rewrite the files after an intended change, then review the diff.
 */
class SongStructureTest {

    private val dir = listOf("src/jvmTest/resources/songs", "chords/src/jvmTest/resources/songs").map { File(it) }.first { it.isDirectory }

    @Test
    fun parsedStructureMatchesGoldenFiles() {
        val songs = dir.listFiles()!!.filter { it.extension == "txt" }.sorted()
        assertTrue(songs.isNotEmpty())
        val update = System.getenv("UPDATE_GOLDEN") == "1"
        for (song in songs) {
            val golden = File(dir, song.nameWithoutExtension + ".parsed")
            val actual = describe(song.readText())
            if (update) {
                golden.writeText(actual)
            } else {
                assertTrue("missing ${golden.name}, run with UPDATE_GOLDEN=1", golden.exists())
                assertEquals(song.name, golden.readText(), actual)
            }
        }
    }

    private fun describe(text: String): String {
        val prepared = Transposer.merge(ChordDetector.replacePlainChords(text))
        val sb = StringBuilder()
        for (section in SectionTokenizer().getSections(prepared)) {
            sb.append("${section.type.name} index=${section.index} copy=${section.copyIndex}\n")
            for (line in section.content.lines) {
                sb.append("  ")
                sb.append(line.chordToText.joinToString(" | ") { "${it.chord ?: "-"} '${it.text}'" })
                line.repeatCount?.let { sb.append("  ×$it") }
                sb.append("\n")
            }
        }
        return sb.toString()
    }
}
