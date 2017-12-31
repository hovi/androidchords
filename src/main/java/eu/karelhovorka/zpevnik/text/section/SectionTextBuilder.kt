package eu.karelhovorka.zpevnik.text.section



data class SectionTextBuilder(val lines: MutableList<SectionLine> = mutableListOf()) {

    fun build(): SectionText {
        return SectionText(lines)
    }

    fun sectionLine(init: SectionLineBuilder.() -> Unit): SectionLine {
        val stateBuilder = SectionLineBuilder()
        stateBuilder.init()
        val sectionLine = stateBuilder.build()
        lines.add(sectionLine)
        return sectionLine
    }
}

fun sectionText(init: SectionTextBuilder.() -> Unit): SectionText {
    val stateBuilder = SectionTextBuilder()
    stateBuilder.init()
    val sectionLine = stateBuilder.build()
    return sectionLine
}

data class SectionLineBuilder(val chordToText: MutableList<ChordPair> = mutableListOf()) {
    fun build(): SectionLine {
        return SectionLine(chordToText)
    }

    fun chordPair(init: ChordPairBuilder.() -> Unit): ChordPair {
        val stateBuilder = ChordPairBuilder()
        stateBuilder.init()
        val chordPair = stateBuilder.build()
        chordToText.add(chordPair)
        return chordPair
    }

}

fun sectionLine(init: SectionLineBuilder.() -> Unit): SectionLine {
    val stateBuilder = SectionLineBuilder()
    stateBuilder.init()
    return stateBuilder.build()
}

data class ChordPairBuilder(var chord: String? = null, var text: String = "") {
    fun build(): ChordPair {
        return ChordPair(chord, text)
    }
}

