# Project Instructions — Mobile Developer Portfolio

## Product intent
Build a polished, responsive, single-page portfolio for a Mobile Developer. The supplied Stitch desktop and mobile designs are visual references for one responsive implementation, not separate websites.

## Required stack
- Kotlin Multiplatform (KMP).
- Compose Multiplatform UI targeting WebAssembly (`wasmJs`) for the initial implementation.
- Kotlin serialization for structured portfolio content.
- Gradle Kotlin DSL and a version catalog.
- Decap CMS for non-technical content editing through Git-based publishing.
- GitHub repository + GitHub Actions + Cloudflare Pages static hosting.
- No application database, server API, or runtime authentication for the public portfolio.

Do not silently replace the requested Kotlin/Compose stack. If a technical limitation makes it unsuitable, explain evidence and propose a decision before changing stack.

## Stability and risk
Compose Multiplatform Web/Wasm is Beta. Keep web-specific code isolated. Verify target browser support, accessibility, crawlability, bundle size, and Cloudflare Pages deployment early. Record any blocker and decision in `docs/decisions/`.

## Workflow
1. Inspect the existing repository before creating, deleting, or moving files.
2. Inspect Stitch MCP tools/resources and the available desktop/mobile design references before implementing UI. The Stitch MCP connection is already configured; do not reinstall or reconfigure it unless it is actually unavailable.
3. Treat Stitch as the visual source of truth. Extract design tokens, sections, spacing, typography, breakpoints, card patterns, and assets. Do not invent a materially different visual direction.
4. Desktop and mobile references must become one responsive composition. Do not create duplicate desktop/mobile pages.
5. Work in small, reviewable phases. Do not implement the entire application in one prompt.
6. Before editing, state the plan, affected files, and assumptions. Ask only when a decision blocks implementation.
7. Never fabricate personal details, employment history, project metrics, client names, app names, or performance claims. Use clearly marked content placeholders where facts are missing.
8. Do not add secrets, private keys, personal contact data not explicitly supplied, or credentials to source control.

## Architecture rules
- Keep content separate from UI. Public portfolio content belongs in structured files under `content/` and is loaded through a repository/data layer.
- Use typed Kotlin models; validate required fields and safe external URLs.
- Keep reusable UI components small and semantic.
- Keep design tokens centralized.
- Keep browser/JS interop behind a small platform boundary.
- Keep CMS configuration and generated build output separate from application source.
- Do not introduce a backend or database for this static portfolio.
- Use static assets in `public/images/`, `public/cv/`.
- CV download must be a real file served from the deployed site. Never generate a fake download or link to a local filesystem path.
- External links must open safely; use appropriate `rel` values when opening a new tab.
- Respect reduced-motion preferences. Avoid animation as the only way to communicate information.

## Quality requirements
- Responsive from narrow mobile to wide desktop.
- Keyboard navigable; visible focus; meaningful accessible names; sufficient contrast.
- Use semantic headings and meaningful link labels wherever the platform allows.
- Provide page title, description, canonical URL, Open Graph metadata, favicon, and structured metadata in the HTML shell.
- Test loading, refresh/deep-link behavior, external links, and CV download on the deployed preview.
- Optimize images; use modern formats where practical; avoid shipping unnecessarily large assets.
- No horizontal overflow at target viewport widths.
- No console errors or failed required network requests.
- Do not claim Lighthouse scores or accessibility compliance without running the checks.

## Verification
Discover the actual Gradle tasks and scripts before assuming command names. At minimum:
- Run the relevant Gradle compile/build task for the web target.
- Run tests for shared/common code.
- Run static analysis/formatting if configured.
- Run a production build.
- Run a browser smoke test at mobile and desktop widths.
- Verify the production artifact and CV URL on a Cloudflare Pages preview.

Report commands run, results, known gaps, and changed files at the end of each phase.

## Content integrity
Use the supplied portfolio owner content only when confirmed. If a section is not populated, hide it or show a clearly intentional placeholder in development; never publish lorem ipsum or invented claims.
