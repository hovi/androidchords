package eu.karelhovorka.zpevnik.text.section

import eu.karelhovorka.zpevnik.util.ChordDetector
import eu.karelhovorka.zpevnik.util.Transposer
import eu.karelhovorka.zpevnik.util.expandTabs
import mock.toPattern
import kotlin.math.min

/**
 * How a [SectionLine] was written, so that it can be shown exactly as written (legacy layout).
 * [line] is the index of its first source line in the section body, [indent] the leading spaces removed
 * from the (text) line before parsing, [chordLine] the whole chord line as written when chords were
 * above the text, [repeatSuffix] the repeat count of the (text) line as written (e.g. " (2x)").
 */
data class LineSource(val line: Int, val indent: String, val chordLine: String?, val repeatSuffix: String)

/**
 * [repeatStart]/[repeatEnd]: repeat bars written on the chord line above the text ("/: [C] [G] :/"),
 * bars in the text itself are in [ChordPair.segments].
 * [repeatedChords]: the same chord sequence (at least [Transposer.MINIMUM_SEQUENCE_COUNT] chords) was on an
 * earlier line of the song, the chords can be hidden.
 * [commentLine]: the source line was a whole line comment, also when it has chords in it.
 * [source], [repeatedChords] and [commentLine] describe the source and the rest of the song and are not part of equality.
 */
data class SectionLine(
    val chordToText: Array<ChordPair>,
    val repeatCount: Int? = null,
    val source: LineSource? = null,
    val repeatStart: Boolean = false,
    val repeatEnd: Boolean = false,
    val repeatedChords: Boolean = false,
    val commentLine: Boolean = false
) {
    val hasChords: Boolean by lazy {
        chordToText.any { it.chord != null }
    }

    val hasText: Boolean by lazy {
        chordToText.any { it.text.isNotBlank() }
    }

    constructor(text: String) : this(arrayOf(ChordPair(null, text))) {
    }

    val chords: List<String>
        get() = chordToText.mapNotNull { it.chord }

    /** A whole line comment ("# ..."), marked by [eu.karelhovorka.zpevnik.util.Comments.markComments]. */
    val isComment: Boolean
        get() = commentLine || chordToText.size == 1 && chordToText[0].chord == null && isComment(chordToText[0].text)

    /** A repeat count or bars belong to the line: hiding its chords must not hide them. */
    val hasRepeat: Boolean
        get() = repeatCount != null || repeatStart || repeatEnd || chordToText.any { pair ->
            pair.segments.any { it == TextSegment.REPEAT_START || it == TextSegment.REPEAT_END }
        }

    fun css(): String {
        val classes = mutableListOf("song-line")
        if (isEmpty()) {
            classes.add("empty-line")
        }
        if (hasText) {
            classes.add("song-line-has-text")
        } else {
            classes.add("song-line-no-text")
        }
        if (hasChords) {
            classes.add("song-line-has-chords")
        } else {
            classes.add("song-line-no-chords")
        }
        if (isComment) {
            classes.add("comment-line")
        }
        if (repeatedChords) {
            classes.add("repeated-chords")
        }
        if (hasRepeat) {
            classes.add("has-repeat")
        }
        return classes.joinToString(" ")
    }

    val simple: Boolean
        get() {
            return chordToText.isEmpty() || chordToText.size == 1 && chordToText.first().chord == null
        }

    fun isEmpty(): Boolean {
        return chordToText.isEmpty() || chordToText.size == 1 && chordToText.first().isEmpty()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as SectionLine

        if (!chordToText.contentEquals(other.chordToText)) return false
        return repeatCount == other.repeatCount && repeatStart == other.repeatStart && repeatEnd == other.repeatEnd
    }

    override fun hashCode(): Int {
        return 31 * (31 * chordToText.contentHashCode() + (repeatCount ?: 0)) + repeatStart.hashCode() * 2 + repeatEnd.hashCode()
    }

}


data class SectionText(val lines: Array<SectionLine>, val originalText: String? = null) {

    fun isEmpty(): Boolean {
        return lines.isEmpty() || lines.size == 1 && lines.first().isEmpty()
    }

    /** Chords or lyrics, not only blank lines and comments: a section of only notes repeats an earlier one. */
    fun hasMusic(): Boolean = lines.any { line ->
        !line.isComment && line.chordToText.any { it.chord != null || it.text.isNotBlank() }
    }

    fun text(): String {
        val sb = StringBuilder()
        for (line in lines) {
            for (cp in line.chordToText) {
                sb.append(cp.text)
            }
            sb.append("\n")
        }
        return sb.toString().trimEnd()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as SectionText

        if (!lines.contentEquals(other.lines)) return false

        return true
    }

    override fun hashCode(): Int {
        return lines.contentHashCode()
    }

}


/**
 * [removedIndents] are the leading spaces already removed from each line of [textBody].
 * [seenChordSequences] are the chord sequences of the song so far, lines repeating one get [SectionLine.repeatedChords].
 */
/** Whether a section body (one line per source line) has chords or lyrics, see [SectionText.hasMusic]. */
fun hasMusic(body: String): Boolean = body.lines().any { it.isNotBlank() && !isComment(it) }

