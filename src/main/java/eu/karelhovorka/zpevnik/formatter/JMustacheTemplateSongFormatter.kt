package eu.karelhovorka.zpevnik.formatter


import com.samskivert.mustache.Mustache

import eu.karelhovorka.zpevnik.text.SongText

class JMustacheTemplateSongFormatter protected constructor(mainTemplate: String, chordReplaceTemplate: String) : MustacheTemplateSongFormatter(mainTemplate, chordReplaceTemplate) {

    override fun applyTemplate(template: String, context: SongText): String {
        //TODO: pre-compile and reuse template
        return Mustache.compiler().escapeHTML(false).compile(template).execute(context)
    }

    companion object {

        fun fromTemplates(mainTemplate: String, chordReplaceTemplate: String): MustacheTemplateSongFormatter {
            return JMustacheTemplateSongFormatter(mainTemplate, chordReplaceTemplate)
        }
    }
}
