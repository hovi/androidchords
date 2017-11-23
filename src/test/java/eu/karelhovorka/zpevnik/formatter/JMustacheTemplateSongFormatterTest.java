package eu.karelhovorka.zpevnik.formatter;

import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;

import eu.karelhovorka.zpevnik.text.SectionTokenizerTest;
import eu.karelhovorka.zpevnik.text.SongDisplaySettings;
import eu.karelhovorka.zpevnik.text.SongText;

import static eu.karelhovorka.zpevnik.text.SectionTokenizerTest.readFileFromTestResources;
import static org.junit.Assert.assertEquals;

public class JMustacheTemplateSongFormatterTest {
    @Test
    public void formatHtml() throws Exception {
        MustacheTemplateSongFormatter formatter = JMustacheTemplateSongFormatter.fromTemplates("nothing", "chords");
        String result = formatter.formatHtml(new SongText("", "", SongDisplaySettings.DEFAULT));
        assertEquals(result, "nothing");
    }

    @Test
    public void formatHtmlResources() throws Exception {


        MustacheTemplateSongFormatter formatter = JMustacheTemplateSongFormatter.fromTemplates(
                SectionTokenizerTest.readFileFromResources("/templates/legacy/main.html"),
                SectionTokenizerTest.readFileFromResources("/templates/legacy/chords.html")
        );
        String result = formatter.formatHtml(new SongText(readFileFromTestResources("/tokenizer/slunecnihrob.txt"), "Sluneční hrob", SongDisplaySettings.DEFAULT));

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