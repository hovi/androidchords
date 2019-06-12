package eu.karelhovorka.zpevnik.text


import eu.karelhovorka.zpevnik.util.ChordDetector
import eu.karelhovorka.zpevnik.util.I18N
import eu.karelhovorka.zpevnik.util.Preconditions.checkNotNull
import eu.karelhovorka.zpevnik.util.SongSettingsFormatter
import kotlin.js.JsName
import kotlin.jvm.JvmOverloads


@JsName("SongText")
class SongText(val originalText: String, val title: String, private val displaySettings: SongDisplaySettings = SongDisplaySettings.DEFAULT, val i18N: I18N = I18N()) {

    val sections: Array<Section>

    val css: String
        get() {
            val sb = StringBuilder()
            sb.append(" has-sections")
            if (sections.size == 1 && sections.first().type == SectionType.UNKNOWN) {
                sb.append(" section-single-unknown")
            }
            sb.append(" " + displaySettings.css)
            return sb.toString()
        }

    init {
        checkNotNull(originalText, "originalText")
        checkNotNull(title, "title")
        checkNotNull(displaySettings, "songDisplaySettings")
        val sectionTokenizer = SectionTokenizer(i18N)
        this.sections = sectionTokenizer.getSectionsOrSingleSection(originalText).toTypedArray()
    }

    fun toMap(): Map<String, Any> {
        val map = HashMap<String, Any>()
        map.put("title", title)
        map.put("originalText", originalText)
        return map
    }

    override fun toString(): String {
        return "SongText{" +
                "sections=" + sections +
                ", title='" + title + '\'' +
                ", displaySettings=" + displaySettings +
                '}'
    }

    companion object {

        @JvmOverloads
        fun fromRawText(originalText: String, title: String, songDisplaySettings: SongDisplaySettings, i18n: I18N = I18N(), wrapInHtml: Boolean = false): SongText {
            checkNotNull(originalText, "originalText")
            checkNotNull(title, "title")
            checkNotNull(songDisplaySettings, "songDisplaySettings")
            var modifiedText = SongSettingsFormatter.modifyTextBasedOnSettings(originalText, songDisplaySettings)
            if (wrapInHtml) {
               modifiedText = ChordDetector.wrapWithAnchors(modifiedText)
            }
            return SongText(modifiedText, title, songDisplaySettings, i18N = i18n)
        }
    }
}