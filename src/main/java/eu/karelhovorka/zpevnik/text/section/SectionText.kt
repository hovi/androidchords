package eu.karelhovorka.zpevnik.text.section

import eu.karelhovorka.zpevnik.util.ChordDetector
import eu.karelhovorka.zpevnik.util.Transposer
import java.util.regex.Pattern


data class SectionLine(val chordToText: List<ChordPair>) {
    var hasChords: Boolean

    init {
        hasChords = _hasChords()
    }

    constructor(text: String) : this(listOf(ChordPair(null, text))) {
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

    fun isEmpty(): Boolean {
        return chord == null && text.isEmpty()
    }
}


data class SectionText(val lines: List<SectionLine>) {

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

}


fun parseSectionText(fullText: String): SectionText {
    val result = mutableListOf<SectionLine>()
    val lines = fullText.trimEnd().split("\n".toRegex())
    var lineIndex = 0
    while (lineIndex < (lines.size)) {
        val line = lines[lineIndex]
        if (Transposer.lineContainsChordsAndText(line)) {
            result.add(parseSectionLineInline(line))
        } else if (lineIndex != lines.size - 1) {
            val nextLine = lines[lineIndex + 1]
            if (Transposer.lineContainsChordsOnly(line) && Transposer.lineContainsTextOnly(nextLine)) {
                result.add(parseSectionLineUpper(line, nextLine))
                lineIndex++
            } else {
                result.add(SectionLine(line))
            }
        } else if (Transposer.lineContainsChordsOnly(line)) {
            result.add(parseSectionLineUpper(line, ""))
        } else {
            result.add(SectionLine(line))
        }
        lineIndex++
    }
    return SectionText(result)
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

    val patternStr = Transposer.CHORD_REGEX
    val pattern = Pattern.compile(patternStr)
    val matcher = pattern.matcher(chordLine)
    val chordGroups = mutableListOf<ChordPair>()
    var chord: String? = null
    var index = 0
    var lastText: String
    var offset = 0
    while (matcher.find()) {
        if (matcher.start() > 0) {
            if (chord == null) {
                chordGroups.add(ChordPair(null, textLine.substring(0, Math.min(matcher.start(), textLine.length))))
            } else {
                offset += 2
                if (index < 0 || index > textLine.length || matcher.start() - offset > textLine.length) {
                    lastText = ""
                } else {
                    lastText = textLine.substring(index, Math.min(matcher.start() - offset, textLine.length))
                }
                chordGroups.add(ChordPair(chord, lastText))
            }
        }
        chord = chordLine.substring(matcher.start(), matcher.end())
        index = matcher.start() - offset
    }
    lastText = textLine.substring(Math.min(index, textLine.length))
    chordGroups.add(ChordPair(chord, lastText))
    return SectionLine(chordGroups)
}

fun parseSectionLineInline(line: String): SectionLine {
    val patternStr = Transposer.CHORD_REGEX
    val pattern = Pattern.compile(patternStr)
    val matcher = pattern.matcher(line)
    val chordGroups = mutableListOf<ChordPair>()
    var lastText: String = ""
    var chord: String? = null
    var index = 0
    while (matcher.find()) {
        lastText = line.substring(index, matcher.start())
        if (matcher.start() > 0) {
            chordGroups.add(ChordPair(chord, lastText))
        }
        chord = line.substring(matcher.start(), matcher.end())
        index = matcher.end()
    }
    lastText = line.substring(index)
    chordGroups.add(ChordPair(chord, lastText))
    return SectionLine(chordGroups)
}