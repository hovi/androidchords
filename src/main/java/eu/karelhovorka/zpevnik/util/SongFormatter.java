package eu.karelhovorka.zpevnik.util;

import java.util.List;

import eu.karelhovorka.zpevnik.text.Section;
import eu.karelhovorka.zpevnik.text.SongDisplaySettings;


public class SongFormatter {
    public static final String CHORD_URL_PROTOCOL = "chord://";

    public static String formatHtml(List<Section> sections) {
        StringBuilder html = new StringBuilder();
        for (Section section : sections) {
            html.append(formatHtml(section));
        }
        return html.toString();
    }

    public static String formatHtml(Section section) {
        StringBuilder html = new StringBuilder();
        html.append("<div class='song-section " + section.getSectionType().css() + "'>");
        html.append(section.getContent());
        html.append("</div>");
        return html.toString();
    }

    public static String formatHtmlChords(String content) {
        return content.replaceAll(Transposer.CHORD_REGEX, "<a class='chord' href='" + CHORD_URL_PROTOCOL + "$1'><sup>" + "$1" + "</sup></a>");
    }

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
