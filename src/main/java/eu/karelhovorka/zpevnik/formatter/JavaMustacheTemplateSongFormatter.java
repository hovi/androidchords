package eu.karelhovorka.zpevnik.formatter;


//import com.github.mustachejava.DefaultMustacheFactory;
//import com.github.mustachejava.Mustache;
//import com.github.mustachejava.MustacheFactory;

import eu.karelhovorka.zpevnik.text.SongText;

//import com.samskivert.mustache.Mustache;

public class JavaMustacheTemplateSongFormatter extends MustacheTemplateSongFormatter {

    protected JavaMustacheTemplateSongFormatter(String mainTemplate, String chordReplaceTemplate) {
        super(mainTemplate, chordReplaceTemplate);
    }

    public static MustacheTemplateSongFormatter fromTemplates(String mainTemplate, String chordReplaceTemplate) {
        return new JavaMustacheTemplateSongFormatter(mainTemplate, chordReplaceTemplate);
    }

    @Override
    protected String applyTemplate(String template, SongText context) {
        //StringWriter writer = new StringWriter();
        //MustacheFactory mf = new DefaultMustacheFactory();
        //Mustache mustache = mf.compile(new StringReader(template), "main");
        //mustache.execute(writer, context);
        //writer.flush();
        return null;
    }
}
