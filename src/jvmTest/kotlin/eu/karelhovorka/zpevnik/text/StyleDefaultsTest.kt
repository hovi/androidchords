package eu.karelhovorka.zpevnik.text

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import eu.karelhovorka.zpevnik.formatter.HtmlSnapshots
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/** StyleDefaults.kt and _style-defaults.scss are generated from style-defaults.json by tools/style_defaults.py. */
class StyleDefaultsTest {

    private val templates = File(HtmlSnapshots.root(), "src/resources/templates")
    private val json = JsonParser.parseString(File(templates, "style-defaults.json").readText()).asJsonObject

    private fun JsonObject.strings(): Map<String, String> = entrySet().associate { it.key to it.value.asString }

    private fun JsonObject.nested(): Map<String, Map<String, String>> = entrySet().associate { it.key to it.value.asJsonObject.strings() }

    @Test
    fun kotlinDefaultsMatchTheJson() {
        assertEquals(json.getAsJsonObject("colors").nested(), StyleDefaults.colors)
        assertEquals(json.getAsJsonObject("common").strings(), StyleDefaults.common)
        assertEquals(json.getAsJsonObject("layouts").nested(), StyleDefaults.layouts)
        assertEquals(json.getAsJsonObject("symbols").strings(), StyleDefaults.symbols)
        assertEquals(json.getAsJsonObject("section_names").strings(), StyleDefaults.sectionNames)
    }

    @Test
    fun sassDefaultsMatchTheJson() {
        val sass = File(templates, "_style-defaults.scss").readText()
        val expected = StyleDefaults.colors.flatMap { (theme, colors) -> colors.map { "\$$theme-${it.key}: ${it.value};" } } +
                StyleDefaults.common.map { "\$${it.key}: ${it.value};" } +
                StyleDefaults.layouts.flatMap { (layout, values) -> values.map { "\$$layout-${it.key}: ${it.value};" } } +
                StyleDefaults.symbols.map { "\$${it.key}: \"${it.value}\";" }
        for (line in expected) {
            assertTrue(line, line in sass.lines())
        }
        assertEquals(expected.size, sass.lines().count { it.startsWith("$") })
    }
}
