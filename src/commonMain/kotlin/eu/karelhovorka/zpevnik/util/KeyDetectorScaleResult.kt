package eu.karelhovorka.zpevnik.util

import eu.karelhovorka.zpevnik.music.Chord
import eu.karelhovorka.zpevnik.music.Scale

class KeyDetectorScaleResultComparator(val first: Chord, val last: Chord): Comparator<KeyDetectorScaleResult> {
    override fun compare(a: KeyDetectorScaleResult, b: KeyDetectorScaleResult): Int {
        if (a.rank == b.rank) {
            if (a.scale.tone == first.tone || a.scale.tone == last.tone) {
                return -1
            }
        }
        return (b.rank * 100 - a.rank * 100).toInt()
    }

}

data class KeyDetectorScaleResult(
        val scale: Scale,
        val rank: Double
)