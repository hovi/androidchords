import eu.karelhovorka.zpevnik.music.Interval
import eu.karelhovorka.zpevnik.text.SongDisplaySettings
import eu.karelhovorka.zpevnik.text.SongMetadata
import eu.karelhovorka.zpevnik.text.SongText
import eu.karelhovorka.zpevnik.util.CsHardcoded
import eu.karelhovorka.zpevnik.util.Tone
import org.w3c.dom.*
import kotlin.browser.document
import kotlin.dom.hasClass


fun makeTemplates(): Map<String, String> {
    return document.querySelectorAll(".song-templates > div").asList().map {
        val element = it as HTMLElement
        element.className to element.innerHTML
    }.toMap()
}


fun templateName(): String {
    return document.querySelector(".song-settings .template")!!.innerHTML
}

fun chordReplacement(templateName: String? = null): String? {
    val tpl = templateName ?: templateName()
    return if (tpl == "legacy") {
        " <a class='chord'>$1</a> "
    } else {
        null
    }
}

fun parseTexts() {
    println("HELLO WORLD CHORDS 1.3.0")
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
    val chordReplacement = chordReplacement(templateName)
    val templates = makeTemplates()
    val songMetadata = songMetadata()
    val ctx = SongText.fromRawText(originalText = originalText, title = title, songDisplaySettings = songDisplaySettings, chordReplacement = chordReplacement, i18n = CsHardcoded, metadata = songMetadata)
    val template = templates[templateName] ?: error("cannot find template $templateName")
    target.innerHTML = renderMustache(template, ctx)
}

fun disableEscaping() {
    js("Mustache.escape = function(text) {return text;};")
}

fun renderMustache(template: String, ctx: Any): dynamic {
    val t = template
    val c = ctx
    return js("Mustache.render(t, c)")
}

fun doParsing(): Boolean {
    return document.body?.hasClass("parse-song") == true
}

fun booleanOrFalse(selector: String, default: Boolean, root: Element = document.body!!): Boolean {
    return root.querySelector(selector)?.innerHTML?.toBoolean() ?: default
}

fun stringOrNull(selector: String, root: Element = document.body!!): String? {
    return root.querySelector(selector)?.innerHTML
}

fun songDisplaySettings(): SongDisplaySettings {
    val step = stringOrNull(".source-song-step")?.toIntOrNull() ?: 0
    return SongDisplaySettings.DEFAULT.copy(
            interval = Interval.of(step),
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
    val templateName = templateName()

    fun onValue(value: String, step: Int) {
        //val step = (document.querySelector(".source-song-step")?.innerHTML)?.toIntOrNull() ?: 0
        val songDisplaySettings = songDisplaySettings().copy(
                interval = Interval.of(step)
        )
        parse(
                originalText = value,
                templateName = templateName,
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
    disableEscaping()
    parseTexts()
    watch()
}

