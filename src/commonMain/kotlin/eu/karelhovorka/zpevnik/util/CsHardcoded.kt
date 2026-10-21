package eu.karelhovorka.zpevnik.util

import eu.karelhovorka.zpevnik.text.StyleDefaults

object CsHardcoded : I18N() {
    override fun translate(message: String, vararg args: String): String {
        return StyleDefaults.sectionNames[message.lowercase()] ?: message.also { println(it) }
    }
}
