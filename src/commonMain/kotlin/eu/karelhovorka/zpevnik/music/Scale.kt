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

enum class Scale(val aliases: Array<String>,vararg val intervals: Interval) {
    ;

    fun contains(chord: Chord): Boolean {
        return false
    }
}