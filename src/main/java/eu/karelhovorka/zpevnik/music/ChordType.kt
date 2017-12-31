package eu.karelhovorka.zpevnik.music

enum class ChordType private constructor(aliases: Array<String>, vararg intervals: Int) {
    DUR(arrayOf("", "dur"), Interval.MAJOR_THIRD.step, Interval.PERFECT_FIFTH.step),
    MOL(arrayOf("mol", "mi", "m"), Interval.MAJOR_THIRD.step, Interval.PERFECT_FIFTH.step);

    private val intervals: List<Int>? = null
}
