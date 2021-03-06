package eu.karelhovorka.zpevnik.text


import eu.karelhovorka.zpevnik.text.section.SectionText
import eu.karelhovorka.zpevnik.util.I18N
import kotlin.jvm.JvmOverloads

class Section @JvmOverloads constructor(val content: SectionText, val type: ISectionType, val copyOf: Section? = null, val copyIndex: Int = 0, val index: Int = 0, val i18N: I18N) {

    val isCopy: Boolean
        get() = copyOf != null

    val css: String by lazy {
        val classes = mutableListOf(
                "section",
                "section-$index",
                "section-type-${type.css()}",
                "section-type-${type.css()}$index"
        )
        classes.joinToString(" ")
    }

    val shortName: String
        get() = type.getShortcut(index)

    val longName: String
        get() = type.getLongName(index)

    val localName: String
        get() {
            if (index == 0) {
                return i18N.translate(type.name, index.toString())
            }
            return i18N.translate(type.name, index.toString()) + " (${index + 1})"
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
        if (index == another.index) {
            return true
        }
        return false
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        other as Section

        if (content != other.content) return false
        if (type != other.type) return false
        if (copyOf != other.copyOf) return false
        if (copyIndex != other.copyIndex) return false
        if (index != other.index) return false

        return true
    }

    override fun hashCode(): Int {
        var result = content.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + (copyOf?.hashCode() ?: 0)
        result = 31 * result + copyIndex
        result = 31 * result + index
        return result
    }

    override fun toString(): String {
        return "Section(content=$content, type=$type, copyOf=$copyOf, copyIndex=$copyIndex, index=$index)"
    }

    companion object {

        fun makeCopyOf(previousSection: Section): Section {
            return Section(previousSection.content, previousSection.type, null, previousSection.copyIndex + 1, previousSection.index, i18N = previousSection.i18N)
        }
    }
}
