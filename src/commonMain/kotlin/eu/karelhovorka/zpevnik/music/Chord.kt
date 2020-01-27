package eu.karelhovorka.zpevnik.music

data class Chord(
        val name: String,
        val alternatives: Array<String> = emptyArray(),
        val halfTones: Array<Boolean> = Array<Boolean>(12) { false }
) {

    init {
        check(halfTones.size == 12) {
            "has to be 12 halftones, not ${halfTones.size}"
        }
    }


    companion object {
        fun makeChord(name: String, alternatives: Array<String> = emptyArray(), halfTones: Array<Boolean> = Array<Boolean>(12) { false }): Chord {
            return Chord(name = name, alternatives = alternatives, halfTones = halfTones)
        }

        fun halfTones(vararg array: Int): Array<Boolean> {
            return if (array.size == 11) {
                array.toMutableList().apply { add(0, 1) }
            } else {
                array.toMutableList()
            }.map {
                it == 1
            }.toTypedArray()
        }

        fun addAlternative(original: String, vararg alternatives: String) {

        }

        val chords = arrayOf(
                makeChord("major", alternatives = arrayOf("maj"), halfTones = halfTones(0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0)),
                makeChord("add9", halfTones = halfTones(0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0)),
                makeChord("sus2", halfTones = halfTones(0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0)),
                makeChord("sus4", halfTones = halfTones(0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0)),
                makeChord("maj7", halfTones = halfTones(0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1)),
                makeChord("7", halfTones = halfTones(0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0)),
                makeChord("minor", alternatives = arrayOf("m", "mi"), halfTones = halfTones(0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0)),
                makeChord("m7", alternatives = arrayOf("mi7"), halfTones = halfTones(0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0)),
                makeChord("5", halfTones = halfTones(0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0)),
                makeChord("6", halfTones = halfTones(0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0))
        )

        fun doAll() {
            addAlternative("major", "maj", "")
            addAlternative("minor", "mi", "m")
            addAlternative("m7", "mi7")
            addAlternative("add9", "add2")
        }
    }
}
