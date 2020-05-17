package eu.karelhovorka.zpevnik.util

import eu.karelhovorka.zpevnik.music.Chord
import eu.karelhovorka.zpevnik.music.Scale
import eu.karelhovorka.zpevnik.music.ScaleType
import eu.karelhovorka.zpevnik.music.percentShared


data class Section(
        val chords: Array<Chord>
)

class KeyDetector(val sections: Array<Section>, scaleTypes: Array<ScaleType> = ScaleType.values()) {

    constructor(text: String, scaleTypes: Array<ScaleType> = ScaleType.values()) : this(textToSections(text), scaleTypes)

    val allChords: List<Chord> = sections.flatMap { it.chords.asIterable() }
    val uniqueChords = allChords.distinct()
    val chordsByOccurence = allChords.groupingBy { it }.eachCount()
    val firstChord = sections.first().chords.first()
    val lastChord = sections.last().chords.last()

    val scales = scaleTypes.flatMap { scaleType ->
        Tone.values().map {
            Scale(
                    tone = it,
                    type = scaleType
            )
        }
    }


    fun detect(): List<KeyDetectorScaleResult> {
        return scales.map { scale ->
            val rank = allChords.map {
                it.percentShared(scale)
            }.average()
            KeyDetectorScaleResult(
                    scale = scale,
                    rank = rank
            )
        }.filter { it.rank > 0.9 }.sortedBy {
            -it.rank
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