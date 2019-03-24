package eu.karelhovorka.zpevnik.music
import kotlin.jvm.JvmStatic

enum class ToneMod(val value: Int) {
    DUR(0), MOL(1), UNKNOWN(-1);


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
    }
}