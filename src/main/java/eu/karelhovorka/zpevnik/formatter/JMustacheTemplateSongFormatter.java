package eu.karelhovorka.zpevnik.formatter;


import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.util.Scanner;

import eu.karelhovorka.zpevnik.text.SongText;
import eu.karelhovorka.zpevnik.util.Transposer;

public class JMustacheTemplateSongFormatter extends SongFormatter {

    private final String mainTemplate;

    private final String chordReplaceTemplate;

    private SongText context;

    public JMustacheTemplateSongFormatter() {
        this(
                "",
                "<a class='chord' href='" + CHORD_URL_PROTOCOL + "$1'>$1</a>"
        );
    }

    public static JMustacheTemplateSongFormatter fromResources(String templatePath, String chordTemplatePath) {
        String mainTemplate = new Scanner(JMustacheTemplateSongFormatter.class.getClass().getResourceAsStream(templatePath), "UTF-8").useDelimiter("\\A").next();
        String chordTemplate = new Scanner(JMustacheTemplateSongFormatter.class.getClassLoader().getResourceAsStream(chordTemplatePath), "UTF-8").useDelimiter("\\A").next();
        return new JMustacheTemplateSongFormatter(mainTemplate, chordTemplate);
    }

    public static JMustacheTemplateSongFormatter fromTemplates(String mainTemplate, String chordReplaceTemplate) {
        return new JMustacheTemplateSongFormatter(mainTemplate, chordReplaceTemplate);
    }

    private JMustacheTemplateSongFormatter(String mainTemplate, String chordReplaceTemplate) {
        this.mainTemplate = mainTemplate;
        this.chordReplaceTemplate = chordReplaceTemplate;
    }

    public String formatHtml(SongText context) {
        return formatHtmlChords(applyTemplate(mainTemplate, context));
    }

    private String applyTemplate(String template, SongText context) {
        Template tmpl = Mustache.compiler().compile(template);
        System.out.println(context);
        String result = tmpl.execute(context);
        System.out.println(result);
        return result;
    }

    public String formatHtmlChords(String content) {
        return content.replaceAll(Transposer.CHORD_REGEX, applyTemplate(chordReplaceTemplate, context));
    }

}
