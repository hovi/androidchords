package eu.karelhovorka.zpevnik.music

enum class IntervalType(val shortcut: Char) {
    AUGMENTED('A'), PERFECT('P'), DIMINISHED('d'), MINOR('m'), MAJOR('M');

    companion object {
        fun fromShortcut(shortcut: Char): IntervalType {
            return values().firstOrNull { it.shortcut == shortcut }
                    ?: error("Could not find interval for shortcut: $shortcut")
        }
    }
}