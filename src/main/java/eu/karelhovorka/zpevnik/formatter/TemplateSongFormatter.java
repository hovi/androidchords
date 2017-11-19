package eu.karelhovorka.zpevnik.formatter;


import java.util.List;

import eu.karelhovorka.zpevnik.text.Section;
import eu.karelhovorka.zpevnik.text.SongText;
import eu.karelhovorka.zpevnik.util.Transposer;

public class TemplateSongFormatter extends SongFormatter {

    private final String mainTemplate;

    private final String chordReplaceTemplate;

    private Object context;

    public TemplateSongFormatter() {
        this(
                "",
                "<a class='chord' href='" + CHORD_URL_PROTOCOL + "$1'>$1</a>"
        );
    }

    public TemplateSongFormatter(String mainTemplate, String chordReplaceTemplate) {
        this.mainTemplate = mainTemplate;
        this.chordReplaceTemplate = chordReplaceTemplate;
    }

    public String formatHtml(SongText songText) {
        if (songText.getSectionList() == null) {
            return formatHtmlChords(songText.getOriginalText());
        }
        context = makeContext(songText.getSectionList());
        return applyTemplate(mainTemplate, context);
    }

    private String applyTemplate(String template, Object context) {
        // what engine use?
        return null;
    }

    private Object makeContext(List<Section> sections) {
        // title
        // song
        // chord_url_protocol:
        // sections
        // song display settings?
        return null;
    }


    public String formatHtmlChords(String content) {
        return content.replaceAll(Transposer.CHORD_REGEX, applyTemplate(chordReplaceTemplate, context));
    }

}
