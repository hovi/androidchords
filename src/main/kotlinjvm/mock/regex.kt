package mock

data class Matcher(val reg: Regex, val text: String) {

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

    fun start(index: Int): Int {
        return currentMatch!!.groups[index]!!.range.start
    }

    fun end(): Int {
        return currentMatch!!.range.endInclusive + 1
    }

    fun end(index: Int): Int {
        return currentMatch!!.groups[index]!!.range.endInclusive + 1
    }

    fun group(): String {
        return currentMatch!!.value
    }

    fun group(index: Int): String? {
        if (currentMatch == null || index >= currentMatch!!.groupValues.size) {
            return null
        }
        println(currentMatch!!.groupValues)
        return currentMatch!!.groupValues[index]
    }

    fun groupCount(): Int {
        return currentMatch!!.groupValues.size
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