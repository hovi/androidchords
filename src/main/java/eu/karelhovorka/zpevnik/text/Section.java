package eu.karelhovorka.zpevnik.text;


import static eu.karelhovorka.zpevnik.util.Preconditions.checkNotNull;

public class Section {

    private final String content;

    private final String[] lines;

    private final SectionType type;

    private final Section copyOf;

    private final int typeIndex;

    public Section(String content, SectionType type) {
        this(content, type, null, 0);
    }

    public Section(String content, SectionType type, Section copyOf, int typeIndex) {
        checkNotNull(content);
        checkNotNull(type);
        this.content = content;
        this.type = type;
        this.copyOf = copyOf;
        this.typeIndex = typeIndex;
        this.lines = content.split("\n");
    }

    public String getContent() {
        return content;
    }

    public SectionType getType() {
        return type;
    }

    public boolean isCopy() {
        return copyOf != null;
    }

    public boolean sameContentAndType(Section another) {
        if (another == null) {
            return false;
        }
        if (type != another.type) {
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

    public String getCss() {
        StringBuilder sb = new StringBuilder();
        sb.append(" section");
        sb.append(" section-type-" + type.css());
        sb.append(" section-type-" + type.css() + typeIndex);
        return sb.toString();
    }

    public String getShortName() {
        return type.getShortcut(typeIndex);
    }

    public Section getCopyOf() {
        return copyOf;
    }

    public int getTypeIndex() {
        return typeIndex;
    }

    public static Section makeCopyOf(Section previousSection) {
        return new Section(previousSection.content, previousSection.type, previousSection, previousSection.typeIndex + 1);
    }

    @Override
    public String toString() {
        return "Section{" +
                "type='" + type + '\'' +
                ", typeIndex=" + typeIndex +
                '}';
    }

    public String[] getLines() {
        return lines;
    }
}
