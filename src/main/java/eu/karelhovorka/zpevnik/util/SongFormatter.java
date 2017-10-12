package eu.karelhovorka.zpevnik.util;

import eu.karelhovorka.zpevnik.text.SongDisplaySettings;


public class SongFormatter {

    public static String modifyTextBasedOnSettings(String source, SongDisplaySettings settings) {
        String text = source;
        text = ChordDetector.replacePlainChords(text);
        if (!settings.isDisplayText()) {
            text = Transposer.removeNonChords(text);
        }
        if (settings.isHideIdentivalSequences()) {
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
