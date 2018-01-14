package eu.karelhovorka.zpevnik.text


import eu.karelhovorka.zpevnik.text.section.parseSectionText
import mock.*

class SectionTokenizer {
    private val SECTION_TYPE_BASIC_REGEX = "^$TYPE_REGEX$".toRegex()

    private fun splitRegex(): String {
        var sb = ""
        for (sectionType in SectionType.values()) {
            for (name in sectionType.names) {
                sb += ("|$name:")
            }

        }
        return "(^" + sb.substring(1) + "\n)"
    }

    fun getSectionsOrSingleSection(text: String): List<Section> {
        try {
            return getSections(text.trimEnd())
        } catch (e: Exception) {
            //e.printStackTrace()
            return listOf(Section(parseSectionText(text.trimEnd())))
        }
    }

    fun getSections(text: String): List<Section> {
        val lines = text.trimEnd().split("\n".toRegex()).dropLastWhile { it.isEmpty() }
        val sections = ArrayList<Section>()
        var sb: StringBuilder? = null
        val sectionTypeCount: MutableMap<ISectionType, Int> = mutableMapOf()
        var currentHeaderLine = ""
        for (line in lines) {
            if (line.trim().matches(SECTION_TYPE_BASIC_REGEX)) {
                if (currentHeaderLine != "") {
                    sections.add(parseSection(currentHeaderLine, sectionTypeCount, sb))
                }
                currentHeaderLine = line
                sb = StringBuilder()
            } else {
                if (sb == null) {
                    throw IllegalArgumentException("Text does not start with a section, line: '$line'")
                }
                sb.append(line + "\n")
            }
        }
        if (currentHeaderLine != "") {
            sections.add(parseSection(currentHeaderLine, sectionTypeCount, sb))
        }
        return mergeSections(sections)
    }

    private fun parseSection(currentHeaderLine: String, sectionTypeCount: MutableMap<ISectionType, Int>, sb: StringBuilder?): Section {
        val explicitIndex = currentHeaderLine.replace(SECTION_TYPE_BASIC_REGEX, "$1$2")
        val st = SectionType.fromName(currentHeaderLine)
        var index = sectionTypeCount.get(st) ?: 0
        if (explicitIndex.isNotBlank()) {
            try {
                index = explicitIndex.toInt() - 1
            } catch (e: NumberFormatException) {
                if (sb!!.toString().isBlank()) {
                    index = 0;
                }
            }
        } else if (sb!!.toString().isBlank()) {
            index = 0;
        }
        sectionTypeCount.put(st, (sectionTypeCount.get(st) ?: 0) + 1)
        return Section(parseSectionText(sb!!.toString()), st, null, 0, index)
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
        val TYPE_REGEX = "(?:[A-Z][A-Za-z]*([0-9]*):|([0-9]+)\\.)[\t ]*"

        val TYPE_REGEX_PATTERN = TYPE_REGEX.toPattern()

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
            if (count < MINIMUM_SECTION_COUNT) {
                throw IllegalStateException("not enough sections: " + count)
            }
        }
    }


}
