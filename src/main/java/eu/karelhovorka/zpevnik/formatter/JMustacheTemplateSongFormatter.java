package eu.karelhovorka.zpevnik.formatter;


import com.samskivert.mustache.Mustache;

import eu.karelhovorka.zpevnik.text.SongText;

public class JMustacheTemplateSongFormatter extends MustacheTemplateSongFormatter {

    protected JMustacheTemplateSongFormatter(String mainTemplate, String chordReplaceTemplate) {
        super(mainTemplate, chordReplaceTemplate);
    }

    public static MustacheTemplateSongFormatter fromTemplates(String mainTemplate, String chordReplaceTemplate) {
        return new JMustacheTemplateSongFormatter(mainTemplate, chordReplaceTemplate);
    }

    @Override
    protected String applyTemplate(String template, SongText context) {
        //TODO: pre-compile and reuse template
        return Mustache.compiler().compile(template).execute(context);
    }
}
