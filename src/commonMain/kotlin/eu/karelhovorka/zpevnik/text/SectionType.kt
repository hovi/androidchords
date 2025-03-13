package eu.karelhovorka.zpevnik.text


import eu.karelhovorka.zpevnik.util.Preconditions

interface ISectionType {
    fun css(): String
    fun getShortcut(index: Int): String
    fun getLongName(index: Int): String
    val name: String
}

data class UnknownSectionType(override val name: String): ISectionType {

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

enum class SectionType constructor(private val shortcut: String, private val longName: String, vararg names: String) : ISectionType {
    INTRO("I", "Předehra", "I:", "U:", "Intro:", "Předehra:", "Úvod:"),
    CHORUS("R", "Refrén", "R([0-9]*):", "Chorus([0-9]*):", "Refren([0-9]*)\\.?:", "Ref([0-9]*)\\.?:", "Refrain([0-9]*)\\.?:"),
    VERSE("S", "Sloka", "S([0-9]*):", "Sloka([0-9]*):", "V([0-9]*):", "([0-9]+)\\."),
    BRIDGE("B", "Bridge", "Bridge([0-9]*):", "B([0-9]*):"),
    NOTE("P", "Poznámka", "Note:", "Poznamka:"),
    SOLO("Solo", "Solo", "Solo:"),
    INTERMEZZO("M", "Mezihra", "Mezihra:", "M:", "Intermezzo:"),
    OUTRO("O", "Outro", "Outro:"),
    SPEECH("Rec", "Recitál", "Recital:", "Rec\\.?:"),
    EMPTY("", "");

    val names: Array<out String>

    override fun css(): String {
        return toString().lowercase()
    }

    init {
        this.names = names
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

        fun fromName(line: String?): ISectionType {
            if (line == null || line.isBlank()) {
                return EMPTY
            }
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
