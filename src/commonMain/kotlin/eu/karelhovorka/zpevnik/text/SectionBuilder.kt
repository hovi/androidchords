package eu.karelhovorka.zpevnik.text

import eu.karelhovorka.zpevnik.text.SectionTokenizer.Companion.SECTION_TYPE_BASIC_REGEX
import eu.karelhovorka.zpevnik.text.section.parseSectionText
import eu.karelhovorka.zpevnik.util.I18N

fun SectionBuilder?.empty(): Boolean {
    return this == null || (sb.isEmpty() && currentHeaderLine?.isBlank() ?: true)
}

data class SectionBuilder(val sb: StringBuilder, val currentHeaderLine: String?) {

    fun trimFuckingNulls(text: String): String {
        if (text.startsWith("null")) {
            return text.substring(4)
        }
        if (text.endsWith("null")) {
            return text.substring(0, text.length - 4)
        }
        return text
    }

    private fun indexFromFirstLine(line: String?, sectionTypeCount: MutableMap<ISectionType, Int>): Int {
        val st = SectionType.fromName(line)
        return line?.let {
            val explicitIndex = trimFuckingNulls(line.trim().replace(SECTION_TYPE_BASIC_REGEX, "$1$2"))
            var index = sectionTypeCount[st] ?: 0
            if (explicitIndex.isNotBlank()) {
                try {
                    index = explicitIndex.toInt() - 1
                } catch (e: NumberFormatException) {
                    if (sb.toString().isBlank()) {
                        index = 0
                    }
                }
            } else if (sb.toString().isBlank()) {
                index = 0
            }
            index
        } ?: 0
    }

    fun parseSection(sectionTypeCount: MutableMap<ISectionType, Int>, i18n: I18N): Section {
        val st = SectionType.fromName(currentHeaderLine)
        val index = indexFromFirstLine(currentHeaderLine, sectionTypeCount)
        sectionTypeCount[st] = (sectionTypeCount[st] ?: 0) + 1
        return Section(parseSectionText(currentHeaderLine, sb.toString()), st, null, 0, index, i18N = i18n)
    }
}