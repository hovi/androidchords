package eu.karelhovorka.zpevnik.text;


import eu.karelhovorka.zpevnik.music.Interval;
import eu.karelhovorka.zpevnik.util.Tone;

public class SongDisplaySettings {

    public static final SongDisplaySettings DEFAULT = new SongDisplaySettings(true, true, false, false, false, false, Interval.PERFECT_UNISON, Tone.CountryCategory.EASTERN, Tone.ModificationAbbreviation.SHARP);

    private boolean displayText;

    private boolean displayChords;

    private boolean hideIdentivalSequences;

    private boolean useBold;

    private boolean doubleColumn;

    private boolean resizeChords;

    private Interval interval;

    private Tone.CountryCategory countryCategory;

    private Tone.ModificationAbbreviation modificationAbbreviation;

    public SongDisplaySettings(boolean displayText, boolean displayChords, boolean hideIdentivalSequences, boolean useBold, boolean doubleColumn, boolean resizeChords, Interval interval, Tone.CountryCategory countryCategory, Tone.ModificationAbbreviation modificationAbbreviation) {
        this.displayText = displayText;
        this.doubleColumn = doubleColumn;
        this.displayChords = displayChords;
        this.useBold = useBold;
        this.resizeChords = resizeChords;
        this.hideIdentivalSequences = hideIdentivalSequences;
        this.interval = interval;
        this.countryCategory = countryCategory;
        this.modificationAbbreviation = modificationAbbreviation;
    }

    public boolean isDisplayText() {
        return displayText;
    }

    public boolean isDisplayChords() {
        return displayChords;
    }

    public boolean isHideIdentivalSequences() {
        return hideIdentivalSequences;
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
}
