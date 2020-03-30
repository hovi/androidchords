package eu.karelhovorka.zpevnik.text


import eu.karelhovorka.zpevnik.text.section.parseSectionText
import eu.karelhovorka.zpevnik.util.I18N
import mock.toPattern
import kotlin.jvm.JvmField

class SectionTokenizer(private val i18n: I18N = I18N()) {

    fun getSectionsOrSingleSection(text: String): List<Section> {
        try {
            return getSections(text.trimEnd())
        } catch (e: Exception) {
            //e.printStackTrace()
            return listOf(Section(parseSectionText("", text.trimEnd()), i18N = i18n))
        }
    }

    fun getSections(text: String): List<Section> {
        val lines = text.trimEnd().lines().dropLastWhile { it.isEmpty() }
        val sections = mutableListOf<Section>()
        val sectionTypeCount: MutableMap<ISectionType, Int> = mutableMapOf()
        var sectionBuilder: SectionBuilder? = null
        for (line in lines) {
            if (line.trim().matches(SECTION_TYPE_BASIC_REGEX)) {
                if (sectionBuilder != null && !sectionBuilder.empty()) {
                    sections.add(sectionBuilder.parseSection(sectionTypeCount, i18n))
                }
                sectionBuilder = SectionBuilder(StringBuilder(), line)
            } else {
                if (sectionBuilder == null) {
                    sectionBuilder = SectionBuilder(StringBuilder(), "")
                }
                sectionBuilder.sb.append(line + "\n")
            }
        }
        if (sectionBuilder != null) {
            sections.add(sectionBuilder.parseSection(sectionTypeCount, i18n))
        }
        return mergeSections(sections)
    }

    private fun mergeSections(originalSections: List<Section>): List<Section> {
        val mergedSections = mutableListOf<Section>()
        for (section in originalSections) {
            var sectionToAdd = section
            for (j in mergedSections.indices.reversed()) {
                val previousSection = mergedSections[j]
                if (section.sameContentAndType(previousSection)) {
                    sectionToAdd = Section.makeCopyOf(previousSection)
                    break
                }
            }
            mergedSections.add(sectionToAdd)
        }
        return mergedSections
    }

    companion object {

        @JvmField
        val TYPE_REGEX = "(?:[A-Z][A-Za-z]*\\.?([0-9]*)\\.?:|([0-9]+)\\.)[\t ]*"

        val SECTION_TYPE_BASIC_REGEX = "^$TYPE_REGEX$".toRegex()

        val VALID_TEXT_PATTERN = "^\\s*$TYPE_REGEX(?:\n|\r).*".toPattern()

        fun isValid(text: String): Boolean {
            try {
                validate(text)
                return true
            } catch (e: Exception) {
                //e.printStackTrace()
                return false
            }

        }

        fun validate(text: String) {
            if (!VALID_TEXT_PATTERN.matcher(text).find()) {
                throw IllegalStateException("Not starting with section header")
            }
            val m = "$TYPE_REGEX(?:\n|\r)".toPattern().matcher(text)
            var count = 0
            while (m.find()) {
                count += 1
            }
            if (count < 2) {
                throw IllegalStateException("not enough sections: " + count)
            }
        }
    }
}
