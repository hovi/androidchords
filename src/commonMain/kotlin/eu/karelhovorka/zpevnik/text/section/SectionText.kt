package eu.karelhovorka.zpevnik.text.section

import eu.karelhovorka.zpevnik.util.ChordDetector
import eu.karelhovorka.zpevnik.util.Transposer
import mock.toPattern
import kotlin.math.min

data class SectionLine(val chordToText: Array<ChordPair>) {
    val hasChords: Boolean by lazy {
        chordToText.any { it.chord != null }
    }

    val hasText: Boolean by lazy {
        chordToText.any { it.text.isNotBlank() }
    }

    constructor(text: String) : this(arrayOf(ChordPair(null, text))) {
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
        return true
    }

    override fun hashCode(): Int {
        return chordToText.contentHashCode()
    }

}


data class SectionText(val lines: Array<SectionLine>, val originalText: String? = null) {

    fun isEmpty(): Boolean {
        return lines.isEmpty() || lines.size == 1 && lines.first().isEmpty()
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


fun parseSectionText(firstLine: String?, textBody: String): SectionText {
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
    var lineIndex = 0
    while (lineIndex < (lines.size)) {
        val line = lines[lineIndex]
        if (Transposer.lineContainsChordsAndText(line)) {
            result.add(parseSectionLineInline(line))
        } else if (lineIndex != lines.size - 1) {
            val nextLine = lines[lineIndex + 1]
            if (Transposer.lineContainsChordsOnly(line) && Transposer.lineContainsTextOnly(nextLine)) {
                if (nextLine.isBlank()) {
                    result.add(parseSectionLineInline(line))
                } else {
                    lineIndex++
                    result.add(parseSectionLineUpper(line, nextLine))
                }
            } else if (Transposer.lineContainsChordsOnly(line) && Transposer.lineContainsChordsOnly(nextLine)) {
                result.add(parseSectionLineInline(line))
            } else {
                result.add(SectionLine(line))
            }
        } else if (Transposer.lineContainsChordsOnly(line)) {
            result.add(parseSectionLineInline(line))
        } else {
            result.add(SectionLine(line))
        }
        lineIndex++
    }
    return SectionText(result.toTypedArray(), fullText)
}

fun parseSectionLineUpper(lines: String): SectionLine {
    val splitLines = lines.trimEnd().split("\n".toRegex())
    if (splitLines.size != 2) {
        throw IllegalArgumentException("lines have to be 2!")
    }
    return parseSectionLineUpper(splitLines.first(), splitLines.last())
}

fun parseSectionLineUpper(cLine: String, textLine: String): SectionLine {
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