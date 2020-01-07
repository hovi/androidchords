package eu.karelhovorka.zpevnik.util

import eu.karelhovorka.zpevnik.util.Preconditions.checkNotNull

fun String.nullIfEmpty(): String? {
    if (isEmpty()) {
        return null
    }
    return this
}

object StringUtil {

    fun removeTrailingWhitespace(text: String): String {
        checkNotNull(text, "text")
        return text.replace("\\s+$".toRegex(), "")
    }

}
