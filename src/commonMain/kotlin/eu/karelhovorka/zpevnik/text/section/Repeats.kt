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

/** The line without its repeat count, the count, and the count as written (e.g. " (2x)"). */
fun splitRepeatCount(line: String): Triple<String, Int?, String> {
    val match = REPEAT_COUNT_REGEX.find(line) ?: return Triple(line, null, "")
    val count = match.groupValues[1].ifEmpty { match.groupValues[2] }.toInt()
    return Triple(line.substring(0, match.range.first), count, match.value)
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
