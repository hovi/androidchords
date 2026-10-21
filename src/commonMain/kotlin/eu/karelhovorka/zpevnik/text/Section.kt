package eu.karelhovorka.zpevnik.text


import eu.karelhovorka.zpevnik.text.section.SectionLine
import eu.karelhovorka.zpevnik.text.section.SectionText
import eu.karelhovorka.zpevnik.util.I18N
import kotlin.jvm.JvmOverloads

/**
 * [isRepeatOnly]: a copy written as a bare header ("R:") in the source, its [content] comes from the earlier section.
 * A repeat with notes ("R:" + "# softly") has the notes in front of the repeated [content]; [ownContent] is
 * what was written in this section (the legacy layout shows the song as written).
 * [isRepeatOnly], [ownContent] and [source] describe the source text only and are not part of equality.
 */
class Section @JvmOverloads constructor(
    val content: SectionText,
    val type: ISectionType,
    val copyIndex: Int = 0,
    val index: Int = 0,
    val i18N: I18N,
    val isRepeatOnly: Boolean = false,
    val source: SectionSource = SectionSource(),
    val ownContent: SectionText = content
) {

    /** Comments written in a repeat without music ("R:" + "# softly"), shown before the repeated content. */
    val notes: List<SectionLine>
        get() = if (isRepeatOnly) ownContent.lines.filter { it.isComment } else emptyList()

    val isCopy: Boolean
        get() = copyIndex > 0

    val css: String by lazy {
        val classes = mutableListOf(
                "section",
                "section-$index",
                "section-type-${type.css()}",
                "section-type-${type.css()}$index"
        )
        if (isCopy) {
            classes.add("section-copy")
        }
        if (isRepeatOnly) {
            classes.add("section-repeat-only")
        }
        classes.joinToString(" ")
    }

    val shortName: String
        get() = type.getShortcut(index)

    val longName: String
        get() = type.getLongName(index)

    val localName: String
        get() = i18N.sectionName(type, index)


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
        // a bare header (e.g. "R:"), also with only notes, repeats the earlier section with the same index
        return !content.hasMusic() && index == another.index
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        other as Section

        if (content != other.content) return false
        if (type != other.type) return false
        if (copyIndex != other.copyIndex) return false
        if (index != other.index) return false

        return true
    }

    override fun hashCode(): Int {
        var result = content.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + copyIndex
        result = 31 * result + index
        return result
    }

    override fun toString(): String {
        return "Section(content=$content, type=$type, copyIndex=$copyIndex, index=$index)"
    }

    companion object {

        /**
         * [section] repeats [previousSection]; a header without music takes its content after its own notes,
         * written out content is kept.
         */
        fun makeCopyOf(previousSection: Section, section: Section): Section {
            val isRepeatOnly = !section.content.hasMusic()
            val notes = section.content.lines.filter { it.isComment }.toTypedArray()
            // a repeat of a repeat takes the music, not the notes of the one it repeats
            val music = previousSection.content.lines.drop(if (previousSection.isRepeatOnly) previousSection.notes.size else 0)
            return Section(
                if (isRepeatOnly) SectionText(notes + music, previousSection.content.originalText) else section.content,
                previousSection.type,
                previousSection.copyIndex + 1,
                previousSection.index,
                i18N = previousSection.i18N,
                isRepeatOnly = isRepeatOnly,
                source = section.source,
                ownContent = section.content
            )
        }
    }
}
