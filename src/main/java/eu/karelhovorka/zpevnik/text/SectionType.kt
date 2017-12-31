package eu.karelhovorka.zpevnik.text


import eu.karelhovorka.zpevnik.util.Preconditions
import java.util.*

interface ISectionType {
    fun css(): String
    fun getShortcut(index: Int): String
    fun getLongName(index: Int): String
}

class UnknownSectionType(val name: String): ISectionType {
    override fun css(): String {
        return "unknown-section"
    }

    override fun getShortcut(index: Int): String {
        return if (index == 0) {
            name
        } else  "$name(${index + 1})"
    }

    override fun getLongName(index: Int): String {
        return if (index == 0) {
            name
        } else  "$name(${index + 1})"
    }

}

enum class SectionType private constructor(private val shortcut: String, private val longName: String, vararg names: String) : ISectionType {
    INTRO("I", "Předehra", "I:", "U:", "Intro:", "Předehra:", "Úvod"),
    CHORUS("R", "Refrén", "R([0-9]*):", "Chorus:", "Refren:", "Ref:"),
    VERSE("S", "Sloka", "S([0-9]*):", "Sloka:", "V:", "([0-9]+)\\."),
    BRIDGE("B", "Bridge", "Bridge:", "B:"),
    NOTE("P", "Poznámka", "Note:", "Poznamka:"),
    SOLO("Solo", "Solo", "Solo:"),
    INTERMEZZO("M", "Mezihra", "Mezihra:", "M:"),
    OUTRO("O", "Outro", "Outro:"),
    SPEECH("Rec", "Recitál", "Recital:", "Rec:"),
    UNKNOWN("?", "?", "");

    val names: Array<String>

    override fun css(): String {
        return toString().toLowerCase(Locale.FRANCE)
    }

    init {
        Preconditions.checkArgument(names.size > 0, "at least 1 name must exist")
        this.names = names as Array<String>
    }

    override fun getShortcut(index: Int): String {
        return if (index == 0) {
            shortcut
        } else "$shortcut(${index + 1})"
    }

    override fun getLongName(index: Int): String {
        return if (index == 0) {
            longName
        } else  "$longName(${index + 1})"

    }

    companion object {

        fun fromName(line: String): ISectionType {
            for (sectionType in values()) {
                for (name in sectionType.names) {
                    if (line.trim().matches(name.toRegex())) {
                        return sectionType
                    }
                }
            }
            return UnknownSectionType(line.trim())
        }
    }
}
