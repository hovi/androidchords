package eu.karelhovorka.zpevnik.formatter;


import java.util.List;

import eu.karelhovorka.zpevnik.text.Section;
import eu.karelhovorka.zpevnik.util.Transposer;

public class StaticHtmlSongFormatter implements SongFormatter {

    public String formatHtml(List<Section> sections) {
        StringBuilder html = new StringBuilder();
        for (Section section : sections) {
            html.append(formatHtml(section));
        }
        return html.toString();
    }

    private String formatHtml(Section section) {
        StringBuilder html = new StringBuilder();
        html.append("<div class='song-section " + section.getSectionType().css() + "'>");
        html.append(section.getContent());
        html.append("</div>");
        return html.toString();
    }

    public String formatHtmlChords(String content) {
        return content.replaceAll(Transposer.CHORD_REGEX, " <a class='chord' href='" + CHORD_URL_PROTOCOL + "$1'><sup>" + "$1" + "</sup></a> ");
    }

}
