package eu.karelhovorka.zpevnik.text


import eu.karelhovorka.zpevnik.music.Interval
import eu.karelhovorka.zpevnik.util.Preconditions.checkNotNull
import eu.karelhovorka.zpevnik.util.Tone
import kotlin.jvm.JvmField
import kotlin.jvm.JvmOverloads


data class SongDisplaySettings @JvmOverloads constructor(
    val isDisplayText: Boolean = true,
    val isDisplayChords: Boolean = true,
    val isHideIdenticalSequences: Boolean = false,
    val isUseBold: Boolean = false,
    val isDoubleColumn: Boolean = false,
    val isResizeChords: Boolean = false,
    val showComments: Boolean = false,
    val interval: Interval = Interval.of(0),
    val countryCategory: Tone.CountryCategory = Tone.CountryCategory.EASTERN,
    val modificationAbbreviation: Tone.ModificationAbbreviation = Tone.ModificationAbbreviation.SHARP,
    val theme: String = LIGHT_THEME,
    val fontFamily: String = DEFAULT_FONT
) {

    val css: String
        get() {
            val sb = StringBuilder(theme)
            sb.append(" font-$fontFamily")
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
            if (showComments) {
                sb.append(" show-comments")
            } else {
                sb.append(" hide-comments")
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

        val SYSTEM_THEME = "system"

        @JvmField
        val PRINT_THEME = "print"

        @JvmField
        val LEGACY_THEME = "legacy"

        @JvmField
        val DEFAULT = SongDisplaySettings()
    }
}
