package eu.karelhovorka.zpevnik.music

import kotlin.jvm.JvmStatic

enum class ToneMod(val value: Int) {
    DUR(1), MOL(0), UNKNOWN(-1);


    companion object {
        @JvmStatic
        fun fromInt(value: Int): ToneMod {
            for (mod in values()) {
                if (mod.value == value) {
                    return mod
                }
            }
            return UNKNOWN
        }

        fun fromString(text: String): ToneMod {
            return when (text.lowercase()) {
                "dur", "maj", "major", "0" -> {
                    DUR
                }
                "mol", "mi", "min", "minor", "1" -> {
                    MOL
                }
                else -> {
                    UNKNOWN
                }
            }
        }
    }
}