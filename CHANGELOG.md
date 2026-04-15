# Changelog

All notable changes to the Wiom Design System (Android) will be documented here.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/) and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Planned
- `WiomInput` component (input, textarea, OTP, search, password, mobile variants)
- Google Fonts provider for Noto Sans
- Paparazzi screenshot testing
- Detekt lint rule enforcing token-only values

## [0.0.1] — 2026-04-15

First scaffold — foundations only.

### Added
- `WiomTheme` composable + `WiomTheme.*` token accessor
- `WiomColors` — 43 semantic color tokens (brand, text, surface, border, positive, negative, warning, info, overlay)
- `WiomTypography` — 13 type tokens (display, heading, title, body, label, meta)
- `WiomSpacing` — 13 spacing tokens on 4px grid
- `WiomRadius` — 7 radius tokens (none → full)
- `WiomStroke` — 2 stroke widths (small 1dp, medium 2dp)
- `WiomShadow` — 5 elevation tokens (none → xl)
- `WiomIconSize` — 4 icon size tokens (xs/sm/md/lg)
- `WiomIcon` composable + `WiomIcons` facade
- 10 Material Symbols Rounded drawables: search, cancel, close, phone, visibility, visibility_off, check_circle, error, warning, refresh, expand_more
- Material3 bridge so Material components pick up Wiom colors and type
- Sample app demonstrating tokens and icons
- CLAUDE.md with 5 hard rules for all future contributors

### Known gaps
- Typography uses `FontFamily.Default`. Noto Sans via Google Fonts provider coming in v0.1.0.
- No components yet — foundation-only release.
