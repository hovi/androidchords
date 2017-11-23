package eu.karelhovorka.zpevnik.formatter;


import eu.karelhovorka.zpevnik.text.SongText;
import eu.karelhovorka.zpevnik.util.Transposer;

public abstract class MustacheTemplateSongFormatter extends SongFormatter {

    protected final String mainTemplate;

    protected final String chordReplaceTemplate;

    protected SongText context;

    protected MustacheTemplateSongFormatter(String mainTemplate, String chordReplaceTemplate) {
        this.mainTemplate = mainTemplate;
        this.chordReplaceTemplate = chordReplaceTemplate;
    }

    public String formatHtml(SongText context) {
        this.context = context;
        return formatHtmlChords(applyTemplate(mainTemplate, context));
    }

    protected abstract String applyTemplate(String template, SongText context);

    public String formatHtmlChords(String content) {
        return content.replaceAll(Transposer.CHORD_REGEX, applyTemplate(chordReplaceTemplate, context));
    }

}
