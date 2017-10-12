package eu.karelhovorka.zpevnik.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static eu.karelhovorka.zpevnik.util.Preconditions.checkNotNull;

public class ChordDetector {


    public static boolean hasBracketChords(String content) {
        checkNotNull(content, "content");
        String lines[] = content.split("[\n\r]");
        for (String line : lines) {
            if (hasBracketChordsInline(line)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasPlainChords(String content) {
        checkNotNull(content, "content");
        String lines[] = content.split("[\n\r]");
        for (String line : lines) {
            if (hasPlainChordsInline(line)) {
                return true;
            }
        }
        return false;
    }

    public static String replacePlainChords(String content) {
        checkNotNull(content, "content");
        StringBuilder sb = new StringBuilder();
        String lines[] = content.split("\n");
        for (String line : lines) {
            if (hasPlainChordsInline(line)) {
                sb.append(replacePlainChordsInline(line) + "\n");
            } else {
                sb.append(line + "\n");
            }
        }
        return sb.toString().replaceFirst("\\s++$", "");

    }

    private static String replacePlainChordsInline(String content) {
        checkNotNull(content, "content");
        return content.replaceAll("(" + Transposer.FULL_CHORD + ")", "[$1]");
    }

    private static boolean hasPlainChordsInline(String line) {
        checkNotNull(line, "line");
        String result = (line + " ").replaceAll(Transposer.FULL_CHORD + "[\\s\\-,]", " ");
        result = result.replaceAll("[,-]\\s", " ");
        result = result.replaceAll("[0-9]x\\s", " ");
        return result.trim().length() == 0 && line.trim().length() > 0;
    }

    private static boolean hasBracketChordsInline(String line) {
        checkNotNull(line, "line");
        String result = (line + " ").replaceAll("\\[" + Transposer.FULL_CHORD + "\\]", " ");
        result = result.replaceAll("[,-]\\s", " ");
        result = result.replaceAll("[0-9]x\\s", " ");
        return result.trim().length() == 0 && line.trim().length() > 0;
    }

    public static String[] getBracketChordsInLine(String line) {
        checkNotNull(line, "line");
        Pattern bracketChord =  Pattern.compile("\\[(" + Transposer.FULL_CHORD + ")\\]");
        Matcher matcher = bracketChord.matcher(line);
        List<String> chords = new ArrayList<>();
        while (matcher.find()) {
            chords.add(matcher.group(1));
        }
        return chords.toArray(new String[0]);
    }

    public static boolean lineContainsChords(String line, String[] chords) {
        checkNotNull(line, "line");
        checkNotNull(chords, "chords");
        return Arrays.equals(getBracketChordsInLine(line), chords);
    }

}
