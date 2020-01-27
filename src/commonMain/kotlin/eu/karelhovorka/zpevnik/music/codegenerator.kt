package eu.karelhovorka.zpevnik.music

val SHRT_REGEX = "([APMmd])(\\d)".toRegex()


val clsChordType = """
package eu.karelhovorka.zpevnik.music

${generateIntervalImports().joinToString("\n")}

enum class ChordType(val longName: String, val shortcuts: Array<String>, val intervals: Array<Interval>) {

${g().joinToString(",", postfix = ";")}

}
""".trimIndent()

val clsInterval = """
package eu.karelhovorka.zpevnik.music

import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic

enum class BasicInterval(override val step: Int, val intervalName: String, val latin: String, val index: Int, type: IntervalType): Interval {
    ${generateIntervals().joinToString(separator = ",", postfix = ";")}

    companion object {
        ${generateIntervalAliases().joinToString("\n")}
        
        @JvmStatic
        fun of(step: Int): Interval {
            return values()[(step % 12 + 12) % 12]
        }
    }
}
""".trimIndent()

fun generateCodeChordType(): String {
    return clsChordType
}

fun generateIntervalType(): String {
    return clsInterval
}

fun g(): List<String> {
    return chords.trim().lines().filter { it.isNotBlank() && !it.startsWith("#") }
            .map { line ->
                val cols = line.split(";").map { it.trim() }
                val name = cols[0]
                val suffixes = cols[1].split(",").map { it.trim() }
                val intervals = cols[2].split(",").map { it.trim() }
                val enumName = name.replace("[ -]".toRegex(), "_").toUpperCase()
                ("""
    $enumName(
        longName = "$name",
        shortcuts = arrayOf(${suffixes.joinToString(separator = "\",\"", prefix = "\"", postfix = "\"")}),
        intervals = arrayOf(${intervals.joinToString(separator = ",")})
    )
                """.trimIndent())
            }
}

fun generateIntervals(): List<String> {
    return intervals.trim().lines().map { line ->
        val cols = line.trim().split("\t")
        val semiTone = cols[0].toInt()
        val name = cols[1]
        val latin = cols[3]
        val short = cols[2]
        val index = short.replace(SHRT_REGEX, "$2").toInt()
        val type = short.replace(SHRT_REGEX, "$1")[0]
        val enum = name.replace(" ", "_").toUpperCase()
        ("""
$enum (
    step = $semiTone, 
    intervalName = "$name",
    latin = "$latin",
    index = $index,
    type = IntervalType.fromShortcut('$type')
)
			""".trimIndent())
    }
}

fun generateIntervalAliases(): List<String> {
    return intervals.trim().lines().map { line ->
        val cols = line.trim().split("\t")
        val semiTone = cols[0].toInt()
        val name = cols[1]
        val latin = cols[3]
        val short = cols[2]
        val index = short.replace(SHRT_REGEX, "$2").toInt()
        val type = short.replace(SHRT_REGEX, "$1")[0]
        val enum = name.replace(" ", "_").toUpperCase()
        ("""
                val $short: Interval
                    @JvmName("get${short}${type.toInt()}")
                    get() = $enum
			""".trimIndent())
    } + intervalAliases.trim().lines().map {
        val cols = it.split(",")
        val new = cols.first()
        val old = cols.last()
        ("""
                val $new: Interval
                    @JvmName("get${old}${new}${old.last().toInt()}")
                    get() = $old
			""".trimIndent())
    }
}

fun generateIntervalImports(): List<String> {
    return intervals.trim().lines().map { line ->
        val cols = line.trim().split("\t").map { it.trim() }
        val semiTone = cols[0].toInt()
        val name = cols[1]
        val latin = cols[3]
        val shrt = cols[2]
        val type = shrt.replace(SHRT_REGEX, "$1")[0]
        val index = shrt
                .replace(
                        SHRT_REGEX,
                        "$2"
                )
                .toInt()

        val enum = name.replace(" ", "_").toUpperCase()
        ("""
            import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.$shrt
			""".trimIndent())
    } + intervalAliases.trim().lines().map {
        val shrt = it.trim().split(",").first()
        ("""
            import eu.karelhovorka.zpevnik.music.BasicInterval.Companion.$shrt
			""".trimIndent())

    }
}