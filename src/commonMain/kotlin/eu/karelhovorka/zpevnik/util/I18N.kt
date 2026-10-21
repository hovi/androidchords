package eu.karelhovorka.zpevnik.util

import eu.karelhovorka.zpevnik.text.ISectionType

open class I18N {

    open fun translate(message: String, vararg args: String): String {
        return message
    }

    /** Header of a section: the translated type, numbered from the second section of a type on. */
    open fun sectionName(type: ISectionType, index: Int): String {
        val name = translate(type.name, index.toString())
        return if (index == 0) name else "$name (${index + 1})"
    }
}

/**
 * Section headers from the user's templates by section type ("verse" to "{index1}."), where {index0} and {index1}
 * are the number of the section within its type counted from 0 and from 1; other types are named by [base].
 */
class SectionNameTemplates(private val templates: Map<String, String>, private val base: I18N) : I18N() {

    override fun translate(message: String, vararg args: String): String = base.translate(message, *args)

    override fun sectionName(type: ISectionType, index: Int): String {
        val template = templates[type.css()] ?: return base.sectionName(type, index)
        return template.replace("{index0}", index.toString()).replace("{index1}", (index + 1).toString())
    }
}
