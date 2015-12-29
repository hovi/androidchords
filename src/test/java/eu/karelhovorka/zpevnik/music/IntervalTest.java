package eu.karelhovorka.zpevnik.music;

import static org.junit.Assert.*;

import org.junit.Test;

public class IntervalTest {

	@Test
	public void testFromStep() {
		assertEquals(Interval.PERFECT_UNISON, Interval.fromStep(0));
		assertEquals(Interval.PERFECT_UNISON, Interval.fromStep(12));
		assertEquals(Interval.PERFECT_UNISON, Interval.fromStep(-12));
	}

}
