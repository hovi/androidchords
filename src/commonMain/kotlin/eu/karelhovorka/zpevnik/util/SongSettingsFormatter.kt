package eu.karelhovorka.zpevnik.util

import eu.karelhovorka.zpevnik.text.SongDisplaySettings
import kotlin.jvm.JvmStatic


object SongSettingsFormatter {

    /**
     * Only rewrites chords (plain chords get brackets, transposition, tone names) and marks comments.
     * Hiding chords, text, comments or repeated chord sequences is done by CSS, see [SongDisplaySettings.css].
     */
    @JvmStatic
    fun modifyTextBasedOnSettings(source: String, settings: SongDisplaySettings = SongDisplaySettings.DEFAULT): String {
        var text = ChordDetector.replacePlainChords(source)
        text = ToneTransposer.transposeAll(text, settings.interval, settings.countryCategory, settings.modificationAbbreviation)
        text = Comments.markComments(text)
        return text.trimEnd()
    }
}