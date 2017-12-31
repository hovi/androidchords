package eu.karelhovorka.zpevnik.music;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IntervalTest {

	@Test
	public void testFromStep() {
		assertEquals(Interval.PERFECT_UNISON, Interval.Companion.of(0));
		assertEquals(Interval.PERFECT_UNISON, Interval.Companion.of(12));
		assertEquals(Interval.PERFECT_UNISON, Interval.Companion.of(-12));
	}

}
