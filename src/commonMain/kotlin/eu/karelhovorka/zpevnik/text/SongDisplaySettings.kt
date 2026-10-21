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
    val fontFamily: String = DEFAULT_FONT,
    /** The user's section headers by section type ("verse" to "{index1}."), see [eu.karelhovorka.zpevnik.util.SectionNameTemplates]. */
    val sectionNames: Map<String, String> = emptyMap()
) {

    /** Classes for the root element of the song, the stylesheet applies the settings from them. */
    val css: String
        get() {
            val classes = mutableListOf("theme-$theme", "font-$fontFamily")
            if (isUseBold) {
                classes.add("bold")
            }
            if (isResizeChords) {
                classes.add("big-chords")
            }
            if (isDoubleColumn) {
                classes.add("double-column")
            }
            if (!isDisplayChords) {
                classes.add("hide-chords")
            }
            if (!isDisplayText) {
                classes.add("hide-text")
            }
            if (!showComments) {
                classes.add("hide-comments")
            }
            if (isHideIdenticalSequences) {
                classes.add("hide-identical-sequences")
            }
            return classes.joinToString(" ")
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
