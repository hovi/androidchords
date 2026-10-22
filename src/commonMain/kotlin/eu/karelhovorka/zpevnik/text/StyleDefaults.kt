// Generated from style-defaults.json by tools/style_defaults.py, do not edit.
package eu.karelhovorka.zpevnik.text

/** Defaults of the song style (CSS variables without the --song- prefix) and the section names by section type. */
object StyleDefaults {
    val colors: Map<String, Map<String, String>> = mapOf(
        "light" to mapOf(
            "background" to "#ffffff",
            "text" to "#000000",
            "chord" to "#0000ff",
            "header-line" to "#000000"
        ),
        "dark" to mapOf(
            "background" to "#000000",
            "text" to "#ffffff",
            "chord" to "#ffd050",
            "header-line" to "#ffd050"
        )
    )

    val common: Map<String, String> = mapOf(
        "section-gap" to "0.75rem",
        "line-height-inline" to "1.5",
        "comment-style" to "italic",
        "header-display" to "block"
    )

    val layouts: Map<String, Map<String, String>> = mapOf(
        "legacy" to mapOf(
            "page-padding" to "0.5rem"
        ),
        "inline" to mapOf(
            "page-padding" to "0",
            "chord-gap" to "0.2rem"
        ),
        "upper" to mapOf(
            "page-padding" to "0",
            "chord-gap" to "0.2em"
        )
    )

    val symbols: Map<String, String> = mapOf(
        "repeat-start" to "‖:",
        "repeat-end" to ":‖",
        "repeat-count" to "×"
    )

    val sectionNames: Map<String, String> = mapOf(
        "intro" to "Předehra",
        "chorus" to "Refrén",
        "verse" to "Sloka",
        "bridge" to "Bridge",
        "note" to "Poznámka",
        "solo" to "Solo",
        "intermezzo" to "Mezihra",
        "outro" to "Outro",
        "speech" to "Recitál"
    )
}
