package eu.karelhovorka.zpevnik.text;


import java.util.Locale;

import eu.karelhovorka.zpevnik.util.Preconditions;

public enum SectionType {
    INTRO("I", "I:", "Intro:"),
    CHORUS("R", "R([0-9]*):", "Chorus:", "Refren:"),
    VERSE("S", "S([0-9]*):", "Sloka:", "V:", "([0-9]+)\\."),
    BRIDGE("B", "B:", "Bridge:"),
    NOTE("P", "Note:", "Poznamka:"),
    SOLO("Solo", "Solo:"),
    INTERMEZZO("M", "Mezihra:"),
    OUTRO("O", "Outro:"),
    SPEECH("Rec", "Recital:", "Rec:"),
    UNKNOWN("?", "");

    public final String names[];
    private final String shortcut;

    public String css() {
        return toString().toLowerCase(Locale.FRANCE);
    }

    SectionType(String shortcut, String... names) {
        Preconditions.checkArgument(names.length > 0, "at least 1 name must exist");
        this.names = names;
        this.shortcut = shortcut;
    }

    public static SectionType fromName(String line) {
        for (SectionType sectionType : values()) {
            for (String name : sectionType.names) {
                if (line.matches(name)) {
                    return sectionType;
                }
            }
        }
        //return UNKNOWN;
        throw new IllegalArgumentException("line: " + line);
    }

    public String getShortcut(int index) {
        if (index == 0) {
            return shortcut;
        }
        return shortcut + (index + 1);
    }
}
