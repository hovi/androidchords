package mock


expect class Pattern {


    fun matcher(text: String): Matcher
}


expect class Matcher {

    fun find(): Boolean

    fun matches(): Boolean

    fun start(): Int

    fun end(): Int

    fun group(): String

    fun group(index: Int): String?

    fun appendReplacement(replacement: String): String

    fun appendTail(): String
}

expect fun String.toPattern(): Pattern