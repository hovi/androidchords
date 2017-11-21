package eu.karelhovorka.zpevnik.formatter;


import eu.karelhovorka.zpevnik.text.Section;
import eu.karelhovorka.zpevnik.text.SongText;
import eu.karelhovorka.zpevnik.util.Transposer;

public class StaticHtmlSongFormatter extends SongFormatter {

    public StaticHtmlSongFormatter() {

    }

    public String formatHtml(Object songText) {
        return formatHtml((SongText) songText);
    }

    public String formatHtml(SongText songText) {
        if (songText.getSections() == null) {
            return formatHtmlChords(songText.getOriginalText());
        }
        StringBuilder html = new StringBuilder();
        for (Section section : songText.getSections()) {
            html.append(formatHtml(section));
        }
        return html.toString();
    }

    private String formatHtml(Section section) {
        StringBuilder html = new StringBuilder();
        html.append("<div class='song-section " + section.getType().css() + "'>");
        html.append(formatHtmlChords(section.getContent()));
        html.append("</div>");
        return html.toString();
    }

    protected String formatHtmlChords(String content) {
        return content.replaceAll(Transposer.CHORD_REGEX, " <a class='chord' href='" + CHORD_URL_PROTOCOL + "$1'><sup>" + "$1" + "</sup></a> ");
    }

}
