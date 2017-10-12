package eu.karelhovorka.zpevnik.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.karelhovorka.zpevnik.music.Interval;
import eu.karelhovorka.zpevnik.util.Tone.ModificationAbbreviation;
import eu.karelhovorka.zpevnik.util.Tone.CountryCategory;

public class ToneTransposer {

	private static Pattern TONE_PATTERN = Pattern.compile("\\[(" + Transposer.SINGLE_TONE + ").*");

	private static final Pattern CHORD_REGEX_PATTERN = Pattern.compile("\\[(" + Transposer.FULL_CHORD + ")\\]");

	private CountryCategory type;

	private ModificationAbbreviation htt;

	public ToneTransposer(CountryCategory type, ModificationAbbreviation htt) {
		this.type = type;
		this.htt = htt;
	}

	public String transposeAll(String text, Interval step) {
		return transposeAll(text, step, type, htt);
	}

	public Tone fromChordString(String chord) {
		return fromChordString(chord, type);
	}

	public Tone fromToneString(String tone) {
		return fromToneString(tone, type);
	}

	public String transpose(String tone, Interval step) {
		return transpose(tone, type, htt, step);
	}

	public String normalize(String tone) {
		return normalize(tone, type, htt);
	}

	public static String transposeAll(String text, Interval step, CountryCategory type, ModificationAbbreviation htt) {
		String result = Transposer.merge(text);
		if ((step.step % Tone.values().length) == 0) {
			return result;
		}
		Matcher m = CHORD_REGEX_PATTERN.matcher(result);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			try {
			Tone tone = ToneTransposer.fromChordString("[" + m.group(1) + "]", type);
			if (m.group(5) != null && m.group(4) != null) {
				Tone tone2 = ToneTransposer.fromChordString("[" + m.group(5) + "]", type);
				String tempChord = tone.transpose(step).getValue(type, htt) + m.group(3) + m.group(4) + tone2.transpose(step).getValue(type, htt);
				if (m.group(6) != null) {
					tempChord += m.group(6);
				}
				m.appendReplacement(sb, "[" + tempChord + "]");
			} else {
				if (m.group(6) != null && m.group(4) != null) {
					m.appendReplacement(sb, "[" + tone.transpose(step).getValue(type, htt) + m.group(3) + m.group(4) + m.group(6) + "]");
				} else {
					m.appendReplacement(sb, "[" + tone.transpose(step).getValue(type, htt) + m.group(3) + "]");
				}
			}
			} catch (IllegalArgumentException e) {
				//tone not found, we will not transpose
				m.appendReplacement(sb, m.group(0));
			}
		}
		m.appendTail(sb);
		return sb.toString();
	}

    public static String removeBrackets(String text) {
        return text.replaceAll(Transposer.CHORD_REGEX, "$1");
    }

	public static Tone fromChordString(String chord, CountryCategory type) {
		Matcher matcher = TONE_PATTERN.matcher(chord);
		if (matcher.find()) {
			return fromToneString(matcher.group(1), type);
		}
		throw new IllegalArgumentException("Chord not found: " + chord);
	}

	public static Tone fromToneString(String tone, CountryCategory type) {
		for (Tone cn : Tone.values()) {
			if (cn.getAreaTone(type).getAliases().contains(tone)) {
				return cn;
			}
		}
		throw new IllegalArgumentException("Tone not found: " + tone);
	}

	public static String transpose(String tone, CountryCategory type, ModificationAbbreviation halfToneType, Interval step) {
		Tone c = fromToneString(tone, type);
		return c.transpose(step).getValue(type, halfToneType);
	}

	public static String normalize(String tone, CountryCategory type, ModificationAbbreviation halfToneType) {
		return fromToneString(tone, type).getValue(type, halfToneType);
	}
}
