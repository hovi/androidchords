"""Focused export checks; no Gradle, npm, database, or Android SDK required."""

import contextlib
import io
import json
import shutil
import tempfile
import unittest
from pathlib import Path
from unittest.mock import patch

import export_assets
from verify_assets import MANIFEST, sha256, verify


class ExportAssetsTest(unittest.TestCase):
    def setUp(self) -> None:
        self.temporary = tempfile.TemporaryDirectory()
        self.addCleanup(self.temporary.cleanup)
        self.root = Path(self.temporary.name)
        self.android = self.root / "android"
        self.server = self.root / "server"
        self.source = self.android / "chords"
        (self.android / "Zpevnik/assets/templates").mkdir(parents=True)
        (self.server / "zpevnik/webapp/templates/inc/song-templates").mkdir(
            parents=True
        )
        for relative in (
            "build.gradle",
            "settings.gradle",
            "gradle.properties",
            "gradlew",
            "gradle/wrapper/gradle-wrapper.properties",
            "gradle/wrapper/gradle-wrapper.jar",
        ):
            self.put(self.android / relative, "build configuration")
        self.put(self.source / "build.gradle", "chords build")
        self.put(self.source / "src/commonMain/kotlin/Song.kt", "song source")
        self.put(self.source / "src/resources/templates/style.scss", "style source")
        for name in ("export_assets.py", "verify_assets.py"):
            path = self.source / "tools" / name
            path.parent.mkdir(parents=True, exist_ok=True)
            shutil.copyfile(Path(__file__).with_name(name), path)
        for layout in export_assets.LAYOUTS:
            self.put(
                self.source / f"src/resources/templates/{layout}/main.html",
                f"<html>\n<!-- @formatter:off -->\n<div>{layout} {{{{text}}}}</div>\n<!-- @formatter:on -->\n</html>",
            )
            self.put(
                self.source / f"src/resources/templates/{layout}/chords.html",
                "<a>$1</a>",
            )
        for path, assets in export_assets.CACHE_TAGS.items():
            self.put(
                self.server / path,
                "Unrelated content\n"
                + "\n".join(
                    '<link href="{% static "' + asset + '" %}?v=3">' for asset in assets
                ),
            )
        self.build = self.enterContext(
            patch.object(
                export_assets, "build_assets", return_value=(b"bundle", b"css")
            )
        )
        self.revision = self.enterContext(
            patch.object(export_assets, "git_revision", return_value="a" * 40)
        )

    @staticmethod
    def put(path: Path, text: str) -> None:
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_text(text, encoding="utf-8")

    def run_export(self, *, check: bool = False) -> int:
        with (
            contextlib.redirect_stdout(io.StringIO()),
            contextlib.redirect_stderr(io.StringIO()),
        ):
            return export_assets.export(
                self.source, self.android, self.server, check=check
            )

    def consumer_bytes(self) -> dict[str, bytes]:
        return {
            path.relative_to(self.root).as_posix(): path.read_bytes()
            for path in self.root.rglob("*")
            if path.is_file() and not path.is_relative_to(self.source)
        }

    def test_export_then_check_is_clean_and_keeps_provenance_after_commit(self) -> None:
        self.assertEqual(0, self.run_export())
        before = self.consumer_bytes()
        self.revision.return_value = "b" * 40
        self.assertEqual(0, self.run_export(check=True))
        self.assertEqual(before, self.consumer_bytes())
        self.assertEqual([], verify(self.android))
        self.assertEqual([], verify(self.server))
        self.assertEqual(
            "{% verbatim %}\n<div>upper {{text}}</div>\n{% endverbatim %}\n",
            (
                self.server / "zpevnik/webapp/templates/inc/song-templates/upper.html"
            ).read_text(),
        )

    def test_check_reports_manual_changes_without_writing_any_files(self) -> None:
        self.run_export()
        self.put(
            self.android / "Zpevnik/assets/templates/print/chords.html", "manual edit"
        )
        self.put(self.server / "zpevnik/assets/js/chords.js", "stale bundle")
        before = self.consumer_bytes()
        self.assertEqual(1, self.run_export(check=True))
        self.assertEqual(before, self.consumer_bytes())
        self.assertTrue(verify(self.android))
        self.assertTrue(verify(self.server))

    def test_check_detects_dirty_source_even_if_build_output_did_not_change(
        self,
    ) -> None:
        self.run_export()
        self.put(self.source / "src/commonMain/kotlin/Song.kt", "uncommitted change")
        before = self.consumer_bytes()
        self.assertEqual(1, self.run_export(check=True))
        self.assertEqual(before, self.consumer_bytes())

    def test_cache_versions_follow_asset_bytes_and_unrelated_template_edits_survive(
        self,
    ) -> None:
        self.run_export()
        path = self.server / "zpevnik/templates/admin/base.html"
        path.write_text(path.read_text() + "\nNew admin content")
        self.assertEqual(0, self.run_export(check=True))
        self.build.return_value = (b"new bundle", b"new css")
        self.run_export()
        self.assertIn("?v=" + sha256(b"new bundle")[:16], path.read_text())
        self.assertIn("?v=" + sha256(b"new css")[:16], path.read_text())
        self.assertIn("New admin content", path.read_text())
        self.assertEqual([], verify(self.server))
        path.write_text(path.read_text().replace("?v=", "?v=wrong-"))
        self.assertTrue(verify(self.server))

    def test_missing_template_marker_or_cache_tag_aborts_before_writes(self) -> None:
        self.run_export()
        self.put(
            self.source / "src/resources/templates/upper/main.html", "missing markers"
        )
        before = self.consumer_bytes()
        with self.assertRaisesRegex(ValueError, "formatter markers"):
            self.run_export()
        self.assertEqual(before, self.consumer_bytes())
        self.put(
            self.source / "src/resources/templates/upper/main.html",
            "<!-- @formatter:off -->new<!-- @formatter:on -->",
        )
        self.put(self.server / "zpevnik/webapp/templates/inc/song-scripts.html", "no script")
        before = self.consumer_bytes()
        with self.assertRaisesRegex(ValueError, "Missing Django static tag"):
            self.run_export()
        self.assertEqual(before, self.consumer_bytes())

    def test_source_changes_during_build_abort_before_writes(self) -> None:
        def changed_during_build(*_args: object) -> tuple[bytes, bytes]:
            self.put(
                self.source / "src/commonMain/kotlin/Song.kt", "edited during build"
            )
            return b"bundle", b"css"

        self.build.side_effect = changed_during_build
        before = self.consumer_bytes()
        with self.assertRaisesRegex(ValueError, "Source changed during export"):
            self.run_export()
        self.assertEqual(before, self.consumer_bytes())

    def test_manifest_paths_cannot_escape_consumer_root(self) -> None:
        self.run_export()
        path = self.server / MANIFEST
        manifest = json.loads(path.read_text())
        manifest["files"] = {"../outside": "anything"}
        path.write_text(json.dumps(manifest))
        with self.assertRaisesRegex(ValueError, "Invalid asset path"):
            verify(self.server)


if __name__ == "__main__":
    unittest.main()
