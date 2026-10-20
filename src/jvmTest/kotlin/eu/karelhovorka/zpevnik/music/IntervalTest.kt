package eu.karelhovorka.zpevnik.music

import org.junit.Assert.assertEquals
import org.junit.Test

class IntervalTest {

    @Test
    fun testFromStep() {
        assertEquals(BasicInterval.PERFECT_UNISON, Interval.of(0))
        assertEquals(BasicInterval.PERFECT_UNISON, Interval.of(12))
        assertEquals(BasicInterval.PERFECT_UNISON, Interval.of(-12))
    }
}
