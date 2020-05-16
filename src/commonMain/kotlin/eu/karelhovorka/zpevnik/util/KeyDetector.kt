package eu.karelhovorka.zpevnik.util

import eu.karelhovorka.zpevnik.music.Chord
import eu.karelhovorka.zpevnik.music.Scale
import eu.karelhovorka.zpevnik.music.ScaleType


data class Section(
        val chords: Array<Chord>
)

class KeyDetector(val sections: Array<Section>, scaleTypes: Array<ScaleType> = ScaleType.values()) {

    constructor(text: String, scaleTypes: Array<ScaleType> = ScaleType.values()) : this(textToSections(text), scaleTypes)

    val allChords: List<Chord> = sections.flatMap { it.chords.asIterable() }
    val uniqueChords = allChords.distinct()
    val chordsByOccurence = allChords.groupingBy { it }.eachCount()

    val scales = scaleTypes.flatMap { scaleType ->
        Tone.values().map {
            Scale(
                    tone = it,
                    type = scaleType
            )
        }
    }


    fun detect(): List<Scale> {
        return scales.filter { scale ->
            uniqueChords.all { scale.contains(it) }
        }
    }

    companion object {
        fun textToSections(text: String): Array<Section> {
            return arrayOf(Section(
                    chords = text.split(" ").map { Chord.fromString(it) }.toTypedArray()
            ))
        }
    }
}