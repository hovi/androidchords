package eu.karelhovorka.zpevnik.util

import eu.karelhovorka.zpevnik.music.Chord
import eu.karelhovorka.zpevnik.music.Scale
import eu.karelhovorka.zpevnik.music.ScaleType
import eu.karelhovorka.zpevnik.music.percentShared
import eu.karelhovorka.zpevnik.text.SongText
import eu.karelhovorka.zpevnik.util.Transposer.FULL_CHORD_REGEX


data class Section(
        val chords: Array<Chord>
)

class KeyDetector(val sections: Array<Section>, scaleTypes: Array<ScaleType> = ScaleType.values(), val minRank: Double = 0.9) {

    constructor(text: String, scaleTypes: Array<ScaleType> = ScaleType.values()) : this(simpleTextToSections(text), scaleTypes)

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
        }.filter { it.rank >= minRank }.sortedWith(KeyDetectorScaleResultComparator(firstChord, lastChord))
    }

    companion object {

        fun songTextToSections(songText: SongText): Array<Section> {
            return songText.sections.mapNotNull {
                it.content.originalText?.let { originalText ->
                    Section(
                            FULL_CHORD_REGEX.findAll(originalText).mapNotNull {
                                Chord.fromMatchResultOrNull(it)
                            }.toList().toTypedArray()
                    )
                }
            }.toTypedArray()
        }

        fun textToSections(text: String): Array<Section> {
            return arrayOf(
                    Section(
                            FULL_CHORD_REGEX.findAll(text).mapNotNull {
                                Chord.fromMatchResultOrNull(it)
                            }.toList().toTypedArray()
                    )
            )
        }

        fun simpleTextToSections(text: String): Array<Section> {
            return arrayOf(Section(
                    chords = text.split(" ").map { Chord.fromString(it) }.toTypedArray()
            ))
        }
    }
}