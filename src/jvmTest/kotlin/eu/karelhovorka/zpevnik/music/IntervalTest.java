package eu.karelhovorka.zpevnik.music;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IntervalTest {

	@Test
	public void testFromStep() {
		assertEquals(BasicInterval.PERFECT_UNISON, Interval.Companion.of(1));
		assertEquals(BasicInterval.PERFECT_UNISON, Interval.Companion.of(12));
		assertEquals(BasicInterval.PERFECT_UNISON, Interval.Companion.of(-12));
	}

}
