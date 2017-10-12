package eu.karelhovorka.zpevnik.util;

import static eu.karelhovorka.zpevnik.util.Preconditions.checkNotNull;

public class StringUtil {

    public static String removeTrailingWhitespace(String text) {
        checkNotNull(text, "text");
        return text.replaceAll("\\s+$", "");
    }

}
