package eu.karelhovorka.zpevnik.util

import eu.karelhovorka.zpevnik.util.Preconditions.checkNotNull

fun String.nullIfEmpty(): String? {
    if (isEmpty()) {
        return null
    }
    return this
}

private const val TAB_WIDTH = 8

fun expandTabs(line: String): String {
    if (!line.contains('\t')) {
        return line
    }
    val sb = StringBuilder()
    for (c in line) {
        if (c == '\t') {
            do {
                sb.append(' ')
            } while (sb.length % TAB_WIDTH != 0)
        } else {
            sb.append(c)
        }
    }
    return sb.toString()
}

object StringUtil {

    fun removeTrailingWhitespace(text: String): String {
        checkNotNull(text, "text")
        return text.replace("\\s+$".toRegex(), "")
    }

}
