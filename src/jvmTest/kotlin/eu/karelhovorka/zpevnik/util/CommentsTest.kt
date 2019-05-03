package eu.karelhovorka.zpevnik.util

import org.junit.Assert.*
import org.junit.Test

class CommentsTest {


    @Test
    fun testSingleLineHash() {
        assertEquals("<comment>asdf</comment>", Comments.markComments("#asdf"))
        assertEquals(" #asdf", Comments.markComments(" #asdf"))
    }

    @Test
    fun testMultiLineHash() {
        assertEquals("""
            <comment>asdf</comment>
            <comment>asdf</comment>
        """.trimIndent(), Comments.markComments("""
            #asdf
            #asdf
        """.trimIndent()))

        assertEquals("""
            <comment>asdf</comment>
             #asdf
        """.trimIndent(), Comments.markComments("""
            #asdf
             #asdf
        """.trimIndent()))

        assertEquals("""
             #asdf
            <comment>asdf</comment>
        """.trimIndent(), Comments.markComments("""
             #asdf
            #asdf
        """.trimIndent()))

    }

    @Test
    fun testMultiLineHideHash() {
        assertEquals("""
        """.trimIndent(), Comments.hideComments("""
            #asdf
            #asdf
        """.trimIndent()))

        assertEquals(" #asdf", Comments.hideComments("""
             #asdf
            #asdf
        """.trimIndent()))

        assertEquals(" #asdf", Comments.hideComments("""
            #asdf
             #asdf
        """.trimIndent()))

    }

}
