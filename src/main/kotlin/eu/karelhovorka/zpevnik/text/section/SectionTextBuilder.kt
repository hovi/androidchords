package eu.karelhovorka.zpevnik.text.section

import eu.karelhovorka.zpevnik.text.ISectionType
import eu.karelhovorka.zpevnik.text.Section
import eu.karelhovorka.zpevnik.text.SectionType
import eu.karelhovorka.zpevnik.util.I18N


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

data class SectionBuilder(var content: SectionText? = null, var type: ISectionType = SectionType.UNKNOWN, var copyOf: Section? = null, var copyIndex: Int = 0, var index: Int = 0, val i18n: I18N = I18N()) {


    fun sectionText(init: SectionTextBuilder.() -> Unit): SectionText {
        val stateBuilder = SectionTextBuilder()
        stateBuilder.init()
        val st = stateBuilder.build()
        content = st
        return st
    }

    fun build(): Section {
        return Section(content = content!!, type = type, copyOf = copyOf, copyIndex = copyIndex, index = index, i18N = i18n)
    }
}

fun sections(init: SectionsBuilder.() -> Unit): List<Section> {
    val sectionBuilder = SectionsBuilder()
    sectionBuilder.init()
    return sectionBuilder.build()
}

data class SectionsBuilder(var sections: MutableList<Section> = mutableListOf<Section>()) {


    fun section(init: SectionBuilder.() -> Unit): Section {
        val sectionBuilder = SectionBuilder()
        sectionBuilder.init()
        val section = sectionBuilder.build()
        sections.add(section)
        return section
    }

    fun build(): List<Section> {
        return sections
    }
}


fun section(init: SectionBuilder.() -> Unit): Section {
    val sectionBuilder = SectionBuilder()
    sectionBuilder.init()
    return sectionBuilder.build()
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

