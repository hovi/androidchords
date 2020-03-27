package eu.karelhovorka.zpevnik.text

import eu.karelhovorka.zpevnik.music.ToneMod
import eu.karelhovorka.zpevnik.util.Tone

data class SongMetadata(
        val bpm: Float? = null,
        val capo: Int? = null,
        val key: Tone? = null,
        val durationSeconds: Float? = null,
        val mode: ToneMod = ToneMod.UNKNOWN
) {


}