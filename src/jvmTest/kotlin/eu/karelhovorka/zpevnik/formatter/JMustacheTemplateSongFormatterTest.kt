package eu.karelhovorka.zpevnik.formatter

import eu.karelhovorka.zpevnik.text.SectionTokenizerTest.Companion.readFileFromResources
import eu.karelhovorka.zpevnik.text.SectionTokenizerTest.Companion.readFileFromTestResources
import eu.karelhovorka.zpevnik.text.SongDisplaySettings
import eu.karelhovorka.zpevnik.text.SongText
import eu.karelhovorka.zpevnik.util.I18N
import org.junit.Assert.assertEquals
import org.junit.Test

class JMustacheTemplateSongFormatterTest {

    @Test
    fun formatHtml() {
        val formatter = JMustacheTemplateSongFormatter.fromTemplates("nothing", "chords")
        val result = formatter.formatHtml(SongText("", "", SongDisplaySettings.DEFAULT, I18N()))
        assertEquals("nothing", result)
    }

    @Test
    fun formatHtmlResources() {
        val formatter = JMustacheTemplateSongFormatter.fromTemplates(
            readFileFromResources("/templates/legacy/main.html"),
            readFileFromResources("/templates/legacy/chords.html")
        )
        val result = formatter.formatHtml(
            SongText(readFileFromTestResources("/tokenizer/slunecnihrob.txt"), "Sluneční hrob", SongDisplaySettings.DEFAULT, I18N())
        )
        if (System.getenv("UPDATE_GOLDEN") == "1") {
            java.io.File(HtmlSnapshots.root(), "src/jvmTest/resources/results/slunecni-hrob-legacy.html").writeText(result)
        }
        assertEquals(readFileFromTestResources("/results/slunecni-hrob-legacy.html"), result)
    }
}
