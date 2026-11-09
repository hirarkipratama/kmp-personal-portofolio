# OpenCode Prompt Runbook

Run these prompts in order. Do not paste all prompts into one request. Keep the agent in Plan mode for discovery and architecture; switch to Build mode for implementation. Stitch MCP is already configured in OpenCode with project ID of '11677376707151998401'.

## Phase 0 — Repository reconnaissance (Plan mode)
```text
We are starting a Kotlin Multiplatform + Compose Multiplatform single-page portfolio for a Mobile Developer. Inspect the current repository without modifying files.

Read AGENTS.md, PRD.md, and ARCHITECTURE.md if present. Inspect the existing Gradle setup, modules, resources, build scripts, deployment configuration, and current Git state. Do not overwrite working project configuration.

Report:
1. Current project structure and what already exists.
2. Whether this is a blank repository or an existing app.
3. Existing Kotlin, Gradle, Compose, and plugin versions.
4. Actual available Gradle tasks and current build status.
5. Missing prerequisites and risks.
6. A minimal implementation plan in phases.

Do not edit files or run destructive commands.
```

## Phase 1 — Stitch design extraction (Plan mode; use Stitch MCP)
```text
Use the already-configured Stitch MCP integration. Discover the available Stitch tools/resources and inspect the desktop and mobile screens for this portfolio. Do not assume tool names; use the MCP capabilities exposed in this session.

Treat both screens as two viewport references for ONE responsive website. Do not create separate desktop and mobile implementations.

Extract and report:
- All page sections and their order.
- Header/navigation behavior.
- Desktop and mobile layout differences.
- Color palette, gradients, typography, spacing, radii, borders, shadows/glows.
- Grid structures, card patterns, image aspect ratios, and responsive breakpoints inferred from the designs.
- Visible text/content fields that need real owner-provided content.
- Asset requirements and which assets are missing.
- Interaction states for navigation, project links, and Download CV.

Create a design-token proposal and a component inventory. Reference the Stitch screens accurately. Do not implement UI yet.
```

## Phase 2 — Technical spike and go/no-go (Plan mode, then Build mode only for spike)
```text
Evaluate the requested Compose Multiplatform Web/Wasm stack against this portfolio's needs: browser support, semantic HTML/accessibility, SEO/crawler visibility, static hosting on Cloudflare Pages, image assets, PDF download, and Decap CMS.

Use current official Kotlin/JetBrains documentation where necessary. Verify compatible Kotlin, Compose Multiplatform, Gradle, and serialization versions instead of guessing.

Create a minimal vertical spike only:
- Build the smallest Compose Multiplatform wasmJs page.
- Confirm it runs locally.
- Confirm a production distribution task and output directory.
- Confirm it can be served as static files.
- Verify whether key content is represented in accessible DOM.
- Record browser support constraints and any SEO caveat.

Do not build the portfolio sections. Report a GO / CONDITIONAL GO / NO-GO recommendation. If NO-GO, present the smallest alternative that still keeps KMP value (for example shared Kotlin models with a web-native UI) and wait for approval before changing direction.
```

## Phase 2.5 - Skill Integration
MODE: PLAN ONLY

This is an architecture reconciliation task.
Do not modify source code, Gradle files, dependencies, configuration,
or project structure.

Do not install additional dependencies.

Do not implement anything.

Your output is a proposed reconciliation and implementation plan
for approval before Build mode.

We have already completed Phase 0, Phase 1, and Phase 2 of this portfolio project.

DO NOT repeat or redo those phases.

The following has already been established:
- Repository reconnaissance has been completed.
- Stitch desktop and mobile designs have been inspected.
- Design structure/tokens/component requirements have been identified.
- The KMP + Compose Multiplatform Web/Wasm technical spike has been completed.
- The Phase 2 GO/CONDITIONAL GO/NO-GO decision and its evidence already exist in the repository/session artifacts.

A new Compose/Kotlin/KMP agent skill has now been added to this OpenCode project.

Your task is to integrate this skill into the existing project without restarting the project.

First:
1. Read AGENTS.md.
2. Read PRD.md.
3. Read ARCHITECTURE.md.
4. Read the existing Phase 0–2 outputs/documentation.
5. Read and understand the newly installed Compose/Kotlin/KMP skill(s).
6. Inspect the current repository state and Git diff.
7. Inspect the existing Gradle/KMP setup produced by Phase 2.

Then perform an architecture reconciliation.

Compare:
A. Existing project decisions from Phase 0–2.
B. The requirements in PRD.md and ARCHITECTURE.md.
C. Recommendations from the newly installed skill(s).

For every difference, classify it as:
- KEEP — existing decision is correct and should remain.
- ADOPT — skill recommendation improves the project and should be incorporated.
- ADAPT — skill recommendation is useful but must be simplified for this portfolio.
- REJECT — recommendation is unnecessary or inappropriate for this project.

