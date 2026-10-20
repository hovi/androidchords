"""Export chords assets to Android and Django, or compare them with a fresh build."""

import argparse
import json
import re
import shutil
import subprocess
import sys
import tempfile
from pathlib import Path

from verify_assets import MANIFEST, cache_tag_pattern, sha256

SASS_VERSION = "1.77.8"
LAYOUTS = ("legacy", "inline", "upper", "print")
CACHE_TAGS = {
    "zpevnik/templates/admin/base.html": ("css/chords.css", "js/chords.js"),
    "zpevnik/webapp/templates/app_base.html": ("css/chords.css",),
    "zpevnik/webapp/templates/inc/song-scripts.html": ("js/chords.js",),
}


def git_revision(source: Path) -> str:
    return subprocess.check_output(
        ["git", "-C", str(source), "rev-parse", "HEAD"], text=True
    ).strip()


def input_hash(source: Path) -> str:
    files = {}
    for directory in ("src/commonMain", "src/jsMain", "src/resources/templates"):
        for path in sorted((source / directory).rglob("*")):
            if path.is_file() and not any(
                p.startswith(".") for p in path.relative_to(source).parts
            ):
                files[path.relative_to(source).as_posix()] = sha256(path.read_bytes())
    for relative in (
        "build.gradle",
        "tools/export_assets.py",
        "tools/verify_assets.py",
    ):
        files[relative] = sha256((source / relative).read_bytes())
    for relative in (
        "build.gradle",
        "settings.gradle",
        "gradle.properties",
        "gradlew",
        "gradle/wrapper/gradle-wrapper.properties",
        "gradle/wrapper/gradle-wrapper.jar",
    ):
        files["android-build/" + relative] = sha256(
            (source.parent / relative).read_bytes()
        )
    return sha256(json.dumps(files, sort_keys=True).encode())


def django_fragment(template: bytes) -> bytes:
    text = template.decode("utf-8")
    start, end = "<!-- @formatter:off -->", "<!-- @formatter:on -->"
    if (
        text.count(start) != 1
        or text.count(end) != 1
        or text.index(start) > text.index(end)
    ):
        raise ValueError("Expected exactly one ordered pair of formatter markers")
    body = text.split(start, 1)[1].split(end, 1)[0].strip("\r\n")
    return ("{% verbatim %}\n" + body + "\n{% endverbatim %}\n").encode()


def update_cache_tags(content: bytes, assets: dict[str, str]) -> bytes:
    text = content.decode("utf-8")
    for asset, version in assets.items():
        text, count = cache_tag_pattern(asset).subn(
            lambda match, v=version: match.group("tag") + "?v=" + v, text
        )
        if not count:
            raise ValueError(f"Missing Django static tag for {asset}")
    return text.encode()


def build_assets(source: Path, staging: Path) -> tuple[bytes, bytes]:
    build_root = source.parent
    if source.name != "chords" or not (build_root / "settings.gradle").is_file():
        raise ValueError("Run this from the chords submodule of an Android checkout")
    subprocess.run(
        [
            str(build_root / "gradlew"),
            ":chords:jsBrowserProductionWebpack",
            "--console=plain",
        ],
        cwd=build_root,
        check=True,
    )
    bundle = source / "build/kotlin-webpack/js/productionExecutable/chords.js"
    shutil.copyfile(bundle, staging / "chords.js")
    subprocess.run(
        [
            "npm",
            "exec",
            "--yes",
            f"--package=sass@{SASS_VERSION}",
            "--",
            "sass",
            "--no-source-map",
            "--style=expanded",
            "--no-error-css",
            str(source / "src/resources/templates/style.scss"),
            str(staging / "chords.css"),
        ],
        cwd=staging,
        check=True,
    )
    return (staging / "chords.js").read_bytes(), (staging / "chords.css").read_bytes()


def output_files(
    source: Path, js: bytes, css: bytes
) -> tuple[dict[str, bytes], dict[str, bytes]]:
    verifier = (source / "tools/verify_assets.py").read_bytes()
    android = {
        "Zpevnik/assets/templates/style.css": css,
        "tools/check_chords_assets.py": verifier,
    }
    server = {
        "zpevnik/assets/css/chords.css": css,
        "zpevnik/assets/js/chords.js": js,
        "tools/check_chords_assets.py": verifier,
    }
    for layout in LAYOUTS:
        for filename in ("main.html", "chords.html"):
            content = (
                source / "src/resources/templates" / layout / filename
            ).read_bytes()
            android[f"Zpevnik/assets/templates/{layout}/{filename}"] = content
            if layout != "print" and filename == "main.html":
                server[f"zpevnik/webapp/templates/inc/song-templates/{layout}.html"] = (
                    django_fragment(content)
                )
    return android, server


