package eu.karelhovorka.zpevnik.music

import eu.karelhovorka.zpevnik.util.Tone
import kotlin.test.Test
import kotlin.test.assertEquals


class ChordTest {

    @Test
    fun fromString() {
        assertEquals(
                Chord(
                        tone = Tone.C,
                        type = ChordType.MAJOR_SEVENTH
                ),
                Chord.fromString("Cmaj7")
        )
    }
}