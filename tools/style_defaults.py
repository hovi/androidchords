"""Generate the Sass and Kotlin copies of the song style defaults, or check that they are current.

src/resources/templates/style-defaults.json is the only source; the exporter copies it to the server.
"""

import argparse
import json
import re
import sys
from pathlib import Path

SOURCE = "src/resources/templates/style-defaults.json"
SASS = "src/resources/templates/_style-defaults.scss"
KOTLIN = "src/commonMain/kotlin/eu/karelhovorka/zpevnik/text/StyleDefaults.kt"
HEADER = "Generated from style-defaults.json by tools/style_defaults.py, do not edit."

GROUPS = {"colors", "common", "layouts", "symbols", "section_names"}
THEMES = {"light", "dark"}
LAYOUTS = {"legacy", "inline", "upper"}
NAME = re.compile(r"[a-z][a-z-]*")
COLOR = re.compile(r"#[0-9a-f]{6}")
CSS_VALUE = re.compile(r"[0-9a-z.#-]+")


def load(source: Path) -> dict:
    defaults = json.loads((source / SOURCE).read_text(encoding="utf-8"))
    if set(defaults) != GROUPS:
        raise ValueError(f"Expected groups {sorted(GROUPS)}, got {sorted(defaults)}")
    if set(defaults["colors"]) != THEMES:
        raise ValueError(f"Expected colours for {sorted(THEMES)}")
    if not set(defaults["layouts"]) <= LAYOUTS:
        raise ValueError(f"Unknown layout in {sorted(defaults['layouts'])}")
    values = [
        *(("color", v) for theme in defaults["colors"].values() for v in theme.items()),
        *(("css", v) for v in defaults["common"].items()),
        *(("css", v) for layout in defaults["layouts"].values() for v in layout.items()),
        *(("text", v) for v in defaults["symbols"].items()),
        *(("text", v) for v in defaults["section_names"].items()),
    ]
    for kind, (name, value) in values:
        if not NAME.fullmatch(name) or not isinstance(value, str) or not value:
            raise ValueError(f"Invalid default {name!r}: {value!r}")
        if kind == "color" and not COLOR.fullmatch(value):
            raise ValueError(f"Colour {name} must be #rrggbb in lower case: {value!r}")
        if kind == "css" and not CSS_VALUE.fullmatch(value):
            raise ValueError(f"Unexpected CSS value for {name}: {value!r}")
    names = [*defaults["common"], *defaults["symbols"]]
    if len(names) != len(set(names)):
        raise ValueError("A variable is both in common and in symbols")
    return defaults


def quoted(value: str) -> str:
    return '"' + value.replace("\\", "\\\\").replace('"', '\\"') + '"'


def sass(defaults: dict) -> str:
    lines = [f"// {HEADER}"]
    for theme, colors in defaults["colors"].items():
        lines += [f"${theme}-{name}: {value};" for name, value in colors.items()]
    lines += [f"${name}: {value};" for name, value in defaults["common"].items()]
    for layout, values in defaults["layouts"].items():
        lines += [f"${layout}-{name}: {value};" for name, value in values.items()]
    lines += [f"${name}: {quoted(value)};" for name, value in defaults["symbols"].items()]
    return "\n".join(lines) + "\n"


def kotlin_string(value: str) -> str:
    return quoted(value).replace("$", "\\$")


def kotlin_map(values: dict, indent: str) -> str:
    entries = ",\n".join(f"{indent}    {kotlin_string(k)} to {kotlin_string(v)}" for k, v in values.items())
    return f"mapOf(\n{entries}\n{indent})"


def kotlin_nested_map(values: dict, indent: str) -> str:
    entries = ",\n".join(
        f"{indent}    {kotlin_string(k)} to {kotlin_map(v, indent + '    ')}" for k, v in values.items()
    )
    return f"mapOf(\n{entries}\n{indent})"


def kotlin(defaults: dict) -> str:
    return f"""// {HEADER}
package eu.karelhovorka.zpevnik.text

/** Defaults of the song style (CSS variables without the --song- prefix) and the section names by section type. */
object StyleDefaults {{
    val colors: Map<String, Map<String, String>> = {kotlin_nested_map(defaults["colors"], "    ")}

    val common: Map<String, String> = {kotlin_map(defaults["common"], "    ")}

    val layouts: Map<String, Map<String, String>> = {kotlin_nested_map(defaults["layouts"], "    ")}

    val symbols: Map<String, String> = {kotlin_map(defaults["symbols"], "    ")}

    val sectionNames: Map<String, String> = {kotlin_map(defaults["section_names"], "    ")}
}}
"""


def generated(source: Path) -> dict[str, str]:
    defaults = load(source)
    return {SASS: sass(defaults), KOTLIN: kotlin(defaults)}


def stale_files(source: Path) -> list[str]:
    return [
        name
        for name, content in generated(source).items()
        if not (source / name).is_file() or (source / name).read_text(encoding="utf-8") != content
    ]


def main() -> int:
    source = Path(__file__).resolve().parent.parent
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--check", action="store_true", help="Report stale files; do not write them")
    args = parser.parse_args()
    try:
        stale = stale_files(source)
        if args.check:
            if stale:
                sys.stderr.write("Stale, run tools/style_defaults.py:\n" + "".join(f"  {n}\n" for n in stale))
                return 1
        else:
            files = generated(source)
            for name in stale:
                (source / name).write_text(files[name], encoding="utf-8")
                sys.stdout.write(f"Updated {name}\n")
    except (OSError, ValueError) as error:
        sys.stderr.write(f"{error}\n")
        return 1
    sys.stdout.write("Style defaults are synchronized.\n")
    return 0


if __name__ == "__main__":
    sys.exit(main())
