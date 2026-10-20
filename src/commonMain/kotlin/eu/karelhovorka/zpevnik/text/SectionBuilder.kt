package eu.karelhovorka.zpevnik.text

import eu.karelhovorka.zpevnik.text.SectionTokenizer.Companion.SECTION_TYPE_BASIC_REGEX
import eu.karelhovorka.zpevnik.text.section.parseSectionText
import eu.karelhovorka.zpevnik.util.ChordDetector
import eu.karelhovorka.zpevnik.util.I18N
import eu.karelhovorka.zpevnik.util.Transposer
import kotlin.math.min

fun SectionBuilder?.empty(): Boolean {
    return this == null || (sb.isEmpty() && currentHeaderLine?.isBlank() ?: true)
}

/**
 * [hangingIndent] is the width of a header written on the same line as text ("R: text"),
 * following lines indented up to that width are only aligned under the text.
 * [unindentedFirstLine] marks a first line that was taken from behind the header as is.
 * [headerLines] lines at the start belong to the header line itself and are never moved to another section.
 */
data class SectionBuilder(
    val sb: StringBuilder,
    val currentHeaderLine: String?,
    val hangingIndent: Int = 0,
    val unindentedFirstLine: Boolean = false,
    val headerLines: Int = 0
) {

    fun trimFuckingNulls(text: String): String {
        if (text.startsWith("null")) {
            return text.substring(4)
        }
        if (text.endsWith("null")) {
            return text.substring(0, text.length - 4)
        }
        return text
    }

    fun removeTrailingChordLine(): String? {
        val lines = sb.toString().trimEnd('\n').split("\n")
        val last = lines.last()
        if (lines.size <= headerLines || !Transposer.lineContainsChordsOnly(ChordDetector.replacePlainChordsInline(last))) {
            return null
        }
        sb.clear()
        lines.dropLast(1).forEach { sb.append(it + "\n") }
        return last
    }

    private fun removeHangingIndent(body: String): String {
        if (hangingIndent == 0) {
            return body
        }
        val lines = body.split("\n")
        val firstIndented = if (unindentedFirstLine) 1 else 0
        val indented = lines.indices.filter { it >= firstIndented && lines[it].isNotBlank() }
        if (indented.isEmpty()) {
            return body
        }
        val indent = min(hangingIndent, indented.minOf { lines[it].takeWhile { c -> c == ' ' }.length })
        return lines.mapIndexed { i, line -> if (i in indented) line.substring(indent) else line }.joinToString("\n")
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
        return Section(parseSectionText(currentHeaderLine, removeHangingIndent(sb.toString())), st, 0, index, i18N = i18n)
    }
}