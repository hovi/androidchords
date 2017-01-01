package eu.karelhovorka.zpevnik.util;


public class ChordDetector {

    public static boolean hasBracketChords(String content) {
        if (content == null) {
            throw new NullPointerException("Content cannot be null!");
        }
        String lines[] = content.split("[\n\r]");
        for (String line : lines) {
            if (hasBracketChordsInline(line)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasPlainChords(String content) {
        if (content == null) {
            throw new NullPointerException("Content cannot be null!");
        }

        String lines[] = content.split("[\n\r]");
        for (String line : lines) {
            if (hasPlainChordsInline(line)) {
                return true;
            }
        }
        return false;
    }

    public static String replacePlainChords(String content) {
        if (content == null) {
            throw new NullPointerException("Content cannot be null!");
        }
        StringBuilder sb = new StringBuilder();
        String lines[] = content.split("[\n\r]");
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
        if (content == null) {
            throw new NullPointerException("Content cannot be null!");
        }
        return content.replaceAll("(" + Transposer.FULL_CHORD + ")", "[$1]");
    }

    private static boolean hasPlainChordsInline(String line) {
        if (line == null) {
            throw new NullPointerException("line cannot be null!");
        }
        String result = (line + " ").replaceAll(Transposer.FULL_CHORD + "[\\s\\-,]", " ");
        result = result.replaceAll("[,-]\\s", " ");
        result = result.replaceAll("[0-9]x\\s", " ");
        return result.trim().length() == 0 && line.trim().length() > 0;
    }

    private static boolean hasBracketChordsInline(String line) {
        if (line == null) {
            throw new NullPointerException("line cannot be null!");
        }
        String result = (line + " ").replaceAll("\\[" + Transposer.FULL_CHORD + "\\]", " ");
        result = result.replaceAll("[,-]\\s", " ");
        result = result.replaceAll("[0-9]x\\s", " ");
        return result.trim().length() == 0 && line.trim().length() > 0;
    }

}
