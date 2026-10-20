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
 * [headerPrefix] is the header as written in front of the text ("R: "), for a header written on the same line as text.
 */
data class SectionBuilder(
    val sb: StringBuilder,
    val currentHeaderLine: String?,
    val hangingIndent: Int = 0,
    val unindentedFirstLine: Boolean = false,
    val headerLines: Int = 0,
    val headerPrefix: String? = null
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
        // only the line right above the header: every body line ends with a line break
        val lines = sb.toString().removeSuffix("\n").split("\n")
        val last = lines.last()
        if (sb.isEmpty() || lines.size <= headerLines || !Transposer.lineContainsChordsOnly(ChordDetector.replacePlainChordsInline(last))) {
            return null
        }
        sb.clear()
        lines.dropLast(1).forEach { sb.append(it + "\n") }
        return last
    }

    /** The body without hanging indentation, and the number of spaces removed from each line. */
    private fun removeHangingIndent(body: String): Pair<String, List<Int>> {
        val lines = body.split("\n")
        val firstIndented = if (unindentedFirstLine) 1 else 0
        val indented = lines.indices.filter { hangingIndent > 0 && it >= firstIndented && lines[it].isNotBlank() }
        if (indented.isEmpty()) {
            return Pair(body, emptyList())
        }
        val indent = min(hangingIndent, indented.minOf { lines[it].takeWhile { c -> c == ' ' }.length })
        return Pair(
            lines.mapIndexed { i, line -> if (i in indented) line.substring(indent) else line }.joinToString("\n"),
            lines.indices.map { if (it in indented) indent else 0 }
        )
    }

    private fun source(body: String): SectionSource {
        // every line in the body ends with a line break
        val trailingLines = if (body.isEmpty()) emptyList() else body.removeSuffix("\n").split("\n").takeLastWhile { it.isBlank() }
        return if (headerPrefix != null) {
            SectionSource(headerPrefix, headerLines - 1, !unindentedFirstLine, trailingLines)
        } else {
            SectionSource(currentHeaderLine?.takeIf { it.isNotEmpty() }, -1, false, trailingLines)
        }
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
        val (body, removedIndents) = removeHangingIndent(sb.toString())
        return Section(parseSectionText(currentHeaderLine, body, removedIndents), st, 0, index, i18N = i18n, source = source(sb.toString()))
    }
}