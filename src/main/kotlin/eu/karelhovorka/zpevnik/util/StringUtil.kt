package eu.karelhovorka.zpevnik.util

import eu.karelhovorka.zpevnik.util.Preconditions.checkNotNull

object StringUtil {

    fun removeTrailingWhitespace(text: String): String {
        checkNotNull(text, "text")
        return text.replace("\\s+$".toRegex(), "")
    }

}
