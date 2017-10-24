package eu.karelhovorka.zpevnik.text;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static eu.karelhovorka.zpevnik.util.Preconditions.checkNotNull;
import static eu.karelhovorka.zpevnik.util.StringUtil.removeTrailingWhitespace;

public class SectionTokenizer {

    private static final int MINIMUM_SECTION_COUNT = 2;
    private final String splitRegex;

    public static final String TYPE_REGEX = "(?:[A-Z][A-Za-z]*([0-9]*):|([0-9]+)(?:\\.|:))";

    public static final Pattern TYPE_REGEX_PATTERN = Pattern.compile(TYPE_REGEX);

    public static final Pattern VALID_TEXT_PATTERN = Pattern.compile("^" + TYPE_REGEX + "(?:\n|\r).*");

    public SectionTokenizer() {
        //splitRegex = splitRegex();
        splitRegex = "^" + TYPE_REGEX + "$";
    }

    public static boolean isValid(String text) {
        try {
            validate(text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void validate(String text) {
        if (!VALID_TEXT_PATTERN.matcher(text).find()) {
            throw new IllegalStateException("Not starting with section header");
        }
        Matcher m = Pattern.compile(TYPE_REGEX + "(?:\n|\r)").matcher(text);
        int count = 0;
        while (m.find()) {
            count += 1;
        }
        if (count < MINIMUM_SECTION_COUNT) {
            throw new IllegalStateException("not enough sections: " + count);
        }
    }

    private String splitRegex() {
        StringBuilder sb = new StringBuilder();
        for (SectionType sectionType : SectionType.values()) {
            for (String name : sectionType.names) {
                sb.append("|" + name + ":");
            }

        }
        return "(^" + sb.substring(1) + "\n)";
    }

    public List<Section> getSections(String text) {
        checkNotNull(text, "text");
        String[] lines = text.split("(\n|\r)+");
        List<Section> sections = new ArrayList<>();
        SectionType st = null;
        StringBuilder sb = null;
        for (String line : lines) {
            if (line.matches(splitRegex)) {
                if (st != null) {
                    sections.add(new Section(removeTrailingWhitespace(sb.toString()), st));
                }
                st = SectionType.fromName(line);
                sb = new StringBuilder();
            } else {
                if (sb == null) {
                    throw new IllegalArgumentException("Text does not start with a section");
                }
                sb.append(line + "\n");
            }
        }
        if (st != null) {
            sections.add(new Section(removeTrailingWhitespace(sb.toString()), st));
        }
        return mergeSections(sections);
    }

    public List<Section> mergeSections(List<Section> originalSections) {
        List<Section> mergedSections = new ArrayList<>();
        for (Section section : originalSections) {
            Section sectionToAdd = section;
            for (int j = mergedSections.size() - 1; j >= 0; j--) {
                Section previousSection = mergedSections.get(j);
                if (section.sameContentAndType(previousSection)) {
                    sectionToAdd = Section.makeCopyOf(previousSection);
                    break;
                }
            }
            mergedSections.add(sectionToAdd);
        }
        return mergedSections;
    }


}
