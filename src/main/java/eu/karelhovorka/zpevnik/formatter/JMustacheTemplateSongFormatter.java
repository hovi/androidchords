package eu.karelhovorka.zpevnik.formatter;


import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

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
        Template tmpl = Mustache.compiler().compile(template);
        return tmpl.execute(context);
    }
}
