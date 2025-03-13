package eu.karelhovorka.zpevnik.music

val scales = """
major;P1,M2,M3,P4,P5,M6,M7;
natural_minor;P1,M2,m3,P4,P5,m6,m7;
harmonic_minor;P1,M2,m3,P4,P5,m6,M7;
melodic_minor;P1,M2,m3,P4,P5,M6,M7;
major_pentatonic;P1,M2,M3,P5,M6;
minor_pentatonic;P1,m3,P4,P5,m7;
ionian;P1,M2,M3,P4,P5,M6,M7;
dorian;P1,M2,m3,P4,P5,M6,m7;
phrygian;P1,m2,m3,P4,P5,m6,m7;
lydian;P1,M2,M3,A4,P5,M6,M7;
mixolydian;P1,M2,M3,P4,P5,M6,m7;
aeolian;P1,M2,m3,P4,P5,m6,m7;
locrian;P1,m2,m3,P4,d5,m6,m7;
""".trimIndent()

fun g2(): List<String> {
    return scales.lines().map {
        val s = it.split(";")
        val name = s.first()
        val intervals = s[1].split(",").joinToString(", ")
        //MAJOR(arrayOf("major"), arrayOf(BasicInterval.P1, BasicInterval.M2, BasicInterval.M3, BasicInterval.P4, BasicInterval.P5, BasicInterval.M6, BasicInterval.M7))
        """${name.lowercase()}(arrayOf("${name}"), arrayOf(${intervals}))"""
    }
}


fun generateCodeScaleType(): String {
    return clsScaleType
}




val clsScaleType = """
package eu.karelhovorka.zpevnik.music


${generateIntervalImports().joinToString("\n")}

enum class ScaleType(val aliases: Array<String>, override val intervals: Array<Interval>) : IntervalGroup {

${g2().joinToString(",\n", postfix = ";")}

 
    companion object {
    }

}
""".trimIndent()