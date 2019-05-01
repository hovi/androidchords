package eu.karelhovorka.zpevnik.text


import eu.karelhovorka.zpevnik.music.Interval
import eu.karelhovorka.zpevnik.util.Preconditions.checkNotNull
import eu.karelhovorka.zpevnik.util.Tone
import kotlin.jvm.JvmField

data class SongDisplaySettings(val isDisplayText: Boolean, val isDisplayChords: Boolean, val isHideIdenticalSequences: Boolean, val isUseBold: Boolean, val isDoubleColumn: Boolean, val isResizeChords: Boolean, val interval: Interval, val countryCategory: Tone.CountryCategory, val modificationAbbreviation: Tone.ModificationAbbreviation, val theme: String, val fontFamily: String) {

    val css: String
        get() {
            val sb = StringBuilder(theme)
            sb.append(" font-" + fontFamily)
            if (isUseBold) {
                sb.append(" use-bold")
            }
            if (isResizeChords) {
                sb.append(" resize-chords")
            }
            if (isDoubleColumn) {
                sb.append(" double-column")
            }
            if (isHideIdenticalSequences) {
                sb.append(" hide-identical-sequences")
            }
            if (isDisplayChords) {
                sb.append(" display-chords")
            }
            if (isDisplayText) {
                sb.append(" display-text")
            }
            return sb.toString()
        }

    init {
        checkNotNull(countryCategory, "countryCategory")
        checkNotNull(modificationAbbreviation, "modificationAbbreviation")
        checkNotNull(theme, "theme")
        checkNotNull(interval, "interval")
    }

    companion object {

        @JvmField
        val DEFAULT_FONT = "normal"

        @JvmField
        val LIGHT_THEME = "light"

        @JvmField
        val DARK_THEME = "dark"

        @JvmField
        val PRINT_THEME = "print"

        @JvmField
        val LEGACY_THEME = "legacy"

        @JvmField
        val DEFAULT = SongDisplaySettings(true, true, false, false, false, false, Interval.PERFECT_UNISON, Tone.CountryCategory.EASTERN, Tone.ModificationAbbreviation.SHARP, DARK_THEME, DEFAULT_FONT)
    }
}
