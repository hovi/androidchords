package mock

actual data class Matcher(val reg: Regex, val text: String) {

    var results: Iterator<MatchResult>? = null

    var currentMatch: MatchResult? = null

    var lastAppend = 0

    actual fun find(): Boolean {
        if (results == null) {
            results = reg.findAll(text).iterator()
        }
        if (results!!.hasNext()) {
            currentMatch = results!!.next()
            return true
        }
        return false
    }

    actual fun matches(): Boolean {
        val matches = find()
        results = null
        return matches
    }

    actual fun start(): Int {
        return currentMatch!!.range.start
    }

    fun start(index: Int): Int {
        return currentMatch!!.groups[index]!!.range.start
    }

    actual fun end(): Int {
        return currentMatch!!.range.endInclusive + 1
    }

    fun end(index: Int): Int {
        return currentMatch!!.groups[index]!!.range.endInclusive + 1
    }

    actual fun group(): String {
        return currentMatch!!.value
    }

    actual fun group(index: Int): String? {
        if (currentMatch == null || index >= currentMatch!!.groupValues.size) {
            return null
        }
        return currentMatch!!.groupValues[index]
    }

    fun groupCount(): Int {
        return currentMatch!!.groupValues.size
    }

    actual fun appendReplacement(replacement: String): String {
        val result = text.substring(lastAppend, start()) + replacement
        lastAppend = end()
        return result
    }

    actual fun appendTail(): String {
        return text.substring(lastAppend)
    }
}

actual data class Pattern(val text: String) {


    actual fun matcher(text: String): Matcher {
        return Matcher(Regex(this.text), text)
    }
}

actual fun String.toPattern(): Pattern {
    return Pattern(this)
}