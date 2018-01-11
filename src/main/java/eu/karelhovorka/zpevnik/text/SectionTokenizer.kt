package eu.karelhovorka.zpevnik.text


import eu.karelhovorka.zpevnik.text.section.parseSectionText
import java.util.*
import java.util.regex.Pattern

class SectionTokenizer {
    private val SECTION_TYPE_BASIC_REGEX = "^$TYPE_REGEX$".toRegex()

    private fun splitRegex(): String {
        val sb = StringBuilder()
        for (sectionType in SectionType.values()) {
            for (name in sectionType.names) {
                sb.append("|$name:")
            }

        }
        return "(^" + sb.substring(1) + "\n)"
    }

    fun getSectionsOrSingleSection(text: String) : List<Section> {
        try {
            return getSections(text.trimEnd())
        } catch (e: Exception) {
            e.printStackTrace()
            return listOf(Section(parseSectionText(text.trimEnd())))
        }
    }

    fun getSections(text: String): List<Section> {
        val lines = text.trimEnd().split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val sections = ArrayList<Section>()
        var st: ISectionType? = null
        var sb: StringBuilder? = null
        val sectionTypeCount: MutableMap<ISectionType, Int> = mutableMapOf()
        for (line in lines) {
            if (line.trim().matches(SECTION_TYPE_BASIC_REGEX)) {
                if (st != null) {
                    val index = sectionTypeCount.get(st) ?: 0
                    val section = Section(parseSectionText(sb!!.toString()), st, null, 0, index)
                    sectionTypeCount.put(st, index + 1)
                    sections.add(section)
                }
                st = SectionType.fromName(line)
                sb = StringBuilder()
            } else {
                if (sb == null) {
                    throw IllegalArgumentException("Text does not start with a section, line: '$line'")
                }
                sb.append(line + "\n")
            }
        }
        if (st != null) {
            val index = sectionTypeCount.get(st) ?: 0
            sections.add(Section(parseSectionText(sb!!.toString().trimEnd()), st, null, 0, index))
            sectionTypeCount.put(st, index + 1)
        }
        return mergeSections(sections)
    }

    fun mergeSections(originalSections: List<Section>): List<Section> {
        val mergedSections = ArrayList<Section>()
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

        private val MINIMUM_SECTION_COUNT = 2

        @JvmField
        val TYPE_REGEX = "(?:[A-Z][A-Za-z]*([0-9]*):|([0-9]+)(?:\\.|:))[\t ]*"

        val TYPE_REGEX_PATTERN = Pattern.compile(TYPE_REGEX)

        val VALID_TEXT_PATTERN = Pattern.compile("^\\s*$TYPE_REGEX(?:\n|\r).*")

        fun isValid(text: String): Boolean {
            try {
                validate(text)
                return true
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }

        }

        fun validate(text: String) {
            if (!VALID_TEXT_PATTERN.matcher(text).find()) {
                throw IllegalStateException("Not starting with section header")
            }
            val m = Pattern.compile("$TYPE_REGEX(?:\n|\r)").matcher(text)
            var count = 0
            while (m.find()) {
                count += 1
            }
            if (count < MINIMUM_SECTION_COUNT) {
                throw IllegalStateException("not enough sections: " + count)
            }
        }
    }


}