fun parseSectionText(
    firstLine: String?,
    textBody: String,
    removedIndents: List<Int> = emptyList(),
    seenChordSequences: MutableSet<List<String>> = mutableSetOf()
): SectionText {
    val fullText = if (firstLine == null) {
        textBody
    } else {
        "$firstLine\n$textBody"
    }
    val result = mutableListOf<SectionLine>()
    if (textBody.isBlank()) {
        return SectionText(emptyArray(), fullText)
    }
    val lines = textBody.trimEnd().split("\n".toRegex())
    val chordsOnly = { line: String -> Transposer.lineContainsChordsOnly(blankRepeatMarks(line)) }
    val removedIndent = { index: Int -> " ".repeat(removedIndents.getOrElse(index) { 0 }) }
    var lineIndex = 0
    while (lineIndex < (lines.size)) {
        val (line, repeatCount, repeatSuffix) = splitRepeatCount(lines[lineIndex])
        val source = LineSource(lineIndex, removedIndent(lineIndex), null, repeatSuffix)
        val nextLine = lines.getOrNull(lineIndex + 1)?.let { splitRepeatCount(it) }
        if (chordsOnly(line) && nextLine != null && nextLine.first.isNotBlank() && !isComment(nextLine.first) && Transposer.lineContainsTextOnly(nextLine.first)) {
            // same indentation as parseSectionLineUpper removes from both lines
            val pairIndent = min(
                expandTabs(blankRepeatMarks(line)).takeWhile { it == ' ' }.length,
                expandTabs(nextLine.first).takeWhile { it == ' ' }.length
            )
            val upperSource = LineSource(lineIndex, removedIndent(lineIndex + 1) + " ".repeat(pairIndent), source.indent + expandTabs(lines[lineIndex]), nextLine.third)
            // bars on the chord line belong to the line, unless the lyrics have the same bars
            result.add(
                parseSectionLineUpper(blankRepeatMarks(line), nextLine.first).copy(
                    repeatCount = repeatCount ?: nextLine.second,
                    source = upperSource,
                    repeatStart = hasRepeatStart(line) && !hasRepeatStart(nextLine.first),
                    repeatEnd = hasRepeatEnd(line) && !hasRepeatEnd(nextLine.first)
                )
            )
            lineIndex++
        } else if (chordsOnly(line) || Transposer.lineContainsChordsAndText(line)) {
            result.add(parseSectionLineInline(line).copy(repeatCount = repeatCount, source = source))
        } else {
            result.add(SectionLine(arrayOf(ChordPair(null, line)), repeatCount, source))
        }
        lineIndex++
    }
    val commentLines = lines.indices.filter { isComment(splitRepeatCount(lines[it]).first) }.toSet()
    val flagged = result.map { line -> if (line.source?.line in commentLines) line.copy(commentLine = true) else line }.map { line ->
        val chords = line.chords
        when {
            chords.size < Transposer.MINIMUM_SEQUENCE_COUNT -> line
            seenChordSequences.add(chords) -> line
            else -> line.copy(repeatedChords = true)
        }
    }
    return SectionText(flagged.toTypedArray(), fullText)
}

// a comment is a note, not lyrics for the chords above it (marked or still raw, see Comments)
internal fun isComment(line: String) = line.trim().let { it.startsWith("#") || it.startsWith("<comment>") && it.endsWith("</comment>") }

fun parseSectionLineUpper(lines: String): SectionLine {
    val splitLines = lines.trimEnd().split("\n".toRegex())
    if (splitLines.size != 2) {
        throw IllegalArgumentException("lines have to be 2!")
    }
    return parseSectionLineUpper(splitLines.first(), splitLines.last())
}

fun parseSectionLineUpper(rawChordLine: String, rawTextLine: String): SectionLine {
    val indentedChordLine = expandTabs(rawChordLine)
    val indentedTextLine = expandTabs(rawTextLine)
    val indent = min(indentedChordLine.takeWhile { it == ' ' }.length, indentedTextLine.takeWhile { it == ' ' }.length)
    val cLine = indentedChordLine.substring(indent)
    val textLine = indentedTextLine.substring(indent)
    var chordLine = Transposer.mergeLine(cLine)
    chordLine = ChordDetector.replacePlainChordsInline(chordLine)

    val patternStr = (Transposer.CHORD_REGEX)
    val pattern = patternStr.toPattern()
    val matcher = pattern.matcher(chordLine)
    val chordGroups = mutableListOf<ChordPair>()
    var chord: String? = null
    var index = 0
    var lastText: String
    var offset = 0
    while (matcher.find()) {
        if (matcher.start() > 0) {
            if (chord == null) {
                chordGroups.add(ChordPair(null, textLine.substring(0, min(matcher.start(), textLine.length))))
            } else {
                offset += 2
                if (index < 0 || index > textLine.length) {
                    lastText = ""
                } else {
                    lastText = textLine.substring(index, min(matcher.start() - offset, textLine.length))
                }
                chordGroups.add(ChordPair(chord, lastText))
            }
        }
        chord = chordLine.substring(matcher.start(), matcher.end())
        index = matcher.start() - offset
    }
    lastText = textLine.substring(min(index, textLine.length))
    chordGroups.add(ChordPair(chord, lastText))
    return SectionLine(chordGroups.toTypedArray())
}

fun parseSectionLineInline(line: String): SectionLine {
    var chordLine = Transposer.mergeLine(line)
    chordLine = ChordDetector.replacePlainChordsInline(chordLine)
    val patternStr = Transposer.CHORD_REGEX
    val pattern = patternStr.toPattern()
    val matcher = pattern.matcher(chordLine)
    val chordGroups = mutableListOf<ChordPair>()
    var lastText: String
    var chord: String? = null
    var index = 0
    while (matcher.find()) {
        lastText = chordLine.substring(index, matcher.start())
        if (matcher.start() > 0) {
            chordGroups.add(ChordPair(chord, lastText))
        }
        chord = chordLine.substring(matcher.start(), matcher.end())
        index = matcher.end()
    }
    lastText = chordLine.substring(index)
    chordGroups.add(ChordPair(chord, lastText))
    return SectionLine(chordGroups.toTypedArray())
}