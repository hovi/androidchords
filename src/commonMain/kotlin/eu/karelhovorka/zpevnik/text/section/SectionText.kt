package eu.karelhovorka.zpevnik.text.section

import eu.karelhovorka.zpevnik.util.ChordDetector
import eu.karelhovorka.zpevnik.util.Transposer
import mock.toPattern
import kotlin.math.min

data class SectionLine(val chordToText: Array<ChordPair>) {
    var hasChords: Boolean

    init {
        hasChords = _hasChords()
    }

    constructor(text: String) : this(arrayOf(ChordPair(null, text))) {
    }

    private fun _hasChords(): Boolean {
        for (pair in chordToText) {
            if (pair.chord != null) {
                return true
            }
        }
        return false
    }

    fun css(): String {
        val builder = StringBuilder()
        builder.append("song-line ")
        if (isEmpty()) {
            builder.append(" empty-line ")
        }
        if (hasChords) {
            builder.append("song-line-has-chords")
        } else {
            builder.append("song-line-no-chords")
        }
        return builder.toString()
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
        if (hasChords != other.hasChords) return false

        return true
    }

    override fun hashCode(): Int {
        var result = chordToText.contentHashCode()
        result = 31 * result + hasChords.hashCode()
        return result
    }

}


data class ChordPair(val chord: String?, val text: String) {

    fun css(): String {
        val builder = StringBuilder()
        builder.append("song-chord-pair ")
        if (chord == null) {
            builder.append(" empty-chord ")
        }
        if (isEmpty()) {
            builder.append(" empty-pair ")
        }
        return builder.toString()
    }

    fun htmlChord(): String {
        return chord ?: "&nbsp;"
    }

    fun withoutBrackets(): String {
        return htmlChord().replace("[", "").replace("]", "")
    }

    fun isEmpty(): Boolean {
        return chord == null && text.isEmpty()
    }
}


data class SectionText(val lines: Array<SectionLine>) {

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


fun parseSectionText(fullText: String): SectionText {
    val result = mutableListOf<SectionLine>()
    if (fullText.isBlank()) {
        return SectionText(emptyArray())
    }
    val lines = fullText.trimEnd().split("\n".toRegex())
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
    return SectionText(result.toTypedArray())
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
    val pattern =  patternStr.toPattern()
    val matcher = pattern.matcher(chordLine)
    val chordGroups = mutableListOf<ChordPair>()
    var chord: String? = null
    var index = 0
    var lastText: String
    var offset = 0
    val results = Regex(patternStr).findAll(chordLine)
/*    for (matchResult in results) {
        if (matchResult.range.start > 0) {
            if (chord == null) {
                chordGroups.add(ChordPair(null, textLine.substring(0, Math.min(matchResult.range.start, textLine.length))))
            } else {
                offset += 2
                if (index < 0 || index > textLine.length) {
                    lastText = ""
                } else {
                    lastText = textLine.substring(index, Math.min(matchResult.range.start - offset, textLine.length))
                }
                chordGroups.add(ChordPair(chord, lastText))
            }
        }
        chord = chordLine.substring(matchResult.range.start, matchResult.range.last + 1)
        index = matchResult.range.start - offset
    }*/
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
    var lastText: String = ""
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