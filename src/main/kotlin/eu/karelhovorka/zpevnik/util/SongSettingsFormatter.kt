package eu.karelhovorka.zpevnik.util

import eu.karelhovorka.zpevnik.text.SongDisplaySettings
import mock.JvmStatic


object SongSettingsFormatter {

    @JvmStatic
    fun modifyTextBasedOnSettings(source: String, settings: SongDisplaySettings = SongDisplaySettings.DEFAULT): String {
        var text = source
        text = ChordDetector.replacePlainChords(text)
        if (!settings.isDisplayText) {
            text = Transposer.removeNonChords(text)
        }
        if (settings.isHideIdenticalSequences) {
            text = Transposer.removeDuplicateChordSequences(text)
        }
        if (settings.isDisplayChords) {
            text = ToneTransposer.transposeAll(text, settings.interval, settings.countryCategory, settings.modificationAbbreviation)
        } else {
            text = Transposer.removeChords(text)
        }
        return text.trimEnd()
    }
}