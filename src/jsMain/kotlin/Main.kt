import eu.karelhovorka.zpevnik.music.Interval
import eu.karelhovorka.zpevnik.text.SongDisplaySettings
import eu.karelhovorka.zpevnik.text.SongText
import eu.karelhovorka.zpevnik.util.CsHardcoded
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.asList
import kotlin.browser.document
import kotlin.dom.hasClass


var tplUpper = """
<div id='content'>
    {{#sections}}
    <div class='{{css}}"'>
        <div class="header">{{localName}}</div>
        <div class="section-content">
            {{#content.lines}}
            <div class='song-text-line {{css}}'>
                {{#chordToText}}
                <div class="song-chord-pair {{css}}">
                    <div class="song-chord"><a class='chord' href='chord://{{withoutBrackets}}'>{{{withoutBrackets}}}</a></div>
                    <div class="song-text content" style="">{{text}}</div>
                </div>
                {{/chordToText}}
            </div>
            {{/content.lines}}
        </div>
    </div>
    {{/sections}}
    {{^sections}}<div class="single-content content">{{originalText}}</div>{{/sections}}
</div>
    """.trimIndent()

val tplInline = """
<div id='content'>
    {{#sections}}
    <div class='{{css}}"'>
        <div class="header">{{localName}}</div>
        <div class="section-content">
            {{#content.lines}}
            <div class='song-text-line {{css}}'>
                {{#chordToText}}
                <div class="song-chord-pair {{css}} content"><a class='chord' href='chord://{{withoutBrackets}}'>{{{withoutBrackets}}}</a>{{text}}</div>
                {{/chordToText}}
            </div>
            {{/content.lines}}
        </div>
    </div>
    {{/sections}}
    {{^sections}}<div class="single-content content">{{originalText}}</div>{{/sections}}
</div>
""".trimIndent()

val tplLegacy = """
<div id="content" class="content">{{filledSections}}</div>
""".trimIndent()


fun makeTemplates(): Map<String, String> {
    return document.querySelectorAll(".song-templates > div").asList().map {
        val element = it as HTMLElement
        element.className to element.innerHTML
    }.toMap()
}


fun parseTexts() {
    println("HELLO WORLD CHORDS 1.3")
    val step = (document.querySelector(".source-song-step")?.innerHTML)?.toIntOrNull() ?: 0
    val songDisplaySettings = SongDisplaySettings.DEFAULT.copy(
            interval = Interval.of(step)
    )
    val templateName = document.querySelector(".source-song-template")!!.innerHTML
    val chordReplacement: String? = if (templateName == "legacy") {
        " <a class='chord'>$1</a> "
    } else {
        null
    }
    parse(
            originalText = document.querySelector(".source-song-text")!!.innerHTML,
            templateName = templateName,
            title = document.querySelector(".source-song-title")!!.innerHTML,
            target = document.querySelector(".song-wrap")!!,
            songDisplaySettings = songDisplaySettings,
            chordReplacement = chordReplacement
    )
}

fun parse(originalText: String, target: Element, templateName: String, songDisplaySettings: SongDisplaySettings = SongDisplaySettings.DEFAULT, title: String = "", chordReplacement: String? = null) {
    val templates_ = mapOf(
            "inline" to tplInline,
            "upper" to tplUpper,
            "legacy" to tplLegacy,
            "" to tplLegacy
    )
    val templates = makeTemplates()
    val wrap = templateName == "" || templateName == "legacy"
    val ctx = SongText.fromRawText(originalText = originalText, title = title, songDisplaySettings = songDisplaySettings, chordReplacement = chordReplacement, i18n = CsHardcoded)
    val template = templates[templateName]!!
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

fun main(args: Array<String>) {
    disableEscaping()
    parseTexts()
}

