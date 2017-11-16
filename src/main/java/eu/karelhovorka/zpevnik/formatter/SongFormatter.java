package eu.karelhovorka.zpevnik.formatter;


import java.util.List;

import eu.karelhovorka.zpevnik.text.Section;

public interface SongFormatter {
    String CHORD_URL_PROTOCOL = "chord://";

    String formatHtml(List<Section> sections);

    String formatHtmlChords(String content);
}
