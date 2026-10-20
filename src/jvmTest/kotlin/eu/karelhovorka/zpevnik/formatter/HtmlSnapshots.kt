package eu.karelhovorka.zpevnik.formatter

import eu.karelhovorka.zpevnik.music.Interval
import eu.karelhovorka.zpevnik.text.SongDisplaySettings
import eu.karelhovorka.zpevnik.text.SongText
import eu.karelhovorka.zpevnik.util.I18N
import java.io.File

/**
 * Renders the test songs the way the app does. Uses only API that also exists in the old code,
 * so the same file can produce the "before" snapshots from an older checkout.
 */
object HtmlSnapshots {

    val layouts = listOf("legacy", "inline", "upper")

    val variants = mapOf(
        "default" to SongDisplaySettings(),
        // the app shows comments by default
        "comments" to SongDisplaySettings(showComments = true),
        "hide-chords" to SongDisplaySettings(isDisplayChords = false),
        "hide-text" to SongDisplaySettings(isDisplayText = false),
        "hide-identical" to SongDisplaySettings(isHideIdenticalSequences = true),
        "transposed" to SongDisplaySettings(interval = Interval.of(2))
    )

    fun root(): File = listOf(File("."), File("chords")).first { File(it, "src/jvmTest/resources/songs").isDirectory }

    fun songs(): List<File> = listOf("songs", "tokenizer")
        .flatMap { File(root(), "src/jvmTest/resources/$it").listFiles()!!.filter { f -> f.extension == "txt" } }
        .sortedBy { it.name }

    fun render(song: File, layout: String, settings: SongDisplaySettings): String {
        val templates = File(root(), "src/resources/templates/$layout")
        val formatter = JMustacheTemplateSongFormatter.fromTemplates(
            File(templates, "main.html").readText(),
            File(templates, "chords.html").readText()
        )
        return formatter.formatHtml(SongText.fromRawText(song.readText(), song.nameWithoutExtension, settings, I18N()))
    }

    /** Relative path of a snapshot, e.g. "repeats/upper-default.html". */
    fun path(song: File, layout: String, variant: String) = "${song.nameWithoutExtension}/$layout-$variant.html"

    fun renderAll(): Map<String, String> {
        val result = sortedMapOf<String, String>()
        for (song in songs()) {
            for (layout in layouts) {
                for ((variant, settings) in variants) {
                    result[path(song, layout, variant)] = render(song, layout, settings)
                }
            }
        }
        return result
    }

    fun writeAll(dir: File) {
        for ((path, html) in renderAll()) {
            val file = File(dir, path)
            file.parentFile.mkdirs()
            file.writeText(html)
        }
    }
}

class HtmlSnapshotWriter {
    @org.junit.Test
    fun write() {
        System.getenv("HTML_SNAPSHOT_DIR")?.let { HtmlSnapshots.writeAll(File(it)) }
    }
}
