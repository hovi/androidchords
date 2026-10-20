"""Verify a consumer's exported chords assets without Gradle or the source checkout."""

import argparse
import hashlib
import json
import re
import sys
from pathlib import Path

MANIFEST = "chords-assets.json"


def sha256(data: bytes) -> str:
    return hashlib.sha256(data).hexdigest()


def cache_tag_pattern(asset: str) -> re.Pattern[str]:
    return re.compile(
        r"(?P<tag>\{%\s*static\s+['\"]"
        + re.escape(asset)
        + r"['\"]\s*%\})(?:\?v=[^'\"\s<>]*)?"
    )


def consumer_path(root: Path, relative: str) -> Path:
    path = (root / relative).resolve()
    if not path.is_relative_to(root.resolve()) or path == root.resolve():
        raise ValueError(f"Invalid asset path: {relative}")
    return path


def verify(root: Path) -> list[str]:
    manifest = json.loads((root / MANIFEST).read_text(encoding="utf-8"))
    if manifest["schema_version"] != 1 or not manifest["files"]:
        raise ValueError("Unsupported or empty chords asset manifest")
    errors = []
    for relative, expected in manifest["files"].items():
        path = consumer_path(root, relative)
        if not path.is_file():
            errors.append(f"Missing: {relative}")
        elif sha256(path.read_bytes()) != expected:
            errors.append(f"Modified: {relative}")
    for relative, assets in manifest["cache_versions"].items():
        path = consumer_path(root, relative)
        if not path.is_file():
            errors.append(f"Missing: {relative}")
            continue
        content = path.read_text(encoding="utf-8")
        for asset, version in assets.items():
            matches = list(cache_tag_pattern(asset).finditer(content))
            if not matches or any(
                match.group() != match.group("tag") + "?v=" + version
                for match in matches
            ):
                errors.append(f"Stale cache version: {relative} ({asset})")
    return errors


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument(
        "--root", type=Path, default=Path(__file__).resolve().parent.parent
    )
    args = parser.parse_args()
    try:
        errors = verify(args.root)
    except (OSError, ValueError, KeyError, TypeError) as error:
        sys.stderr.write(f"Cannot verify chords assets: {error}\n")
        return 1
    if errors:
        sys.stderr.write("\n".join(errors) + "\nRe-export these assets from chords.\n")
        return 1
    sys.stdout.write("Chords assets match the recorded export.\n")
    return 0


if __name__ == "__main__":
    sys.exit(main())
