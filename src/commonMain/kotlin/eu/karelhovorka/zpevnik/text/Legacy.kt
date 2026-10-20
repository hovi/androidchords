package eu.karelhovorka.zpevnik.text

import eu.karelhovorka.zpevnik.text.section.TextSegment
import eu.karelhovorka.zpevnik.util.Transposer

/**
 * How a section was written. [header] is the header line as written or, for a header written in front of
 * text, the part in front of the text ("R: ") with [headerTextLine] the index of that text line in the body.
 * [headerTextPadded]: that text line was indented to the column after the header to keep chords aligned.
 */
data class SectionSource(
    val header: String? = null,
    val headerTextLine: Int = -1,
    val headerTextPadded: Boolean = false,
    val trailingLines: List<String> = emptyList()
)

/**
 * One line of the song as written (legacy layout), split into chord and text parts.
 * Not "chord": the chord link inside gets that class, styles like font-size would apply twice.
 */
class LegacyRow(val css: String, val text: String, val end: String = "\n") {

    val parts: List<TextSegment> by lazy {
        val parts = mutableListOf<TextSegment>()
        var index = 0
        for (match in CHORD.findAll(text)) {
            if (match.range.first > index) {
                parts.add(TextSegment("legacy-text", text.substring(index, match.range.first)))
            }
            parts.add(TextSegment("legacy-chord", match.value))
            index = match.range.last + 1
        }
        if (index < text.length) {
            parts.add(TextSegment("legacy-text", text.substring(index)))
        }
        parts
    }

    fun withEnd(end: String) = LegacyRow(css, text, end)

    override fun toString() = text

    companion object {
        private val CHORD = Transposer.CHORD_REGEX.toRegex()
    }
}

/** The section as written: header, lines rebuilt from chord pairs, trailing blank lines. */
fun Section.legacyRows(): List<LegacyRow> {
    // rows with the index of the body line they come from
    val rows = mutableListOf<Pair<Int, LegacyRow>>()
    if (!isRepeatOnly) {
        content.lines.forEachIndexed { i, line ->
            val lineSource = line.source
            val css = line.css()
            if (lineSource?.chordLine != null) {
                val text = line.chordToText.joinToString("") { it.text }
                rows.add(lineSource.line to LegacyRow("$css chord-row", lineSource.chordLine))
                rows.add(lineSource.line + 1 to LegacyRow("$css text-row", lineSource.indent + text + lineSource.repeatSuffix))
            } else {
                val text = line.chordToText.joinToString("") { (it.chord ?: "") + it.text }
                rows.add((lineSource?.line ?: i) to LegacyRow(css, (lineSource?.indent ?: "") + text + (lineSource?.repeatSuffix ?: "")))
            }
        }
    }
    val header = source.header
    val headerText = rows.indexOfFirst { it.first == source.headerTextLine }
    val result = rows.map { it.second }.toMutableList()
    if (header != null && headerText >= 0) {
        val row = result[headerText]
        val text = if (source.headerTextPadded) row.text.drop(header.length) else row.text
        result[headerText] = LegacyRow("${row.css} section-header", header + text)
    } else if (header != null) {
        result.add(0, LegacyRow("section-header", header))
    }
    source.trailingLines.forEach { result.add(LegacyRow("empty-line", it)) }
    return result
}
