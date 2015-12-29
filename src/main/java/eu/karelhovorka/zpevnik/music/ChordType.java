package eu.karelhovorka.zpevnik.music;

import java.util.List;

import eu.karelhovorka.zpevnik.util.Tone;

public enum ChordType {
	DUR(new String[]{"", "dur"}, Interval.MAJOR_THIRD.step, Interval.PERFECT_FIFTH.step),
	MOL(new String[]{"mol", "mi", "m"}, Interval.MAJOR_THIRD.step, Interval.PERFECT_FIFTH.step);
	
	
	private ChordType(String[] aliases, int... intervals) {
		
	}
	
	private List<Integer> intervals;
}
