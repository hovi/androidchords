import eu.karelhovorka.zpevnik.music.Interval
import eu.karelhovorka.zpevnik.text.SongDisplaySettings
import eu.karelhovorka.zpevnik.text.SongText
import eu.karelhovorka.zpevnik.util.ChordDetector
import kotlin.browser.document


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
<div id="content" class="content">{{originalText}}</div>
""".trimIndent()


fun main(args: Array<String>) {
    val templates = mapOf(
            "inline" to tplInline,
            "upper" to tplUpper,
            "legacy" to tplLegacy,
            "" to tplLegacy
    )


    println("HELLO WORLD CHORDS 1.2")
    val originalText = document.querySelector(".source-song-text")!!.innerHTML
    val title = document.querySelector(".source-song-title")!!.innerHTML
    val step = (document.querySelector(".source-song-step")!!.innerHTML).toInt()
    val songDisplaySettings = SongDisplaySettings.DEFAULT.copy(
            interval = Interval.of(step)
    )

    js("Mustache.escape = function(text) {return text;};")
    val templateName = document.querySelector(".source-song-template")!!.innerHTML
    val template = templates[templateName]
    println("-$templateName-")
    val wrap = templateName == "" || templateName == "legacy"
    val ctx = SongText.fromRawText(originalText = originalText, title = title, songDisplaySettings = songDisplaySettings, wrapInHtml = wrap)


    val output = js("Mustache.render(template, ctx)")
    val target = document.querySelector(".song-wrap")!!
    target.innerHTML = output
}

