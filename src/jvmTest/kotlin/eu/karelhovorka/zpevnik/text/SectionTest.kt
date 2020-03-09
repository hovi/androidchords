package eu.karelhovorka.zpevnik.text

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SectionTest {

    @Test
    fun textLine() {
        val text = """
1.
 (text 1.sloky)
2.
 (text 2.sloky)
3.
 (text 3.sloky)
R:
 (text refrénu)
2.
1.
        """.trim()
        val sections = SectionTokenizer().getSections(text)
        val lastSection = sections.last()
        val secondLast = sections[4]

        assertEquals(6, sections.size)
        assertEquals(SectionType.VERSE, lastSection.type)
        assertEquals(0, lastSection.index)
        assertEquals(" (text 1.sloky)", lastSection.content.text() )
        assertEquals(1, secondLast.index)
        assertEquals(" (text 2.sloky)", secondLast.content.text() )
        assertEquals(sections.first().content.text(), lastSection.content.text() )
    }

    @Test
    fun basicRegex() {
        val R = "([0-9]*)\\.".toRegex()
        assertTrue("1.".matches(R))
        assertTrue("1.".matches(R))
        assertTrue("2.".matches(R))
        assertEquals("1", SectionTokenizer.explicitVerseIndex("1."))
    }


}
