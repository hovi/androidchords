package eu.karelhovorka.zpevnik.text.section

data class ChordPair(val chord: String?, val text: String) {

    fun css(): String {
        val builder = StringBuilder()
        builder.append("song-chord-pair ")
        if (chord == null) {
            builder.append(" empty-chord ")
        }
        if (isEmpty()) {
            builder.append(" empty-pair ")
        }
        return builder.toString()
    }

    fun htmlChord(): String {
        return chord ?: "&nbsp;"
    }

    fun withoutBrackets(): String {
        return htmlChord().replace("[", "").replace("]", "")
    }

    fun isEmpty(): Boolean {
        return chord == null && text.isEmpty()
    }
}