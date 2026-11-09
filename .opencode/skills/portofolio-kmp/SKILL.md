---
name: portfolio-kmp
description: Build and review this responsive Mobile Developer portfolio using Kotlin Multiplatform, Compose Multiplatform Web/Wasm, Stitch MCP, typed JSON content, static assets, accessibility, and Cloudflare Pages.
---

## Core Stack

- Kotlin Multiplatform
- Compose Multiplatform
- Kotlin/Wasm
- Kotlin Serialization
- Gradle Kotlin DSL
- Stitch MCP
- Static JSON content
- Cloudflare Pages

## UI Rules

- One responsive Compose implementation.
- Never create separate desktop/mobile pages.
- Use Stitch as the visual reference.
- Extract design tokens before implementing sections.
- Centralize typography, colors, spacing, radii and breakpoints.
- Prefer reusable composables.
- Avoid unnecessary architecture.

## Portfolio Rules

- This is a static portfolio, not a mobile application backend.
- Do not introduce a database.
- Do not introduce unnecessary networking.
- Content belongs in JSON.
- Personal information must never be fabricated.
- Metrics must be explicitly verified.
- CV is a static PDF.
- Images are static assets.

## Web Rules

- Test 360px, 390px, 768px, 1024px and 1440px.
- No horizontal overflow.
- Keyboard navigation must work.
- Focus states must be visible.
- Respect reduced motion.
- Validate SEO metadata.
- Validate actual browser rendering.
- Validate Wasm browser compatibility.

## Stitch Rules

Before implementing a major section:

1. Inspect Stitch.
2. Identify layout.
3. Identify spacing.
4. Identify typography.
5. Identify colors.
6. Identify responsive behavior.
7. Build reusable Compose components.
8. Compare implementation against Stitch.

Never blindly reproduce screenshot coordinates.