package eu.karelhovorka.zpevnik.text;


import java.util.List;

import eu.karelhovorka.zpevnik.util.SongSettingsFormatter;

public class SongText {

    private final String originalText;

    private final List<Section> sectionList;

    public SongText(String originalText) {
        this.originalText = originalText;
        if (SectionTokenizer.isValid(originalText)) {
            SectionTokenizer sectionTokenizer = new SectionTokenizer();
            this.sectionList = sectionTokenizer.getSections(originalText);
        } else {
            this.sectionList = null;
        }
    }


    public  static SongText fromRawText(String originalText, SongDisplaySettings songDisplaySettings) {
        String modifiedText = SongSettingsFormatter.modifyTextBasedOnSettings(originalText, songDisplaySettings);
        return new SongText(modifiedText);
    }

    public String getOriginalText() {
        return originalText;
    }

    public List<Section> getSectionList() {
        return sectionList;
    }

}
