# Product Requirements Document
## Kotlin Multiplatform Mobile Developer Portfolio

**Status:** Draft for implementation  
**Product type:** Public, responsive, single-page portfolio  
**Primary audience:** Mobile engineering recruiters, engineering managers, technical interviewers, and potential collaborators.

## 1. Product vision
Present the owner as a Mobile Developer who can design, build, test, secure, and maintain production-grade mobile applications. The site should communicate engineering depth through concise evidence: shipped applications, case studies, technical specializations, measurable outcomes, and career progression.

The visual direction follows the supplied Stitch reference: dark technical interface, cyan/pink accent gradients, compact navigation, high-contrast typography, card-based case studies, capability panels, and a vertical experience timeline.

## 2. Goals
1. Make the owner's professional positioning immediately clear.
2. Let visitors scan experience, technical strengths, projects, and career history quickly.
3. Provide a reliable, direct CV download.
4. Work well on mobile and desktop through one responsive implementation.
5. Allow a non-technical owner to update portfolio content without editing Kotlin source.
6. Publish content changes through Git-based workflow to `main`, followed by automated static deployment.
7. Demonstrate KMP and Compose Multiplatform engineering quality without making the site unnecessarily complex.

## 3. Non-goals
- User accounts, visitor profiles, comments, or personalization.
- Database-backed CMS or custom backend.
- Job application tracking or contact CRM.
- Complex blog platform.
- Separate mobile and desktop applications/pages.
- Fabricated testimonials, project metrics, client names, or app store links.
- A full interactive product demo for every case study.

## 4. Primary user journeys
### Visitor
1. Opens the portfolio on desktop or mobile.
2. Understands role, specialization, and experience from the hero.
3. Uses navigation or scrolls to inspect About, Projects, Services/Skills, Experience, and Contact.
4. Opens a case study or external professional profile.
5. Downloads the CV.

### Portfolio owner
1. Opens the CMS admin page.
2. Edits profile, skills, project details, experience, links, or contact information.
3. Uploads/updates approved image assets and CV.
4. Saves/publishes through Git workflow to `main`.
5. GitHub Actions builds and deploys the static site to Cloudflare Pages.

## 5. Information architecture
Single-page sections, in this order:

1. **Header / Navigation**
   - Brand/name and role label.
   - Anchor links: About, Projects, Services, Experience, Contact.
   - Compact responsive mobile navigation.
2. **Hero**
   - Name, role, concise positioning statement.
   - Short summary focused on mobile engineering.
   - Primary CTA: View Featured Projects.
   - Secondary CTA: Download CV.
   - Profile image/portrait and compact status/summary panel.
   - Technology highlights.
3. **Impact Metrics**
   - Years of experience.
   - Apps shipped.
   - Relevant quality/performance/security metric(s).
   - Only verified numbers; omit unknown metrics.
4. **About / Profile Matrix**
   - Short professional narrative.
   - Engineering principles/specializations.
   - Technology and tooling groups.
5. **Featured Engineering Case Studies**
   - Project image, name, category/stack tags, concise problem/solution/outcome.
   - Optional verified metrics.
   - Case-study detail link only when a real destination exists.
6. **Services & Specializations**
   - Cross-platform development.
   - Native Android/iOS engineering.
   - App architecture and performance.
   - UI/UX implementation and fluid interactions.
   - Labels and descriptions must reflect actual capabilities.
7. **Experience Timeline**
   - Role, company, location/work mode, dates.
   - Impact-oriented bullet points.
   - Relevant stack tags.
8. **Contact / Footer**
   - Professional contact method and verified profile links.
   - Back-to-top action.
   - Copyright.

## 6. Functional requirements
### FR-01 Responsive layout
- One implementation must support mobile, tablet, laptop, and wide desktop.
- Navigation must collapse appropriately on narrow screens.
- Cards and grids must reflow without horizontal scrolling.
- Content order must remain logical at every breakpoint.

### FR-02 Navigation
- Header links navigate to section anchors.
- Current/hover/focus states are visually distinguishable.
- Mobile menu is keyboard accessible and closes after selecting a section.

### FR-03 CV download
- A clearly visible “Download CV” CTA is present in the hero and optionally footer.
- CV is stored as a static file under `public/cv/`.
- Use a stable public URL such as `/cv/hirarki-pratama-cv.pdf` (confirm final filename).
- Set a descriptive download filename where supported.
- If the CV is missing, do not render a broken link; show a development warning and hide the CTA in production until configured.
- Verify the deployed URL returns the actual PDF.

