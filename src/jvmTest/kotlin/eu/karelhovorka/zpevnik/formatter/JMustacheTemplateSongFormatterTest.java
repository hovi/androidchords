package eu.karelhovorka.zpevnik.formatter;

import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;

import eu.karelhovorka.zpevnik.text.SectionTokenizerTest;
import eu.karelhovorka.zpevnik.text.SongDisplaySettings;
import eu.karelhovorka.zpevnik.text.SongText;
import eu.karelhovorka.zpevnik.util.I18N;

import static eu.karelhovorka.zpevnik.text.SectionTokenizerTest.readFileFromTestResources;
import static org.junit.Assert.assertEquals;

public class JMustacheTemplateSongFormatterTest {
    @Test
    public void formatHtml() throws Exception {
        MustacheTemplateSongFormatter formatter = JMustacheTemplateSongFormatter.Companion.fromTemplates("nothing", "chords");
        String result = formatter.formatHtml(new SongText("", "", SongDisplaySettings.DEFAULT, new I18N()));
        assertEquals(result, "nothing");
    }

    @Test
    public void formatHtmlResources() throws Exception {


        MustacheTemplateSongFormatter formatter = JMustacheTemplateSongFormatter.Companion.fromTemplates(
                SectionTokenizerTest.Companion.readFileFromResources("/templates/legacy/main.html"),
                SectionTokenizerTest.Companion.readFileFromResources("/templates/legacy/chords.html")
        );
        String result = formatter.formatHtml(new SongText(readFileFromTestResources("/tokenizer/slunecnihrob.txt"), "Sluneční hrob", SongDisplaySettings.DEFAULT, new I18N()));

        //writeToFile("/Users/Ef/Downloads/hrob.html", result);
        assertEquals(result, readFileFromTestResources("/results/slunecni-hrob-legacy.html"));

    }

    public static void writeToFile(String fileName, String content)
            throws IOException {
        try (PrintWriter out = new PrintWriter(fileName)) {
            out.println(content);
        }
    }

}