def manifest_bytes(
    root: Path,
    files: dict[str, bytes],
    versions: dict[str, dict[str, str]],
    fingerprint: str,
    revision: str,
) -> bytes:
    # Keep provenance stable after committing an export or unrelated changes.
    # The input hash, not HEAD equality, determines whether source has changed.
    existing = root / MANIFEST
    if existing.is_file():
        previous = json.loads(existing.read_text(encoding="utf-8"))
        recorded = previous["source"]
        if recorded["inputs_sha256"] == fingerprint:
            revision = recorded["revision"]
    if not re.fullmatch(r"[0-9a-f]{40,64}", revision):
        raise ValueError("Invalid source revision in asset manifest")
    manifest = {
        "schema_version": 1,
        "source": {
            "repository": "https://github.com/hovi/androidchords.git",
            "revision": revision,
            "inputs_sha256": fingerprint,
        },
        "build": {"sass": SASS_VERSION},
        "files": {name: sha256(data) for name, data in sorted(files.items())},
        "cache_versions": versions,
    }
    return (json.dumps(manifest, indent=2, sort_keys=True) + "\n").encode()


def changed_files(root: Path, expected: dict[str, bytes]) -> list[str]:
    return [
        name
        for name, data in sorted(expected.items())
        if not (root / name).is_file() or (root / name).read_bytes() != data
    ]


def write_files(root: Path, expected: dict[str, bytes], names: list[str]) -> None:
    for name in names:
        path = root / name
        path.parent.mkdir(parents=True, exist_ok=True)
        with tempfile.NamedTemporaryFile(dir=path.parent, delete=False) as temp:
            temporary = Path(temp.name)
            temp.write(expected[name])
        try:
            temporary.chmod(path.stat().st_mode & 0o777 if path.exists() else 0o644)
            temporary.replace(path)
        finally:
            temporary.unlink(missing_ok=True)


def export(source: Path, android_root: Path, server_root: Path, *, check: bool) -> int:
    if not (android_root / "Zpevnik/assets/templates").is_dir():
        raise ValueError(f"Not an Android destination: {android_root}")
    if not (server_root / "zpevnik/webapp/templates/inc/song-templates").is_dir():
        raise ValueError(f"Not a server destination: {server_root}")
    fingerprint = input_hash(source)
    revision = git_revision(source)
    with tempfile.TemporaryDirectory(prefix="chords-export-") as temporary:
        js, css = build_assets(source, Path(temporary))
        android, server = output_files(source, js, css)
    versions = {"css/chords.css": sha256(css)[:16], "js/chords.js": sha256(js)[:16]}
    cache_versions = {
        path: {asset: versions[asset] for asset in assets}
        for path, assets in CACHE_TAGS.items()
    }
    android[MANIFEST] = manifest_bytes(android_root, android, {}, fingerprint, revision)
    server[MANIFEST] = manifest_bytes(
        server_root, server, cache_versions, fingerprint, revision
    )
    for path, assets in cache_versions.items():
        server[path] = update_cache_tags((server_root / path).read_bytes(), assets)
    if input_hash(source) != fingerprint:
        raise ValueError("Source changed during export; retry with a stable checkout")
    changes = [
        (android_root, android, changed_files(android_root, android)),
        (server_root, server, changed_files(server_root, server)),
    ]
    stale = False
    for root, expected, names in changes:
        if names:
            stale = True
            sys.stdout.write(f"{'Stale' if check else 'Updating'} in {root}:\n")
            sys.stdout.write("".join(f"  {name}\n" for name in names))
            if not check:
                write_files(root, expected, names)
    if check and stale:
        sys.stderr.write(
            "Chords assets are out of sync. Run the exporter without --check.\n"
        )
        return 1
    sys.stdout.write("Chords assets are synchronized.\n")
    return 0


def main() -> int:
    source = Path(__file__).resolve().parent.parent
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--android-root", type=Path, default=source.parent)
    parser.add_argument(
        "--server-root", type=Path, default=source.parent.parent / "zpevnikserver"
    )
    parser.add_argument(
        "--check",
        action="store_true",
        help="Build and compare; do not write consumer files",
    )
    args = parser.parse_args()
    try:
        return export(
            source,
            args.android_root.resolve(),
            args.server_root.resolve(),
            check=args.check,
        )
    except (
        OSError,
        ValueError,
        KeyError,
        TypeError,
        subprocess.CalledProcessError,
    ) as error:
        sys.stderr.write(f"Asset export failed: {error}\n")
        return 1


if __name__ == "__main__":
    sys.exit(main())
