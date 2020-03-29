package eu.karelhovorka.zpevnik.text

import org.junit.Test

import java.io.File
import java.io.IOException
import java.util.Scanner

import eu.karelhovorka.zpevnik.text.SectionTokenizer.Companion.TYPE_REGEX
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

class SectionTokenizerTest {

    @Test
    fun testRegex() {
        assertTrue("1.".matches(TYPE_REGEX.toRegex()))
        assertTrue("100.".matches(TYPE_REGEX.toRegex()))
        assertTrue("Recital:".matches(TYPE_REGEX.toRegex()))
        assertTrue("Recital: ".matches(TYPE_REGEX.toRegex()))
    }

    @Test
    fun testIndexVerse() {
        val text = """
1.
sloka1
2.
sloka2
1.
V1:
        """.trimIndent()
        val sections = SectionTokenizer().getSections(text)
        assertEquals(4, sections.size)
        val first = sections.first()
        val beforeLast = sections[2]
        val last = sections.last()
        assertTrue(last.sameContentAndType(first))
        assertTrue(beforeLast.sameContentAndType(first))
    }

    @Test
    fun testIndexChorus() {
        val text = """
R1:
refren1
R2:
refren2
Ref1:
        """.trimIndent()
        val sections = SectionTokenizer().getSections(text)
        assertEquals(3, sections.size)
        val first = sections.first()
        val last = sections.last()
        assertTrue(last.sameContentAndType(first))
        assertEquals("R1:\nrefren1\n", last.content.originalText)
    }

    @Test
    fun testIndexUnknown() {
        val text = """
U1:
unknown1
U2:
unknown2
U1:
        """.trimIndent()
        val sections = SectionTokenizer().getSections(text)
        assertEquals(3, sections.size)
        val first = sections.first()
        val last = sections.last()
        assertTrue(last.sameContentAndType(first))
        assertEquals("U1:\nunknown1\n", last.content.originalText)
    }


    @Test
    fun testValid() {
        assertTrue(SectionTokenizer.isValid("\n\n\nRecital:\n fdsfdsa\nR:\n\nOutro:"))
        assertTrue(SectionTokenizer.isValid("Recital:\n fdsfdsa\nR:\n\nOutro:"))
        assertTrue(SectionTokenizer.isValid("Recital: \n fdsfdsa\nR:\n\nOutro: "))
        assertTrue(SectionTokenizer.isValid("Intro:\n \nR:\n\nOutro:"))
        assertTrue(SectionTokenizer.isValid("R:\n R:\n"))
        assertTrue(SectionTokenizer.isValid("Fdsfds:\nFdfsf:\n\n "))
        assertTrue(SectionTokenizer.isValid("1.\n2.\n"))

        assertFalse(SectionTokenizer.isValid("fdsfds:\n fdsfds:\n"))
        assertFalse(SectionTokenizer.isValid("Intro:\nIntro:"))
        assertFalse(SectionTokenizer.isValid("Intro\nIntro"))
        assertFalse(SectionTokenizer.isValid("\nIntro:\nIntro:"))
    }

    @Test
    @Throws(Exception::class)
    fun testGetSectionFile() {
        val sectionTokenizer = SectionTokenizer()
        val source1 = readFileTokenizer("source1.txt")
        val sectionList = sectionTokenizer.getSections(source1)

        val verse1 = sectionList[0]
        assertEquals(SectionType.VERSE, verse1.type)
        assertEquals("Jak ta zář, co ve tmách usíná,\n" +
                "tak nějak skomírá svíčka na stole\n" +
                "a tvář tvá mi napoví, co nemohla's mi říct, však stačí v očích číst, ty všechno prozradí.",
                verse1.content.text())

        val chorus1 = sectionList[1]
        assertEquals(SectionType.CHORUS, chorus1.type)
        assertEquals(0, chorus1.copyIndex.toLong())
        assertEquals("Na krajíčku pláč, ty slzy v očích máš,\n" +
                "stačilo jen říct pár slov prozatím.\n" +
                "Ta slova dobře znáš, když jdeš spát a usínáš, si opakuješ v snách - zůstaň se mnou dál.\n" +
                "Já ti napovím, jak je vyslovit, co s tím,\n" +
                "abys nebloudila dál sama, jako stín.\n" +
                "Tak poslouchej, chci říct jen pár vět, nic vic. Říkejme si každý sám - zůstaň se mnou dál.",
                chorus1.content.text())

        val verse2 = sectionList[2]
        assertEquals(SectionType.VERSE, verse2.type)
        assertEquals("Němým možná líp se rozumí, ti neumějí lhát, věty poskládat. Slova plynou a ztrácejí se zas, já bych přesto chtěl, jednou, uslyšet tvůj hlas.",
                verse2.content.text())

        val chorus2 = sectionList[3]
        assertEquals(SectionType.CHORUS, chorus2.type)
        assertEquals(1, chorus2.copyIndex.toLong())
        assertEquals(chorus1.content,
                chorus2.content)

        val intermezzo = sectionList[4]
        assertEquals(SectionType.INTERMEZZO, intermezzo.type)
        assertEquals("",
                intermezzo.content.text())

        val chorus3 = sectionList[5]
        assertEquals(SectionType.CHORUS, chorus2.type)
        assertEquals(2, chorus3.copyIndex.toLong())
        assertEquals(chorus1.content,
                chorus3.content)

    }

    @Test
    @Throws(Exception::class)
    fun testSlunecniHrob() {
        val sectionTokenizer = SectionTokenizer()
        val source1 = readFileTokenizer("slunecnihrob.txt")
        val sectionList = sectionTokenizer.getSections(source1)
        SectionTokenizer.validate(source1)
        assertTrue(SectionTokenizer.isValid(source1))
    }

    @Test
    fun testGetSections() {
        assertFirstSectionContentEquals("Tady je intro",
                "Intro:\n" + "Tady je intro")
        assertFirstSectionTypeEquals(SectionType.INTRO, "Intro:\n" + "Tady je intro")
    }

    companion object {

        private fun assertFirstSectionContentEquals(expectedContent: String, sourceText: String) {
            val sc = SectionTokenizer()
            val firstSection = sc.getSections(sourceText)[0]
            assertEquals(expectedContent, firstSection.content.lines[0].chordToText[0].text)
        }

        private fun assertFirstSectionTypeEquals(expectedType: SectionType, sourceText: String) {
            val sc = SectionTokenizer()
            val firstSection = sc.getSections(sourceText)[0]
            assertEquals(expectedType, firstSection.type)
        }


        @Throws(IOException::class)
        fun readFileTokenizer(path: String): String {
            return readFileFromTestResources("/tokenizer/$path")
        }

        @Throws(IOException::class)
        fun readFileFromResources(path: String): String {
            try {
                return readFile("src/main/resources", path)
            } catch (e: IOException) {
                return readFile("chords/src/main/resources", path)
            }

        }

        @Throws(IOException::class)
        @JvmStatic
        fun readFileFromTestResources(path: String): String {
            try {
                return readFile("src/jvmTest/resources", path)
            } catch (e: IOException) {
                return readFile("chords/src/jvmTest/resources", path)
            }
        }


        @Throws(IOException::class)
        private fun readFile(prefix: String, path: String): String {
            println(path)
            var scanner: Scanner? = null
            val text: String
            try {
                scanner = Scanner(File(prefix + path))
                text = scanner.useDelimiter("\\A").next()
            } finally {
                scanner?.close()
            }
            return text
        }
    }

}
