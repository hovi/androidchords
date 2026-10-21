import eu.karelhovorka.zpevnik.music.Interval
import eu.karelhovorka.zpevnik.text.SongDisplaySettings
import eu.karelhovorka.zpevnik.text.SongMetadata
import eu.karelhovorka.zpevnik.text.SongText
import eu.karelhovorka.zpevnik.util.CsHardcoded
import eu.karelhovorka.zpevnik.util.Tone
import eu.karelhovorka.zpevnik.util.Transposer
import org.w3c.dom.*
import kotlinx.browser.document


fun makeTemplates(): Map<String, String> {
    return document.querySelectorAll(".song-templates > div").asList().map {
        val element = it as HTMLElement
        element.className to element.innerHTML
    }.toMap()
}


fun templateName(): String {
    return document.querySelector(".song-settings .template")!!.innerHTML
}

// chords are links in the app (chord://), the web has nothing to open;
// templates are read back from the page, where the browser may have changed ' to "
private val APP_CHORD_LINK = Regex(" href=[\"']chord://[^\"']*[\"']")

private val chordTemplates = mapOf(
        "legacy" to " <a class='chord'>$1</a> ",
        "inline" to "<a class='chord'>$1</a>",
        "upper" to "$1"
)

private fun obj(vararg properties: Pair<String, Any?>): dynamic {
    val result: dynamic = js("{}")
    for ((name, value) in properties) {
        result[name] = value
    }
    return result
}

/**
 * The values the templates use, as plain JS objects: mustache.js reads properties by name,
 * and Kotlin/JS renames the properties of Kotlin classes.
 */
fun templateContext(songText: SongText, songDisplaySettings: SongDisplaySettings): dynamic = obj(
        "title" to songText.title,
        "originalText" to songText.originalText,
        "filledSections" to songText.filledSections,
        "css" to songText.css,
        "displaySettings" to obj("css" to songDisplaySettings.css),
        "sections" to songText.sections.map { section ->
            obj(
                    "css" to section.css,
                    "localName" to section.localName,
                    "content" to obj("lines" to section.content.lines.map { line ->
                        obj(
                                "css" to line.css(),
                                "repeatCount" to line.repeatCount,
                                "repeatStart" to line.repeatStart,
                                "repeatEnd" to line.repeatEnd,
                                "chordToText" to line.chordToText.map { pair ->
                                    obj(
                                            "css" to pair.css(),
                                            "chord" to pair.chord,
                                            "htmlChord" to pair.htmlChord(),
                                            "withoutBrackets" to pair.withoutBrackets(),
                                            "text" to pair.text,
                                            "segments" to pair.segments.map { obj("css" to it.css, "text" to it.text) }.toTypedArray()
                                    )
                                }.toTypedArray()
                        )
                    }.toTypedArray())
            )
        }.toTypedArray(),
        "legacyRows" to songText.legacyRows.map { row ->
            obj(
                    "css" to row.css,
                    "text" to row.text,
                    "end" to row.end,
                    "parts" to row.parts.map { obj("css" to it.css, "text" to it.text) }.toTypedArray()
            )
        }.toTypedArray()
)

fun parseTexts() {
    val templateName = templateName()
    parse(
            originalText = document.querySelector(".source-song-text")?.innerHTML
                    ?: error("cannot find .source-song-text"),
            templateName = templateName,
            title = document.querySelector(".source-song-title")!!.innerHTML,
            target = document.querySelector(".song-wrap")!!,
            songDisplaySettings = songDisplaySettings()
    )
}

fun parse(
        originalText: String,
        target: Element,
        templateName: String,
        songDisplaySettings: SongDisplaySettings = SongDisplaySettings.DEFAULT,
        title: String = ""
) {
    val templates = makeTemplates()
    val songMetadata = songMetadata()
    val songText = SongText.fromRawText(originalText = originalText, title = title, songDisplaySettings = songDisplaySettings, i18n = CsHardcoded, metadata = songMetadata)
    val template = templates[templateName] ?: error("cannot find template $templateName")
    val chordTemplate = chordTemplates[templateName] ?: error("cannot find chord template $templateName")
    val html: String = renderMustache(template, templateContext(songText, songDisplaySettings))
    target.innerHTML = html.replace(APP_CHORD_LINK, "").replace(Transposer.CHORD_REGEX.toRegex(), chordTemplate)
}

