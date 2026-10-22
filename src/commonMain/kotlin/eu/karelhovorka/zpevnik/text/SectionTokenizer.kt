package eu.karelhovorka.zpevnik.text


import eu.karelhovorka.zpevnik.text.section.parseSectionText
import eu.karelhovorka.zpevnik.util.ChordDetector
import eu.karelhovorka.zpevnik.util.I18N
import eu.karelhovorka.zpevnik.util.Transposer
import mock.toPattern
import kotlin.jvm.JvmField

class SectionTokenizer(private val i18n: I18N = I18N()) {

    fun getSectionsOrSingleSection(text: String): List<Section> {
        try {
            return getSections(text.trimEnd())
        } catch (e: Exception) {
            //e.printStackTrace()
            return listOf(Section(parseSectionText("", text.trimEnd()), i18N = i18n, type = SectionType.EMPTY))
        }
    }

    fun getSections(text: String): List<Section> {
        val lines = text.trimEnd().lines().dropLastWhile { it.isEmpty() }
        val sections = mutableListOf<Section>()
        val sectionTypeCount: MutableMap<ISectionType, Int> = mutableMapOf()
        val seenChordSequences = mutableSetOf<List<String>>()
        var sectionBuilder: SectionBuilder? = null
        for (line in lines) {
            val headerWithText = splitHeaderWithText(line)
            if (line.trim().matches(SECTION_TYPE_BASIC_REGEX)) {
                if (sectionBuilder != null && !sectionBuilder.empty()) {
                    sections.add(sectionBuilder.parseSection(sectionTypeCount, i18n, seenChordSequences))
                }
                sectionBuilder = SectionBuilder(StringBuilder(), line)
            } else if (headerWithText != null) {
                val chordLineAbove = sectionBuilder?.removeTrailingChordLine()
                if (sectionBuilder != null && !sectionBuilder.empty()) {
                    sections.add(sectionBuilder.parseSection(sectionTypeCount, i18n, seenChordSequences))
                }
                val (label, prefix, plainText) = headerWithText
                val textStart = prefix.length
                // plain chords are bracketed already when the text went through ChordDetector.replacePlainChords
                val text = ChordDetector.replacePlainChordsInline(plainText)
                // keep columns aligned with the chord line above or below
                val aligned = chordLineAbove != null || Transposer.lineContainsChordsOnly(text)
                val firstLine = if (aligned) " ".repeat(textStart) + text else text
                sectionBuilder = SectionBuilder(
                    StringBuilder(),
                    label,
                    textStart,
                    !aligned,
                    headerLines = if (chordLineAbove == null) 1 else 2,
                    headerPrefix = prefix
                )
                chordLineAbove?.let { sectionBuilder.sb.append(it + "\n") }
                sectionBuilder.sb.append(firstLine + "\n")
            } else {
                if (sectionBuilder == null) {
                    sectionBuilder = SectionBuilder(StringBuilder(), "")
                }
                sectionBuilder.sb.append(line + "\n")
            }
        }
        if (sectionBuilder != null) {
            sections.add(sectionBuilder.parseSection(sectionTypeCount, i18n, seenChordSequences))
        }
        return mergeSections(sections)
    }

    private fun mergeSections(originalSections: List<Section>): List<Section> {
        val mergedSections = mutableListOf<Section>()
        for (section in originalSections) {
            var sectionToAdd = section
            for (j in mergedSections.indices.reversed()) {
                val previousSection = mergedSections[j]
                if (section.sameContentAndType(previousSection)) {
                    sectionToAdd = Section.makeCopyOf(previousSection, section)
                    break
                }
            }
            mergedSections.add(sectionToAdd)
        }
        return mergedSections
    }

    companion object {

        private const val TYPE_LABEL = "(?:[A-ZÁČĎÉĚÍŇÓŘŠŤÚŮÝŽ][A-Za-zÀ-ž]*\\.? ?([0-9]*)\\.?:|([0-9]+)\\.)"

        @JvmField
        val TYPE_REGEX = "$TYPE_LABEL[\t ]*"

        val SECTION_TYPE_BASIC_REGEX = "^$TYPE_REGEX$".toRegex()

        private val HEADER_WITH_TEXT_REGEX = "^[\t ]*($TYPE_LABEL)[\t ]+(\\S.*)$".toRegex()

        /**
         * A known section header written in front of text ("R: text", "1. text"):
         * the header label, everything in front of the text, and the text.
         */
        fun splitHeaderWithText(line: String): Triple<String, String, String>? {
            val match = HEADER_WITH_TEXT_REGEX.matchEntire(line) ?: return null
            if (SectionType.fromName(match.groupValues[1]) !is SectionType) {
                return null
            }
            val text = match.groupValues[4]
            return Triple(match.groupValues[1], line.substring(0, line.length - text.length), text)
        }

        val VALID_TEXT_PATTERN = "^\\s*$TYPE_REGEX(?:\n|\r).*".toPattern()

        fun isValid(text: String): Boolean {
            try {
                validate(text)
                return true
            } catch (e: Exception) {
                //e.printStackTrace()
                return false
            }

        }

        fun validate(text: String) {
            if (!VALID_TEXT_PATTERN.matcher(text).find()) {
                throw IllegalStateException("Not starting with section header")
            }
            val m = "$TYPE_REGEX(?:\n|\r)".toPattern().matcher(text)
            var count = 0
            while (m.find()) {
                count += 1
            }
            if (count < 2) {
                throw IllegalStateException("not enough sections: " + count)
            }
        }
    }
}
