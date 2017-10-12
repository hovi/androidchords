package eu.karelhovorka.zpevnik.music;

public enum Interval {
	// @formatter:off
	PERFECT_UNISON(0), //C-C
	MINOR_SECOND(1),
	MAJOR_SECOND(2), //C-D
	MINOR_THIRD(3),
	MAJOR_THIRD(4), //C-E
	PERFECT_FOURTH(5), //C-F
	AUGMENTED_FOURTH(6),
	PERFECT_FIFTH(7), //C-G
	MINOR_SIXTH(8),
	MAJOR_SIXTH(9), //C-A
	MINOR_SEVENTH(10),
	MAJOR_SEVENTH(11) //C-H
	;
    // @formatter:on

	public final int step;

    Interval(int step) {
		this.step = step;
	}

	public static Interval of(int step) {
		return values()[((step % 12) + 12) % 12];
	}

}