fun disableEscaping() {
    js("Mustache.escape = function(text) {return text;};")
}

fun renderMustache(template: String, ctx: Any): dynamic {
    val t = template
    val c = ctx
    return js("Mustache.render(t, c)")
}

/** Only pages with a song to show have the song templates (the script is loaded on every admin page). */
fun hasSong(): Boolean {
    return document.querySelector(".song-templates") != null && document.querySelector(".source-song-text") != null
}

fun booleanOrFalse(selector: String, default: Boolean, root: Element = document.body!!): Boolean {
    return root.querySelector(selector)?.innerHTML?.toBoolean() ?: default
}

fun stringOrNull(selector: String, root: Element = document.body!!): String? {
    return root.querySelector(selector)?.innerHTML
}

/** The viewer's section headers by section type, written by the server as JSON in #song-section-names. */
fun sectionNames(): Map<String, String> {
    val json = document.getElementById("song-section-names")?.textContent ?: return emptyMap()
    val names: dynamic = JSON.parse(json)
    val types: Array<String> = js("Object.keys")(names)
    return types.associateWith { names[it] as String }
}

fun songDisplaySettings(): SongDisplaySettings {
    val step = stringOrNull(".source-song-step")?.toIntOrNull() ?: 0
    return SongDisplaySettings.DEFAULT.copy(
            interval = Interval.of(step),
            theme = stringOrNull(".song-settings .theme") ?: SongDisplaySettings.LIGHT_THEME,
            sectionNames = sectionNames(),
            isDisplayChords = booleanOrFalse(".song-settings .display-chords", true),
            isDoubleColumn = booleanOrFalse(".song-settings .double_column", false),
            isDisplayText = booleanOrFalse(".song-settings .show_text", true),
            isHideIdenticalSequences = booleanOrFalse(".song-settings .hide_identical_sequences", false),
            showComments = booleanOrFalse(".song-settings .show_comments", true),
            countryCategory = stringOrNull(".song-settings .country_category")?.let {
                try {
                    Tone.CountryCategory.valueOf(it)
                } catch (exception: Throwable) {
                    Tone.CountryCategory.EASTERN
                }
            } ?: Tone.CountryCategory.EASTERN,
            modificationAbbreviation = stringOrNull(".song-settings .modification_abbreviation")?.let {
                try {
                    Tone.ModificationAbbreviation.valueOf(it)
                } catch (exception: Throwable) {
                    Tone.ModificationAbbreviation.SHARP
                }
            }?: Tone.ModificationAbbreviation.SHARP

    )
}

fun songMetadata(): SongMetadata? {
    val metawrap = document.querySelector(".song-meta") ?: return null
    return SongMetadata(
            bpm = stringOrNull(selector = ".bpm", root = metawrap)?.toFloatOrNull(),
            capo = stringOrNull(selector = ".capo", root = metawrap)?.toIntOrNull(),
            durationSeconds = stringOrNull(selector = ".length", root = metawrap)?.toFloatOrNull()
            //key = Tone.fromString(stringOrNull(selector = ".length", root = metawrap) ?: "")
    )
}


fun watch() {
    // the layout and the settings are read on every change, the style editor changes them for its preview
    fun onValue(value: String, step: Int) {
        //val step = (document.querySelector(".source-song-step")?.innerHTML)?.toIntOrNull() ?: 0
        val songDisplaySettings = songDisplaySettings().copy(
                interval = Interval.of(step)
        )
        parse(
                originalText = value,
                templateName = templateName(),
                title = document.querySelector(".source-song-title")!!.innerHTML,
                target = document.querySelector(".song-wrap")!!,
                songDisplaySettings = songDisplaySettings
        )
    }

    fun step(): Int {
        return (document.querySelector("#id_step") as HTMLInputElement?)?.value?.toIntOrNull()
                ?: 0
    }

    (document.querySelector("#id_text") as HTMLTextAreaElement?)?.let { t ->
        document.querySelector("#id_step")?.addEventListener("change", {
            onValue(t.value, step())
        })
        t.addEventListener("change", {
            onValue(t.value, step())
        })
        t.addEventListener("keyup", {
            onValue(t.value, step())
        })
    }
}

fun main(args: Array<String>) {
    if (!hasSong()) {
        return
    }
    disableEscaping()
    parseTexts()
    watch()
}

