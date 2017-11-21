package eu.karelhovorka.zpevnik.text;


import eu.karelhovorka.zpevnik.music.Interval;
import eu.karelhovorka.zpevnik.util.Tone;

import static eu.karelhovorka.zpevnik.util.Preconditions.checkNotNull;

public class SongDisplaySettings {

    public static final String LIGHT_THEME = "light";

    public static final String DARK_THEME = "dark";

    public static final SongDisplaySettings DEFAULT = new SongDisplaySettings(true, true, false, false, false, false, Interval.PERFECT_UNISON, Tone.CountryCategory.EASTERN, Tone.ModificationAbbreviation.SHARP, DARK_THEME);

    private boolean displayText;

    private boolean displayChords;

    private boolean hideIdenticalSequences;

    private boolean useBold;

    private boolean doubleColumn;

    private boolean resizeChords;

    private Interval interval;

    private Tone.CountryCategory countryCategory;

    private Tone.ModificationAbbreviation modificationAbbreviation;

    private String theme;

    public SongDisplaySettings(boolean displayText, boolean displayChords, boolean hideIdenticalSequences, boolean useBold, boolean doubleColumn, boolean resizeChords, Interval interval, Tone.CountryCategory countryCategory, Tone.ModificationAbbreviation modificationAbbreviation, String theme) {
        checkNotNull(countryCategory, "countryCategory");
        checkNotNull(modificationAbbreviation, "modificationAbbreviation");
        checkNotNull(theme, "theme");
        checkNotNull(interval, "interval");
        this.displayText = displayText;
        this.doubleColumn = doubleColumn;
        this.displayChords = displayChords;
        this.useBold = useBold;
        this.resizeChords = resizeChords;
        this.hideIdenticalSequences = hideIdenticalSequences;
        this.interval = interval;
        this.countryCategory = countryCategory;
        this.modificationAbbreviation = modificationAbbreviation;
        this.theme = theme;
    }

    public boolean isDisplayText() {
        return displayText;
    }

    public boolean isDisplayChords() {
        return displayChords;
    }

    public boolean isHideIdenticalSequences() {
        return hideIdenticalSequences;
    }

    public Interval getInterval() {
        return interval;
    }

    public Tone.CountryCategory getCountryCategory() {
        return countryCategory;
    }

    public Tone.ModificationAbbreviation getModificationAbbreviation() {
        return modificationAbbreviation;
    }

    public boolean isUseBold() {
        return useBold;
    }

    public boolean isDoubleColumn() {
        return doubleColumn;
    }

    public boolean isResizeChords() {
        return resizeChords;
    }

    public String getTheme() {
        return theme;
    }

    public String getCss() {
        StringBuilder sb = new StringBuilder(theme);
        if (useBold) {
            sb.append(" use-bold");
        }
        if (resizeChords) {
            sb.append(" resize-chords");
        }
        if (doubleColumn) {
            sb.append(" double-column");
        }
        if (hideIdenticalSequences) {
            sb.append(" hide-identical-sequences");
        }
        if (displayChords) {
            sb.append(" display-chords");
        }
        if (displayText) {
            sb.append(" display-text");
        }
        return sb.toString();
    }
}
