package eu.karelhovorka.zpevnik.formatter;


import eu.karelhovorka.zpevnik.text.SectionTokenizer;
import eu.karelhovorka.zpevnik.text.SongText;

public abstract class SongFormatter {
    public static final String CHORD_URL_PROTOCOL = "chord://";

    public abstract String formatHtml(SongText context);

    protected abstract String formatHtmlChords(String content);

    public static boolean isValidSectionFormat(String content) {
        return SectionTokenizer.isValid(content);
    }
}
