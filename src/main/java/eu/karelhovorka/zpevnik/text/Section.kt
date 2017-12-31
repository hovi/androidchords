package eu.karelhovorka.zpevnik.text


import eu.karelhovorka.zpevnik.text.section.SectionText
import java.lang.StringBuilder

class Section @JvmOverloads constructor(val content: SectionText, val type: ISectionType = SectionType.UNKNOWN, val copyOf: Section? = null, val typeIndex: Int = 0, val index: Int = 0) {

    val isCopy: Boolean
        get() = copyOf != null

    val css: String
        get() {
            val sb = StringBuilder()
            sb.append(" section")
            sb.append(" section-type-" + type.css())
            sb.append(" section-type-" + type.css() + index)
            return sb.toString()
        }

    val shortName: String
        get() = type.getShortcut(index)

    val longName: String
        get() = type.getLongName(index)

    override fun toString(): String {
        return "Section{" +
                "type='" + type + '\'' +
                ", typeIndex=" + typeIndex +
                '}'
    }



    fun sameContentAndType(another: Section?): Boolean {
        if (another == null) {
            return false
        }
        if (type != another.type) {
            return false
        }
        if (content == another.content) {
            return true
        }
        if (content.isEmpty()) {
            return true
        }
        return false
    }

    companion object {

        fun makeCopyOf(previousSection: Section): Section {
            return Section(previousSection.content, previousSection.type, previousSection, previousSection.typeIndex + 1, previousSection.index)
        }
    }
}
