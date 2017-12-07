package eu.karelhovorka.zpevnik.util;

import eu.karelhovorka.zpevnik.text.SongDisplaySettings;

import static eu.karelhovorka.zpevnik.util.Preconditions.checkNotNull;


public class SongSettingsFormatter {


    public static String modifyTextBasedOnSettings(String source, SongDisplaySettings settings) {
        checkNotNull(source, "source");
        checkNotNull(settings, "settings");
        String text = source;
        text = ChordDetector.replacePlainChords(text);
        if (!settings.isDisplayText()) {
            text = Transposer.removeNonChords(text);
        }
        if (settings.isHideIdenticalSequences()) {
            text = Transposer.removeDuplicateChordSequences(text);
        }
        if (settings.isDisplayChords()) {
            text = ToneTransposer.transposeAll(text, settings.getInterval(), settings.getCountryCategory(), settings.getModificationAbbreviation());
        } else {
            text = Transposer.removeChords(text);
        }
        return StringUtil.removeTrailingWhitespace(text);
    }
}
