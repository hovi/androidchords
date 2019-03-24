package eu.karelhovorka.zpevnik.formatter


import eu.karelhovorka.zpevnik.text.SectionTokenizer
import eu.karelhovorka.zpevnik.text.SongText
import kotlin.jvm.JvmField

abstract class SongFormatter {

    abstract fun formatHtml(songText: SongText): String

    protected abstract fun formatHtmlChords(content: String): String

    companion object {

        @JvmField
        val CHORD_URL_PROTOCOL = "chord://"

        fun isValidSectionFormat(content: String): Boolean {
            return SectionTokenizer.isValid(content)
        }
    }
}