### FR-04 Content management
- Decap CMS admin is available at `/admin/`.
- Content is stored as version-controlled JSON files under `content/`.
- CMS edits content and commits to the configured branch (`main` for direct publishing).
- Image uploads are stored under `public/images/`.
- The public application reads typed content from bundled/generated JSON resources.
- Do not require a database or runtime CMS API.

### FR-05 Case studies
- Support multiple projects with title, slug, summary, problem, solution, impact, stack tags, image, and optional external URL.
- Hide optional values when absent.
- Never invent outcome metrics.

### FR-06 Contact and external links
- Render only verified contact/profile URLs.
- External links have clear labels and safe new-tab behavior when applicable.
- No fake contact form. A mailto link is acceptable if the owner supplies an address.

### FR-07 Optional audio
- If the previously discussed audio feature remains part of the final content, audio files stay static under `public/audio/`.
- Implement a small accessible player with play/pause, progress, seek, duration, loading, and error states.
- Do not autoplay. Omit the feature entirely if no approved audio content is supplied.

## 7. Content model
Suggested top-level JSON documents:
- `content/site.json`: identity, hero, summary, metrics, navigation labels, footer.
- `content/skills.json`: groups, categories, technologies, descriptions.
- `content/projects.json`: ordered project list and case-study content.
- `content/experience.json`: roles, dates, descriptions, achievements, tags.
- `content/services.json`: service cards.
- `content/links.json`: contact and professional profile links.

All fields should be typed in Kotlin. Required fields should fail validation during build/test with a helpful message. Optional fields should be nullable or have safe defaults.

## 8. Non-functional requirements
### Performance
- Optimize and resize images; avoid enormous hero assets.
- Lazy-load below-the-fold images where supported by the rendering stack.
- Avoid unnecessary animations and large third-party libraries.
- Set a performance budget after measuring the first production build.

### Accessibility
- Keyboard access for all controls.
- Visible focus indicators.
- Text contrast appropriate for dark backgrounds.
- Reduced-motion support.
- Descriptive alternative text for informative images; decorative images should not add noise.
- Logical heading hierarchy and accessible names.

### SEO and sharing
- Static HTML shell contains title, meta description, canonical URL, Open Graph/Twitter metadata, favicon, and `noscript` fallback.
- Add structured metadata for a Person/Professional profile only using verified information.
- Test crawler-visible output. Do not assume a client-rendered Compose page provides complete SEO by default.

### Reliability
- No broken images, links, or CV downloads.
- Graceful empty/optional content behavior.
- Production build reproducible in CI.
- Cloudflare Pages preview must be validated before publishing.

### Security/privacy
- No secrets in repository or client bundle.
- CMS write access is restricted to authorized Git provider users.
- Validate/sanitize external URLs and content-derived links.
- No analytics/tracking unless explicitly approved and disclosed.

## 9. Visual requirements
- Match the Stitch reference's dark visual language and accent palette.
- Centralize colors, typography scale, spacing, radii, elevations, and breakpoints as design tokens.
- Use consistent card styles and restrained glow/gradient accents.
- Keep content density readable; do not reproduce tiny text simply because it appears in the screenshot.
- Use actual owner portrait/project images when supplied. Use neutral placeholders during development.

## 10. Acceptance criteria
- [ ] All required sections appear in the correct order.
- [ ] Desktop and mobile Stitch references are implemented as one responsive layout.
- [ ] Navigation works by mouse, touch, and keyboard.
- [ ] No horizontal overflow at 360px, 390px, 768px, 1024px, and 1440px widths.
- [ ] CV download works on deployed preview and opens the correct PDF.
- [ ] CMS can update at least one field in every content collection and publish to `main`.
- [ ] A content change triggers a successful build/deploy.
- [ ] Missing optional content does not produce broken UI.
- [ ] No fabricated personal facts or metrics are present.
- [ ] Build, tests, accessibility checks, and browser smoke tests are documented.
- [ ] Cloudflare Pages serves the final production artifact correctly.

## 11. Risks and decision gates
1. **Compose Web/Wasm Beta:** validate browser support and production deployment in Phase 1. If unacceptable, stop and decide on Kotlin/JS/Compose HTML or a conventional semantic web UI with KMP-shared models.
2. **SEO/accessibility:** validate actual rendered DOM and crawler output early, not at the end.
3. **CMS JSON compatibility:** prove Decap can edit the selected JSON files and upload media before building all UI.
4. **CV privacy/versioning:** confirm the exact CV file and public filename before production.
5. **Content completeness:** use owner-confirmed facts only; keep unknown content out of production.
