package eu.karelhovorka.zpevnik.util

import eu.karelhovorka.zpevnik.music.Interval
import kotlin.jvm.JvmStatic

fun Int.toToneIndex(size: Int = 12): Int {
    return (this % size + size) % size
}

enum class Tone(vararg areaTones: CountrySpecificToneName) {
    // @formatter:off
    C(CountrySpecificToneName("C", CountryCategory.EASTERN, 0, "His", "H#"), CountrySpecificToneName("C", CountryCategory.WESTERN, 0, "Bis", "B#")),
    CIS(CountrySpecificToneName("C#", "Db", "Cis", "Des", CountryCategory.EASTERN, 1), CountrySpecificToneName("C#", "Db", "Cis", "Des", CountryCategory.WESTERN, 1)),
    D(CountrySpecificToneName("D", CountryCategory.EASTERN, 2), CountrySpecificToneName("D", CountryCategory.WESTERN, 2)),
    DIS(CountrySpecificToneName("D#", "Eb", "Dis", "Es", CountryCategory.EASTERN, 3), CountrySpecificToneName("D#", "Eb", "Dis", "Es", CountryCategory.WESTERN, 3)),
    E(CountrySpecificToneName("E", CountryCategory.EASTERN, 4, "Fes", "Fb"), CountrySpecificToneName("E", CountryCategory.WESTERN, 4, "Fes", "Fb")),
    F(CountrySpecificToneName("F", CountryCategory.EASTERN, 5, "Eis", "E#"), CountrySpecificToneName("F", CountryCategory.WESTERN, 5, "Eis", "E#")),
    FIS(CountrySpecificToneName("F#", "Gb", "Fis", "Ges", CountryCategory.EASTERN, 6), CountrySpecificToneName("F#", "Gb", "Fis", "Ges", CountryCategory.WESTERN, 6)),
    G(CountrySpecificToneName("G", CountryCategory.EASTERN, 7), CountrySpecificToneName("G", CountryCategory.WESTERN, 7)),
    GIS(CountrySpecificToneName("G#", "Ab", "Gis", "As", CountryCategory.EASTERN, 8), CountrySpecificToneName("G#", "Ab", "Gis", "As", CountryCategory.WESTERN, 8)),
    A(CountrySpecificToneName("A", CountryCategory.EASTERN, 9, "Bb"), CountrySpecificToneName("A", CountryCategory.WESTERN, 9)),
    AIS(CountrySpecificToneName("A#", "B", "Ais", "B", CountryCategory.EASTERN, 10, "Hb", "Hes"), CountrySpecificToneName("A#", "Bb", "Ais", "Bb", CountryCategory.WESTERN, 10)),
    H(CountrySpecificToneName("H", CountryCategory.EASTERN, 11, "Cb", "H", "Ces"), CountrySpecificToneName("B", CountryCategory.WESTERN, 11, "H", "Cb", "B", "Ces"));


    // @formatter:on
    private val aliases = HashMap<CountryCategory, CountrySpecificToneName>()

    val index: Int

    enum class CountryCategory {
        WESTERN, EASTERN
    }

    enum class ModificationAbbreviation {
        SHARP, FLAT, IS, ES
    }

    class CountrySpecificToneName(sharp: String, flat: String, `is`: String, es: String, val type: CountryCategory, val index: Int, vararg extraAliases: String) {

        val aliases: List<String>

        private var names: Map<ModificationAbbreviation, String>

        constructor(name: String, type: CountryCategory, index: Int, vararg extraAliases: String) : this(name, name, name, name, type, index, *extraAliases) {}

        init {
            names = mapOf(
                    ModificationAbbreviation.SHARP to sharp,
                    ModificationAbbreviation.FLAT to flat,
                    ModificationAbbreviation.IS to `is`,
                    ModificationAbbreviation.ES to es
            )
            val buildAliases = mutableListOf<String>()
            buildAliases.add(sharp)
            buildAliases.add(flat)
            buildAliases.add(`is`)
            buildAliases.add(es)
            for (alias in extraAliases) {
                buildAliases.add(alias)
            }
            this.aliases = buildAliases
        }

        fun getValue(type: ModificationAbbreviation): String {
            return names[type]!!
        }

    }

    init {
        this.index = areaTones[0].index
        for (at in areaTones) {
            this.aliases.put(at.type, at)
        }
    }

    fun transpose(step: Interval): Tone {
        val c = this
        val index = (c.index + step.step + values().size) % values().size
        return values()[index]
    }

    fun getAreaTone(type: CountryCategory): CountrySpecificToneName {
        return aliases[type]!!
    }

    fun getValue(type: CountryCategory, halfToneType: ModificationAbbreviation): String {
        return this.aliases[type]!!.getValue(halfToneType)
    }

    companion object {

        @JvmStatic
        fun fromString(string: String, type: CountryCategory = CountryCategory.WESTERN): Tone? {
            return values().find { string in it.aliases[type]!!.aliases }
        }

        @JvmStatic
        fun fromIndexOrNull(index: Int?): Tone? {
            if (index == null) {
                return null
            }
            val normalizedIndex = (index % values().size + values().size) % values().size
            for (t in values()) {
                if (t.index == normalizedIndex) {
                    return t
                }
            }
            return null
        }

        @JvmStatic
        fun fromIndex(index: Int): Tone {
            val normalizedIndex = (index % values().size + values().size) % values().size
            for (t in values()) {
                if (t.index == normalizedIndex) {
                    return t
                }
            }
            throw IllegalArgumentException("Illegal index: $index")
        }
    }

    fun toString(category: CountryCategory): String {
        return getAreaTone(category).aliases.first()
    }

    override fun toString(): String {
        return toString(CountryCategory.WESTERN)
    }
}
