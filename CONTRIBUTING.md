# Contributing & Reporting Issues

Short guide for anyone using `wiom-ds-android` — designers, devs, QA — to report bugs and request changes.

---

## Opening an issue (the easy way)

1. Go to https://github.com/abhishekgarg-wiom/wiom-ds-android/issues
2. Click the green **"New issue"** button (top-right of the issues list).
3. Pick the template that matches your situation:
   - **🐛 Bug report** — a component renders wrong, crashes, or behaves weirdly
   - **🎨 Visual mismatch with Figma** — the code looks different from the design
   - **✨ Component / enhancement request** — new component, new variant, new param
   - **📖 Documentation issue** — a README / guide is wrong, unclear, or missing something
4. Fill the fields. The template has only the fields that matter — don't overthink it.
5. Click **"Submit new issue"**.

That's it. You'll be notified by email when someone responds.

## What makes a good issue

The template asks for this, but in case you skip:

- **Library version** — from your `build.gradle.kts`: `v0.2.0`, `v0.1.1`, etc.
- **Screenshot or video** — the single most useful thing. Any photo of the phone screen or a screen recording works.
- **Figma link** (for visual mismatches) — so the reviewer can compare without hunting for the frame.
- **Device + Android version** (for bugs) — some bugs only happen on older Android or specific OEM skins.
- **What you tried** — if you worked around it, describe the workaround.

## Severity isn't a field — it's obvious from what you write

- "App crashes when I open the Bills tab" → we fix it this week.
- "Border is 1dp off on the dropdown expanded state" → we fix it in the next minor release.
- "Would be nice to have a 7th pagination style" → we discuss and schedule.

Don't stress about labels or priority — we'll handle those.

## Questions vs issues

If you're not sure it's a bug and just want to ask "how do I do X with this library," use **Discussions** instead of Issues: https://github.com/abhishekgarg-wiom/wiom-ds-android/discussions. Same notification flow, but keeps the issue list for actionable work.

## Proposing a code change (PR)

1. Fork the repo, clone your fork.
2. Create a branch: `git checkout -b fix/dropdown-radius`.
3. Make your changes. Follow the rules in [CLAUDE.md](./CLAUDE.md) — especially the token-only rule.
4. Update / add the component's README.md if the API changed.
5. Add an entry to [CHANGELOG.md](./CHANGELOG.md) under `[Unreleased]`.
6. Push, open a Pull Request against `main`.
7. CI runs automatically. Fix any issues it flags.
8. A maintainer reviews. Once approved + green, it merges.

## Release cadence

- **Patch releases** (v0.2.1, v0.2.2) — bug fixes, visual tweaks. As soon as something needs fixing.
- **Minor releases** (v0.3.0) — new components, new variants, new params. Every few weeks.
- **Major releases** (v1.0.0) — breaking API changes. Rarely. Will include a migration guide.

After any release, check [CHANGELOG.md](./CHANGELOG.md) for what changed and if it affects your app.

---

Thanks for reporting issues — that's how the library improves.
