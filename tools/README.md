# Exporting song display assets

`src/resources/templates/`, `src/commonMain/`, and `src/jsMain/` are the source of
the song display assets consumed by Android and zpevnikserver. Edit the sources
here, then export the complete set together. Do not hand-edit the exported files.

From the `chords` directory:

```sh
python3 tools/export_assets.py
python3 tools/export_assets.py --check
```

## Song style defaults

`src/resources/templates/style-defaults.json` is the only source of the song style
defaults: colours per theme, the CSS variables (without the `--song-` prefix, common,
per layout, and the quoted repeat symbols), and the section names by section type.
After editing it, regenerate its copies:

```sh
python3 tools/style_defaults.py          # writes _style-defaults.scss and StyleDefaults.kt
python3 tools/style_defaults.py --check  # reports stale copies, writes nothing
```

`style.scss` uses the Sass variables, Kotlin (`CsHardcoded`, and later the app's style
editor) uses `StyleDefaults`, and the exporter copies the JSON to the server
(`zpevnik/webapp/style-defaults.json`). The exporter refuses to run with stale copies;
`StyleDefaultsTest` (JVM) and `tools/test_style_defaults.py` fail on them too.

## Destinations

Defaults are the enclosing Android checkout and its sibling `zpevnikserver`.
Destination paths can be overridden and are resolved relative to your working
directory:

```sh
python3 tools/export_assets.py --android-root .. --server-root ../../zpevnikserver
```

Requirements: Python 3.11+, Node/npm, and the JDK required by the Android checkout's
Gradle wrapper. Run from a `chords` submodule in that checkout: the exporter builds
`:chords:jsBrowserProductionWebpack` with the enclosing wrapper (the standalone
chords wrapper is older). Destination overrides do not change the build checkout.
Only the JS production bundle is built; no app builds or tests are run. The build
uses normal Gradle caches. Dart Sass 1.77.8, the version used for the existing CSS,
is invoked through `npm exec` and downloaded into npm's cache if necessary.

The exporter copies all four Android layouts, compiles their shared SCSS, exports
the three Django template bodies between the formatter markers wrapped in Django
`verbatim` tags, and copies the JS bundle. App-only chord links are removed by the
existing web renderer. It also updates the four Django asset references to
content-derived cache versions, preserving the rest of those templates.

`--check` builds expected outputs in staging/build directories, reports every
out-of-sync consumer file, and exits nonzero on a mismatch. It never changes
checked-in consumer assets, templates, or manifests. Build caches and npm/Gradle
intermediate files may still be written. Invalid template markers, missing cache
tags, and source edits during a build fail before any consumer files are written.

Each consumer gets `chords-assets.json` with the source revision, an input hash
(including uncommitted source edits, exporter code, and Gradle configuration),
output hashes, and browser cache versions. The revision identifies the checkout
at generation time; the input hash identifies its actual contents. A later
commit with identical inputs does not force regeneration just to update HEAD.
Consumers also get a lightweight verifier:

```sh
# From either consumer checkout; requires only Python, no sibling repository.
python3 tools/check_chords_assets.py
```

This verifies files against their recorded manifest, including web cache tags.
It cannot establish that the source checkout is current. Use the full `--check`
before merging coordinated changes to establish that both consumers match the
same source. Commit the source changes, exported assets, manifests, and Android
submodule pointer together in their respective repositories. The exporter never
stages, commits, or pushes files.

Focused tests (no builds or downloads):

```sh
python3 -m unittest discover -s tools -p test_export_assets.py
```

# Checking what readers see

Display settings (hide chords, text, comments, repeated chord sequences) are applied by the stylesheet, so
whether the old and the new HTML show the same can only be told in a browser. `visible-text/check.mjs`
renders every snapshot in `src/jvmTest/resources/html/before` (old code, its `style.css`) and `after`
(current code, `style.scss` compiled) in Chrome and compares the visible text. Every difference must be
listed with a reason in `src/jvmTest/resources/html/reviewed-changes.json`.

```sh
npm ci --prefix tools/visible-text
node tools/visible-text/check.mjs
```

The text comparison ignores whitespace and cannot see CSS generated content, so the same run also checks
the current snapshots directly: repeat bars and counts drawn by CSS (`‖:`, `:‖`, `×`) for songs with known
repeats, and every chord placed at its text (above its start in the upper layout, right before it and raised
in the inline layout). Legacy alignment comes from spaces and is covered by `LegacyRowsTest`.

Requirements: Node/npm and Google Chrome (or `CHROME_PATH` pointing to Chrome or Chromium).
Regenerate `after` first after an intended change: `UPDATE_GOLDEN=1 ./gradlew :chords:jvmTest`.
