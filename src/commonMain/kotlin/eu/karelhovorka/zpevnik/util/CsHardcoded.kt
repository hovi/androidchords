package eu.karelhovorka.zpevnik.util

object CsHardcoded : I18N() {
    override fun translate(message: String, vararg args: String): String {
        return when (message) {
            "INTRO" -> "Předehra"
            "CHORUS" -> "Refrén"
            "VERSE" -> "Sloka"
            "BRIDGE" -> "Bridge"
            "NOTE" -> "Poznámka"
            "SOLO" -> "Solo"
            "INTERMEZZO" -> "Mezihra"
            "OUTRO" -> "Outro"
            "SPEECH" -> "Recitál"
            else -> {
                println(message)
                message
            }

        }
    }
}