Important constraints:
- Do not restart Phase 0–2.
- Do not replace the KMP + Compose Multiplatform direction.
- Do not introduce unnecessary Clean Architecture, MVVM, use-case layers, DI frameworks, networking, databases, or backend services.
- This is a static portfolio website, not a mobile business application.
- Keep architecture proportional to the project's complexity.
- Stitch MCP remains the visual source of truth.
- The desktop and mobile Stitch designs represent one responsive UI.
- Do not invent portfolio content, metrics, employment history, or project details.

Pay special attention to:
- Compose Multiplatform Web/Wasm architecture.
- commonMain vs wasmJsMain boundaries.
- Compose state management.
- UI component organization.
- design system/token organization.
- resource/static asset handling.
- responsive layout patterns.
- accessibility.
- performance.
- testing.
- browser/platform interop.
- web-specific limitations.
- maintainability.

Do not make broad implementation changes yet.

Produce:

1. "Existing Decisions"
   - Summarize the important decisions already made in Phase 0–2.

2. "Skill Recommendations"
   - Summarize recommendations from the new skill that are relevant to this project.

3. "Reconciliation"
   - For each relevant recommendation, classify it as KEEP, ADOPT, ADAPT, or REJECT.
   - Explain why.

4. "Architecture Changes"
   - List only the changes that should actually be made to the current architecture.

5. "Phase 3 Implementation Plan"
   - Provide the exact implementation sequence for the next phase.
   - Identify files that should be created/modified.
   - Identify dependencies that are actually necessary.

6. "Risks"
   - Identify anything introduced by the skill that could overcomplicate the portfolio.

Do not modify source files unless a small documentation update is required.
Wait for approval before implementing architecture changes.
STOP after producing the reconciliation.

Do not execute the proposed changes.
Do not switch to Build mode automatically.
Wait for explicit approval.

## Phase 3 — Project foundation (Build mode)
```text
Before implementing, apply the approved decisions from the Phase 2.5 Skill Integration & Architecture Reconciliation.

Do not introduce recommendations that were classified as REJECT.
Do not add architecture that was not approved.

Implement only the approved project foundation using the existing template conventions.

Requirements:
- Preserve working Gradle configuration.
- Pin compatible plugin/dependency versions in the version catalog.
- Establish commonMain and wasmJsMain boundaries.
- Add kotlinx.serialization and a minimal typed SiteContent model.
- Add a content loading/decoding boundary.
- Add a basic Compose app entry point and theme skeleton.
- Add static public asset handling.
- Add a production build task note with its actual output path.

Do not implement full page sections yet. Do not add unnecessary modules or libraries. Run the relevant compile/test/build tasks and report exact results and changed files.
```

## Phase 4 — Content model and Decap CMS proof (Build mode)
```text
Implement the content pipeline and CMS proof of concept.

Use JSON content files under content/ and typed @Serializable Kotlin models. Start with site.json and projects.json only. Validate required fields, unique IDs/slugs, safe URL schemes, and local asset paths.

Configure Decap CMS at /admin/ with GitHub backend and main branch publishing. Configure media uploads to public/images with public path /images. Confirm the current Decap configuration supports the chosen file-based JSON collections. Do not invent authentication secrets or commit credentials.

Prove this end-to-end:
1. Edit one field through the CMS configuration/workflow.
2. Confirm the JSON file is the source of truth.
3. Confirm the Kotlin app loads the changed content after a rebuild.
4. Confirm an uploaded image path resolves.

If the GitHub backend authentication or JSON collection strategy is not viable, stop and report the blocker instead of creating a custom backend.
```

## Phase 5 — Design system and responsive shell (Build mode; use Stitch MCP)
```text
Implement the design system and page shell based on the approved Stitch extraction.

Use one responsive Compose UI tree. Create centralized tokens for colors, gradients, typography, spacing, radii, surfaces, and breakpoints. Implement only:
- App/page scaffold.
- Header and desktop navigation.
- Mobile navigation/menu.
- Section container and section heading primitives.
- Shared card, chip/tag, and button primitives.

Match the Stitch reference's dark technical style, cyan/pink accents, compact navigation, and restrained glow. Do not add invented copy or metrics. Use temporary labeled placeholders where approved content is missing.

Validate at 360px, 390px, 768px, 1024px, and 1440px. Fix horizontal overflow before proceeding.
```

## Phase 6 — Hero and metrics (Build mode)
```text
Implement the Hero and Metrics sections using the content repository and Stitch design.

Hero:
- Owner name and role.
- Main positioning headline and concise summary.
- Featured Projects anchor CTA.
- Real Download CV link to the configured static PDF path.
- Profile portrait/summary panel.
- Technology tags.

Metrics:
- Render only verified values from content.
- If a metric is absent, omit that metric rather than showing zero or inventing a number.

Ensure the CV CTA is hidden or clearly disabled in production until the real PDF exists. Verify the file path and browser download behavior. Add responsive and accessibility checks.
```

