// Renders every song HTML snapshot of the old code (src/jvmTest/resources/html/before, with its stylesheet
// before/style.css) and of the current code (html/after, with style.scss compiled) in Chrome, and compares the
// text a reader sees. Display settings hide content by CSS, so only a browser can tell what is visible.
// Every difference must be listed in html/reviewed-changes.json.
//
//   npm ci --prefix tools/visible-text && node tools/visible-text/check.mjs
//
// Uses the installed Google Chrome; set CHROME_PATH to use another Chrome or Chromium binary.
import { readFileSync, readdirSync } from 'node:fs';
import { dirname, join } from 'node:path';
import { fileURLToPath } from 'node:url';
import { chromium } from 'playwright-core';
import * as sass from 'sass';

const root = join(dirname(fileURLToPath(import.meta.url)), '..', '..');
const html = join(root, 'src', 'jvmTest', 'resources', 'html');
const stylesheets = {
  before: readFileSync(join(html, 'before', 'style.css'), 'utf8'),
  after: sass.compile(join(root, 'src', 'resources', 'templates', 'style.scss')).css,
};

function snapshots() {
  return readdirSync(join(html, 'before'), { withFileTypes: true })
    .filter((entry) => entry.isDirectory())
    .flatMap((song) => readdirSync(join(html, 'before', song.name)).map((file) => `${song.name}/${file}`))
    .sort();
}

function reviewed() {
  const { changes } = JSON.parse(readFileSync(join(html, 'reviewed-changes.json'), 'utf8'));
  const result = new Map();
  for (const { song, layouts, variants, reason } of changes) {
    for (const layout of layouts) {
      for (const variant of variants) {
        result.set(`${song}/${layout}-${variant}.html`, reason);
      }
    }
  }
  return result;
}

// whitespace is layout here: the old and the new markup break lines differently for the same text, and a
// word split between two chord/text pairs ("ni|kam") is one word on screen but two in innerText
const normalize = (text) => text.replace(/\s+/g, '');

async function visibleText(page, side, path) {
  const source = readFileSync(join(html, side, path), 'utf8')
    .replace('<link href="style.css"/>', () => `<style>${stylesheets[side]}</style>`);
  await page.setContent(source, { waitUntil: 'load' });
  return normalize(await page.evaluate(() => document.body.innerText));
}

const browser = await chromium.launch(process.env.CHROME_PATH ? { executablePath: process.env.CHROME_PATH } : { channel: 'chrome' });
const page = await browser.newPage();
const expected = reviewed();
const changed = new Map();
const paths = snapshots();
for (const path of paths) {
  const before = await visibleText(page, 'before', path);
  const after = await visibleText(page, 'after', path);
  if (before !== after) {
    changed.set(path, { before, after });
  }
}

// innerText has no whitespace here and no CSS generated content, so some things are checked directly:
// repeat symbols drawn by CSS, and chords placed at their text (the structure itself is in the .parsed goldens)
const failures = [];
async function load(path) {
  const source = readFileSync(join(html, 'after', path), 'utf8')
    .replace('<link href="style.css"/>', () => `<style>${stylesheets.after}</style>`);
  await page.setContent(source, { waitUntil: 'load' });
}

// visible repeat symbols per snapshot: [start bars, end bars, counts]
const repeatSymbols = {
  'repeats/inline-default.html': [2, 2, 3], 'repeats/upper-default.html': [2, 2, 3],
  'repeats/inline-hide-chords.html': [2, 2, 3], 'repeats/upper-hide-chords.html': [2, 2, 3],
  'comments-and-repeat-chords/inline-default.html': [1, 1, 4], 'comments-and-repeat-chords/upper-default.html': [1, 1, 4],
  'comments-and-repeat-chords/inline-hide-chords.html': [1, 1, 4],
  'comments-and-repeat-chords/upper-hide-identical.html': [1, 1, 4],
};
for (const [path, expected] of Object.entries(repeatSymbols)) {
  await load(path);
  const found = await page.evaluate(() => {
    const drawn = (selector, pseudo, content) => [...document.querySelectorAll(selector)].filter((e) =>
      e.getClientRects().length > 0 && getComputedStyle(e, pseudo).content === content).length;
    return [drawn('.repeat-start', '::before', '"‖:"'), drawn('.repeat-end', '::after', '":‖"'), drawn('.repeat-count', '::after', '"×"')];
  });
  if (found.join() !== expected.join()) {
    failures.push(`${path}: repeat bars and counts drawn ${found.join('/')}, expected ${expected.join('/')}`);
  }
}

// chords stand at their text: above its start (upper), right before it and raised (inline)
for (const path of paths.filter((p) => /\/(inline|upper)-(default|transposed)\.html$/.test(p))) {
  await load(path);
  const misplaced = await page.evaluate((upper) => {
    const result = [];
    for (const pair of document.querySelectorAll('.song-chord-pair')) {
      const chord = pair.querySelector('a.chord');
      // the pair's text starts with its first piece, spaces and repeat bars ("‖:") included
      const text = [...pair.querySelectorAll('.text, .repeat-start, .repeat-end')].find((e) => e.getClientRects().length);
      if (!chord || !chord.getClientRects().length || !text) continue;
      const c = chord.getBoundingClientRect();
      const t = text.getClientRects()[0];
      const ok = upper
        ? Math.abs(c.left - t.left) <= 1 && c.bottom <= t.top + 1
        : c.right <= t.left + 1 && t.left - c.right <= 12 && c.top < t.top;
      if (!ok) result.push(`${chord.textContent} at ${Math.round(c.left)},${Math.round(c.top)} for "${text.textContent}" at ${Math.round(t.left)},${Math.round(t.top)}`);
    }
    return result;
  }, path.includes('/upper-'));
  for (const m of misplaced) failures.push(`${path}: chord ${m}`);
}
await browser.close();
for (const failure of failures) {
  console.log(`LAYOUT ${failure}`);
}

const unreviewed = [...changed.keys()].filter((path) => !expected.has(path));
const stale = [...expected.keys()].filter((path) => !changed.has(path));
for (const path of unreviewed) {
  const { before, after } = changed.get(path);
  let start = 0;
  while (start < before.length && before[start] === after[start]) start++;
  console.log(`UNREVIEWED ${path}\n  before: …${before.slice(Math.max(0, start - 40), start + 80)}\n  after:  …${after.slice(Math.max(0, start - 40), start + 80)}`);
}
for (const path of stale) {
  console.log(`NO LONGER CHANGED ${path}`);
}
console.log(`${paths.length} snapshots, ${changed.size} with visible changes, ${unreviewed.length} unreviewed, ${stale.length} reviewed but unchanged`);
console.log(`${failures.length} layout problems`);
process.exit(unreviewed.length || stale.length || failures.length ? 1 : 0);
