package eu.karelhovorka.zpevnik.music

import eu.karelhovorka.zpevnik.util.Tone

data class Chord(
        val tone: Tone,
        val type: ChordType
)
