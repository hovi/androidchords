package mock

data class Matcher(val reg: Regex, val text: String) {

    var index = 0

    var results: Iterator<MatchResult>? = null

    var currentMatch: MatchResult? = null

    var lastAppend = 0

    fun find(): Boolean {
        if (results == null) {
            results = reg.findAll(text).iterator()
        }
        if (results!!.hasNext()) {
            currentMatch = results!!.next()
            return true
        }
        return false
    }

    fun matches(): Boolean {
        val matches = find()
        results = null
        return matches
    }

    fun start(): Int {
        return currentMatch!!.range.start
    }

    fun end(): Int {
        return currentMatch!!.range.endInclusive + 1
    }

    fun group(): String {
        return currentMatch!!.value
    }

    fun group(index: Int): String {
        return currentMatch!!.groupValues[index]
    }

    fun appendReplacement(replacement: String): String {
        val result = text.substring(lastAppend, start()) + replacement
        lastAppend = end()
        return result
    }

    fun appendTail(): String {
        return text.substring(lastAppend)
    }
}

data class Pattern(val text: String) {


    fun matcher(text: String): Matcher {
        return Matcher(Regex(this.text), text)
    }
}

fun String.toPattern(): Pattern {
    return Pattern(this)
}