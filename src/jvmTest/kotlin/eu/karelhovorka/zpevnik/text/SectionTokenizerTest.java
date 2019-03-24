package eu.karelhovorka.zpevnik.text;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static eu.karelhovorka.zpevnik.text.SectionTokenizer.TYPE_REGEX;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SectionTokenizerTest {

    @Test
    public void testRegex() {
        assertTrue("1.".matches(TYPE_REGEX));
        assertTrue("100.".matches(TYPE_REGEX));
        assertTrue("Recital:".matches(TYPE_REGEX));
        assertTrue("Recital: ".matches(TYPE_REGEX));
    }


    @Test
    public void testValid() {
        assertTrue(SectionTokenizer.Companion.isValid("\n\n\nRecital:\n fdsfdsa\nR:\n\nOutro:"));
        assertTrue(SectionTokenizer.Companion.isValid("Recital:\n fdsfdsa\nR:\n\nOutro:"));
        assertTrue(SectionTokenizer.Companion.isValid("Recital: \n fdsfdsa\nR:\n\nOutro: "));
        assertTrue(SectionTokenizer.Companion.isValid("Intro:\n \nR:\n\nOutro:"));
        assertTrue(SectionTokenizer.Companion.isValid("R:\n R:\n"));
        assertTrue(SectionTokenizer.Companion.isValid("Fdsfds:\nFdfsf:\n\n "));
        assertTrue(SectionTokenizer.Companion.isValid("1.\n2.\n"));

        assertFalse(SectionTokenizer.Companion.isValid("fdsfds:\n fdsfds:\n"));
        assertFalse(SectionTokenizer.Companion.isValid("Intro:\nIntro:"));
        assertFalse(SectionTokenizer.Companion.isValid("Intro\nIntro"));
        assertFalse(SectionTokenizer.Companion.isValid("\nIntro:\nIntro:"));
    }

    @Test
    public void testGetSectionFile() throws Exception {
        SectionTokenizer sectionTokenizer = new SectionTokenizer();
        String source1 = readFileTokenizer("source1.txt");
        List<Section> sectionList = sectionTokenizer.getSections(source1);

        Section verse1 = sectionList.get(0);
        assertEquals(SectionType.VERSE, verse1.getType());
        assertEquals("Jak ta zář, co ve tmách usíná,\n" +
                        "tak nějak skomírá svíčka na stole\n" +
                        "a tvář tvá mi napoví, co nemohla's mi říct, však stačí v očích číst, ty všechno prozradí.",
                verse1.getContent().text());

        Section chorus1 = sectionList.get(1);
        assertEquals(SectionType.CHORUS, chorus1.getType());
        assertEquals(0, chorus1.getCopyIndex());
        assertEquals("Na krajíčku pláč, ty slzy v očích máš,\n" +
                        "stačilo jen říct pár slov prozatím.\n" +
                        "Ta slova dobře znáš, když jdeš spát a usínáš, si opakuješ v snách - zůstaň se mnou dál.\n" +
                        "Já ti napovím, jak je vyslovit, co s tím,\n" +
                        "abys nebloudila dál sama, jako stín.\n" +
                        "Tak poslouchej, chci říct jen pár vět, nic vic. Říkejme si každý sám - zůstaň se mnou dál.",
                chorus1.getContent().text());

        Section verse2 = sectionList.get(2);
        assertEquals(SectionType.VERSE, verse2.getType());
        assertEquals("Němým možná líp se rozumí, ti neumějí lhát, věty poskládat. Slova plynou a ztrácejí se zas, já bych přesto chtěl, jednou, uslyšet tvůj hlas.",
                verse2.getContent().text());

        Section chorus2 = sectionList.get(3);
        assertEquals(SectionType.CHORUS, chorus2.getType());
        assertEquals(1, chorus2.getCopyIndex());
        assertEquals(chorus1.getContent(),
                chorus2.getContent());

        Section intermezzo = sectionList.get(4);
        assertEquals(SectionType.INTERMEZZO, intermezzo.getType());
        assertEquals("",
                intermezzo.getContent().text());

        Section chorus3 = sectionList.get(5);
        assertEquals(SectionType.CHORUS, chorus2.getType());
        assertEquals(2, chorus3.getCopyIndex());
        assertEquals(chorus1.getContent(),
                chorus3.getContent());

    }

    @Test
    public void testSlunecniHrob() throws Exception {
        SectionTokenizer sectionTokenizer = new SectionTokenizer();
        String source1 = readFileTokenizer("slunecnihrob.txt");
        List<Section> sectionList = sectionTokenizer.getSections(source1);
        SectionTokenizer.Companion.validate(source1);
        assertTrue(SectionTokenizer.Companion.isValid(source1));
    }

    @Test
    public void testGetSections() {
        assertFirstSectionContentEquals("Tady je intro",
                "Intro:\n" +
                        "Tady je intro");
        assertFirstSectionTypeEquals(SectionType.INTRO, "Intro:\n" +
                "Tady je intro");
    }

    private static void assertFirstSectionContentEquals(String expectedContent, String sourceText) {
        SectionTokenizer sc = new SectionTokenizer();
        Section firstSection = sc.getSections(sourceText).get(0);
        assertEquals(expectedContent, firstSection.getContent().getLines().get(0).getChordToText().get(0).getText());
    }

    private static void assertFirstSectionTypeEquals(SectionType expectedType, String sourceText) {
        SectionTokenizer sc = new SectionTokenizer();
        Section firstSection = sc.getSections(sourceText).get(0);
        assertEquals(expectedType, firstSection.getType());
    }


    public static String readFileTokenizer(String path) throws IOException {
        return readFileFromTestResources("/tokenizer/" + path);
    }

    public static String readFileFromResources(String path) throws IOException {
        try {
            return readFile("src/main/resources", path);
        } catch (IOException e) {
            return readFile("chords/src/main/resources", path);
        }
    }

    public static String readFileFromTestResources(String path) throws IOException {
        try {
            return readFile("src/test/resources", path);
        } catch (IOException e) {
            return readFile("chords/src/test/resources", path);
        }


    }


    private static String readFile(String prefix, String path) throws IOException {
        System.out.println(path);
        Scanner scanner = null;
        String text;
        try {
            scanner = new Scanner(new File(prefix + path));
            text = scanner.useDelimiter("\\A").next();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return text;
    }

}