## Phase 7 — About / profile matrix (Build mode)
```text
Implement the About/Profile Matrix section from the Stitch reference.

Use data-driven content for:
- Professional narrative.
- Engineering principles/specializations.
- Mobile frameworks and languages.
- State management and architecture patterns.
- Backend/API/storage integrations.
- Testing and CI/CD workflows.

Keep the section scannable and responsive. Avoid turning every technology into a paragraph. Do not add a technology unless the owner confirms it. Verify heading hierarchy, contrast, keyboard focus, and mobile stacking.
```

## Phase 8 — Featured case studies (Build mode)
```text
Implement the Featured Engineering Case Studies section.

Each project card should support:
- Project title and concise summary.
- Project category and stack tags.
- Approved image with meaningful alt text.
- Problem, solution, and impact details for a case-study view or expandable detail.
- Optional verified metrics.
- Optional external project link.

Use the Stitch card proportions and visual treatment. Do not fabricate project names, client identities, screenshots, metrics, or app-store links. If only a short summary is supplied, render only that summary. Add sensible image sizing/loading and mobile layout.
```

## Phase 9 — Services and experience timeline (Build mode)
```text
Implement Services/Specializations and Experience Timeline from typed content.

Services:
- Responsive service cards.
- Concise title and description.
- Optional capability bullets.

Experience:
- Reverse chronological or owner-defined order.
- Role, organization, location/work mode, dates.
- Impact-focused bullet list.
- Relevant technology tags.

Hide missing optional fields. Do not invent employer details, dates, outcomes, or location. Match the Stitch timeline and card styling while maintaining readability on mobile.
```

## Phase 10 — Contact, footer, and optional audio (Build mode)
```text
Implement the Contact/Footer section with only verified professional links and contact methods. Add a back-to-top action.

If approved audio assets are part of the final scope, load them as static files from public/audio and implement an accessible player with play/pause, progress, seeking, duration, loading, and error states. Do not autoplay. If there are no approved audio files, do not implement an empty audio feature.

Verify all external links, mailto links, focus states, and mobile behavior.
```

## Phase 11 — SEO, accessibility, performance, and hardening (Plan mode then Build mode)
```text
Audit the complete portfolio against PRD.md and AGENTS.md. Do not assume the app is production-ready because it compiles.

Inspect:
- Static HTML metadata shell, title, description, canonical, Open Graph, favicon, noscript content.
- Actual rendered DOM and crawler-visible content.
- Keyboard-only navigation, focus visibility, accessible names, contrast, reduced motion.
- Responsive overflow at 360, 390, 768, 1024, and 1440px.
- Image dimensions/formats/payload and lazy loading.
- CV and asset URLs.
- Console errors and failed network requests.
- Bundle size and initial loading behavior.

Run available automated checks. Fix issues by severity. Do not claim compliance or scores unless measured. If Compose/Wasm creates material SEO or accessibility problems, stop and report options before applying a stack change.
```

## Phase 12 — CI, Cloudflare Pages, and release verification (Build mode)
```text
Prepare production deployment using the existing GitHub repository and Cloudflare Pages.

First inspect current deployment configuration. Choose exactly one authoritative deployment mechanism (GitHub Actions or Cloudflare Git integration) and document why. Configure:
- Content validation.
- Gradle web production build using the verified task.
- Artifact/output directory using the verified path.
- Deployment to Cloudflare Pages.
- Preview validation where supported.
- main as production publishing branch.

Do not invent Cloudflare account IDs, project names, tokens, or secrets. Document required repository secrets as placeholders in setup instructions only. Verify /, /admin/, CV PDF, images, and refresh behavior on the deployed preview before release.
```

## Phase 13 — Final review (use reviewer subagent)
```text
Use the portfolio-reviewer subagent to review the entire implementation against PRD.md, ARCHITECTURE.md, and AGENTS.md.

Ask for findings ordered by severity, with file and line references. Focus on correctness, responsive regressions, accessibility, content integrity, broken links/assets, security, SEO, deployment reproducibility, and unnecessary complexity.

Do not accept cosmetic-only approval. Fix critical/high findings, rerun the relevant checks, and provide a release checklist with known limitations.
```

## Agent operating rules
- Keep each phase in a separate OpenCode task/session when practical.
- Commit after each verified milestone.
- Do not let the agent continue to the next phase if the current acceptance criteria fail.
- Use Plan mode for analysis and Build mode for changes.
- Use Stitch MCP during design extraction and visual implementation, not as a substitute for browser testing.
- Ask the agent to summarize changed files and exact commands/results after every phase.
