package eu.karelhovorka.zpevnik.text.section

data class TextSegment(val css: String, val text: String) {
    companion object {
        val REPEAT_START = TextSegment("repeat-start", "")
        val REPEAT_END = TextSegment("repeat-end", "")

        fun text(text: String) = TextSegment("text", text)
    }
}

// "2x", "(2x)", "x2", "2×" at the end of a line
private val REPEAT_COUNT_REGEX = "(?:^|\\s+)\\(?(?:([0-9]+)\\s*[x×]|[x×]\\s*([0-9]+))\\)?\\s*$".toRegex()

// "/: ... :/" or "|: ... :|", but not the ":/" in "http://"
private val REPEAT_MARK_REGEX = "/:|:/(?!/)|\\|:|:\\|".toRegex()

fun splitRepeatCount(line: String): Pair<String, Int?> {
    val match = REPEAT_COUNT_REGEX.find(line) ?: return Pair(line, null)
    val count = match.groupValues[1].ifEmpty { match.groupValues[2] }.toInt()
    return Pair(line.substring(0, match.range.first), count)
}

fun blankRepeatMarks(line: String): String = line.replace(REPEAT_MARK_REGEX, "  ")

fun textSegments(text: String): List<TextSegment> {
    val segments = mutableListOf<TextSegment>()
    var index = 0
    for (match in REPEAT_MARK_REGEX.findAll(text)) {
        if (match.range.first > index) {
            segments.add(TextSegment.text(text.substring(index, match.range.first)))
        }
        segments.add(if (match.value.startsWith(":")) TextSegment.REPEAT_END else TextSegment.REPEAT_START)
        index = match.range.last + 1
    }
    if (index < text.length || segments.isEmpty()) {
        segments.add(TextSegment.text(text.substring(index)))
    }
    return segments
}
