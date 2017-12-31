package eu.karelhovorka.zpevnik.formatter


import eu.karelhovorka.zpevnik.text.SongText
import eu.karelhovorka.zpevnik.util.Transposer

abstract class MustacheTemplateSongFormatter protected constructor(protected val mainTemplate: String, protected val chordReplaceTemplate: String) : SongFormatter() {

    protected lateinit var songText: SongText

    override fun formatHtml(songText: SongText): String {
        this.songText = songText
        return formatHtmlChords(applyTemplate(mainTemplate, songText))
    }

    protected abstract fun applyTemplate(template: String, context: SongText): String

    public override fun formatHtmlChords(content: String): String {
        return content.replace(Transposer.CHORD_REGEX.toRegex(), applyTemplate(chordReplaceTemplate, songText))
    }

}
