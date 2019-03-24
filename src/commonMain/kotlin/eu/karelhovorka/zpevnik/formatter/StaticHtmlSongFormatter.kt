package eu.karelhovorka.zpevnik.formatter


import eu.karelhovorka.zpevnik.text.Section
import eu.karelhovorka.zpevnik.text.SongText
import eu.karelhovorka.zpevnik.text.section.SectionText
import eu.karelhovorka.zpevnik.util.Transposer

class StaticHtmlSongFormatter : SongFormatter() {

    override fun formatHtml(songText: SongText): String {
        val html = StringBuilder()
        for (section in songText.sections) {
            html.append(formatHtml(section))
        }
        return html.toString()
    }

    private fun formatHtml(section: Section): String {
        val html = StringBuilder()
        html.append("<div class='song-section " + section.type.css() + "'>")
        html.append(formatHtmlChords(formatSectionText(section.content)))
        html.append("</div>")
        return html.toString()
    }

    private fun formatSectionText(sectionText: SectionText): String {
        val html = StringBuilder()
        html.append("<div class='song-text'>")
        for (line in sectionText.lines) {
            html.append("<div class='song-text-line'>")
            for (chordPair in line.chordToText) {
                html.append("<div class='chord-wrap'>${chordPair.chord}</div><div class='text-wrap'>${chordPair.text}</div>")

            }
            html.append("</div>")
        }
        html.append("</div>")
        return html.toString()
    }

    override fun formatHtmlChords(content: String): String {
        return content.replace(Transposer.CHORD_REGEX.toRegex(), " <a class='chord' href='" + SongFormatter.CHORD_URL_PROTOCOL + "$1'><sup>" + "$1" + "</sup></a> ")
    }

}
