package eu.karelhovorka.zpevnik.text;


import java.util.Locale;

import eu.karelhovorka.zpevnik.util.Preconditions;

public enum SectionType {
    INTRO("I:", "Intro:"),
    CHORUS("R([0-9]*):", "Chorus:", "Refren:"),
    VERSE("S([0-9]*):", "Sloka:", "V:", "([0-9]+)\\."),
    BRIDGE("B:", "Bridge:"),
    NOTE("Note:", "Poznamka:"),
    SOLO("Solo:"),
    INTERMEZZO("Mezihra:"),
    OUTRO("Outro:"),
    SPEECH("Recital:"),
    UNKNOWN("");

    public final String names[];

    public String css() {
        return toString().toLowerCase(Locale.FRANCE);
    }

    SectionType(String... names) {
        Preconditions.checkArgument(names.length > 0, "at least 1 name must exist");
        this.names = names;
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
}
