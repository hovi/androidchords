package eu.karelhovorka.zpevnik.text;


import static eu.karelhovorka.zpevnik.util.Preconditions.checkNotNull;

public class Section {

    private final String content;

    private final SectionType sectionType;

    private final Section copyOf;

    private final int typeIndex;

    public Section(String content, SectionType sectionType) {
        this(content, sectionType, null, 0);
    }

    public Section(String content, SectionType sectionType, Section copyOf, int typeIndex) {
        checkNotNull(content);
        checkNotNull(sectionType);
        this.content = content;
        this.sectionType = sectionType;
        this.copyOf = copyOf;
        this.typeIndex = typeIndex;
    }

    public String getContent() {
        return content;
    }

    public SectionType getSectionType() {
        return sectionType;
    }

    public boolean isCopy() {
        return copyOf != null;
    }

    public boolean sameContentAndType(Section another) {
        if (another == null) {
            return false;
        }
        if (sectionType != another.sectionType) {
            return false;
        }
        if (content.equals(another.getContent())) {
            return true;
        }
        if (content.equals("")) {
            return true;
        }
        if (content.endsWith("...") && another.content.startsWith(content.substring(0, content.length() - 3))) {
            return true;
        }
        return false;
    }

    public Section getCopyOf() {
        return copyOf;
    }

    public int getTypeIndex() {
        return typeIndex;
    }

    public static Section makeCopyOf(Section previousSection) {
        return new Section(previousSection.content, previousSection.sectionType, previousSection, previousSection.typeIndex + 1);
    }

    @Override
    public String toString() {
        return "Section{" +
                "sectionType='" + sectionType + '\'' +
                ", typeIndex=" + typeIndex +
                '}';
    }
}
