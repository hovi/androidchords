"""Checks of the style defaults generator; no Gradle, npm, or Sass required."""

import json
import tempfile
import unittest
from pathlib import Path

import style_defaults

REPOSITORY = Path(__file__).resolve().parent.parent


class StyleDefaultsTest(unittest.TestCase):
    def setUp(self) -> None:
        self.temporary = tempfile.TemporaryDirectory()
        self.addCleanup(self.temporary.cleanup)
        self.source = Path(self.temporary.name)
        self.defaults = json.loads((REPOSITORY / style_defaults.SOURCE).read_text(encoding="utf-8"))

    def write(self, defaults: dict) -> None:
        path = self.source / style_defaults.SOURCE
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_text(json.dumps(defaults, ensure_ascii=False), encoding="utf-8")

    def test_repository_files_are_current(self) -> None:
        self.assertEqual([], style_defaults.stale_files(REPOSITORY))

    def test_changed_source_makes_generated_files_stale(self) -> None:
        self.write(self.defaults)
        for name, content in style_defaults.generated(self.source).items():
            (self.source / name).parent.mkdir(parents=True, exist_ok=True)
            (self.source / name).write_text(content, encoding="utf-8")
        self.assertEqual([], style_defaults.stale_files(self.source))
        self.defaults["common"]["section-gap"] = "1rem"
        self.write(self.defaults)
        self.assertEqual([style_defaults.SASS, style_defaults.KOTLIN], style_defaults.stale_files(self.source))

    def test_symbols_are_quoted_for_sass_and_kotlin(self) -> None:
        self.defaults["symbols"]["repeat-start"] = 'a"b\\$'
        self.write(self.defaults)
        files = style_defaults.generated(self.source)
        self.assertIn('$repeat-start: "a\\"b\\\\$";', files[style_defaults.SASS])
        self.assertIn('"repeat-start" to "a\\"b\\\\\\$"', files[style_defaults.KOTLIN])

    def test_invalid_defaults_are_rejected(self) -> None:
        for change, message in (
            (lambda d: d.update(extra={}), "Expected groups"),
            (lambda d: d["colors"]["light"].update(chord="#0000FF"), "lower case"),
            (lambda d: d["common"].update(gap="1rem; color: red"), "Unexpected CSS value"),
            (lambda d: d["layouts"].update(print={}), "Unknown layout"),
            (lambda d: d["section_names"].update(verse=""), "Invalid default"),
        ):
            defaults = json.loads(json.dumps(self.defaults))
            change(defaults)
            self.write(defaults)
            with self.subTest(message), self.assertRaisesRegex(ValueError, message):
                style_defaults.load(self.source)


if __name__ == "__main__":
    unittest.main()
