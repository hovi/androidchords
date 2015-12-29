package eu.karelhovorka.zpevnik.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Transposer {

	public static final String SINGLE_TONE = "[A-Habcdefgh]\\#?b?"; // b

	public static final String TONE_ADDITIONS = "(?:sus|maj|mimaj|add|aug|dim|min|mi|m|b|\\+)?[1-9]{0,2}";

	// private static final String SINGLE_CHORD = "(" + SINGLE_TONE + ")" +
	// TONE_ADDITIONS;

	public static final String FULL_CHORD = "(" + SINGLE_TONE + ")" + TONE_ADDITIONS + "(?:(\\/)(" + SINGLE_TONE + ")" + TONE_ADDITIONS + ")?";
	
	
	// private static final String FULL_CHORD =
	// "([A-H]\\#?)(mi?|sus|maj)?[0-9]?(\\/([A-H]\\#?))?";

	// public static final String CHORD_REGEX = CHORD_1 + CHORD_2 + CHORD_3 +
	// CHORD_4 + CHORD_5 + CHORD_6;

	public static final String CHORD_REGEX = "\\[(" + FULL_CHORD + ")\\]";
	
	private static Pattern multiChord = Pattern.compile("\\[(" + FULL_CHORD + ",?\\s*){2,}\\]");

	public static String getChordRegex(final String tone1, final String tone2) {
		return "\\[" + "(" + tone1 + ")(" + TONE_ADDITIONS + ")((\\/)(" + tone2 + ")((" + TONE_ADDITIONS + ")))" + "\\]";
	}

	public static String getChordRegex(final String tone) {
		return "\\[" + "(" + tone + ")(" + TONE_ADDITIONS + ")" + "\\]";
	}
	
	public static String merge(final String text) {
		final Matcher m = multiChord.matcher(text);
		String result = text;
		if (m.find()) {
			result = text.substring(0, m.start()) + split(m.group().trim().substring(1, m.group().trim().length() - 1)) + text.substring(m.end());
			if (result.equals(text)) {
				return result;
			}
			return merge(result);
		}
		return result;
	}

	public static String removeChords(final String text) {
		return merge(text).replaceAll(CHORD_REGEX, "");
	}

	public static String removeNonChords(String text) {
		StringBuilder result = new StringBuilder();
		Pattern pattern = Pattern.compile(CHORD_REGEX);
		for (String line : text.split("\n")) {
			Matcher m = pattern.matcher(line);
			boolean found = false;
			while (m.find()) {
				result.append(m.group() + " ");
				found = true;
			}
			if (found) {
				result.append("\n");
			}
		}
		return result.toString();
	}

	private static String split(final String group) {
		String result = "";
		for (final String s : group.split("[,\\s]+")) {
			if (s.length() != 0) {
				result += "[" + s + "]";
			}
		}
		return result;
	}

}
