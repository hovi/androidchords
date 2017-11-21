package eu.karelhovorka.zpevnik.text;


import java.util.List;

import eu.karelhovorka.zpevnik.util.SongSettingsFormatter;

import static eu.karelhovorka.zpevnik.util.Preconditions.checkNotNull;

public class SongText {

    private final String originalText;

    private final List<Section> sections;

    public final String title;

    private final SongDisplaySettings displaySettings;

    public SongText(String originalText, String title, SongDisplaySettings songDisplaySettings) {
        checkNotNull(originalText, "originalText");
        checkNotNull(title, "title");
        checkNotNull(songDisplaySettings, "songDisplaySettings");
        this.title = title;
        this.displaySettings = songDisplaySettings;
        this.originalText = originalText;
        if (SectionTokenizer.isValid(originalText)) {
            SectionTokenizer sectionTokenizer = new SectionTokenizer();
            List<Section> tmpList = null;
            try {
                tmpList = sectionTokenizer.getSections(originalText);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.sections = tmpList;
        } else {
            this.sections = null;
        }
    }


    public static SongText fromRawText(String originalText, String title, SongDisplaySettings songDisplaySettings) {
        String modifiedText = SongSettingsFormatter.modifyTextBasedOnSettings(originalText, songDisplaySettings);
        return new SongText(modifiedText, title, songDisplaySettings);
    }

    public String getOriginalText() {
        return originalText;
    }

    public List<Section> getSections() {
        return sections;
    }

    @Override
    public String toString() {
        return "SongText{" +
                "sections=" + sections +
                ", title='" + title + '\'' +
                ", displaySettings=" + displaySettings +
                '}';
    }
}
