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

        assertEquals(6, sections.size)
        assertEquals(SectionType.VERSE, lastSection.type)
        assertEquals(0, lastSection.index)
        assertEquals(" (text 1.sloky)", lastSection.content.text() )
    }


}
