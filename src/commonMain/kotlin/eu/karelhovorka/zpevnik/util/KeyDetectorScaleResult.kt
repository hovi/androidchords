package eu.karelhovorka.zpevnik.util

import eu.karelhovorka.zpevnik.music.Scale

data class KeyDetectorScaleResult(
        val scale: Scale,
        val rank: Double
)