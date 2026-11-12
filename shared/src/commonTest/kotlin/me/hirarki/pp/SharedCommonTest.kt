package me.hirarki.pp

import kotlinx.serialization.json.Json
import me.hirarki.pp.domain.model.AboutContent
import me.hirarki.pp.domain.model.Experience
import me.hirarki.pp.domain.model.Project
import me.hirarki.pp.domain.model.ProjectScreenshot
import me.hirarki.pp.domain.model.Service
import me.hirarki.pp.domain.model.SiteContent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SharedCommonTest {

    @Test
    fun siteContentSupportsPortraitAndCvPathsWithoutRequiredMetrics() {
        val content = Json.decodeFromString<SiteContent>(
            """
            {
              "identity": {"name": "Portfolio Owner", "role": "Mobile Developer"},
              "hero": {
                "headline": "Building mobile applications",
                "summary": "A verified professional summary.",
                "profileImagePath": "/images/profile.JPG",
                "cvPath": "/cv/portfolio.pdf"
              },
              "footer": {"copyright": "Portfolio"}
            }
            """.trimIndent()
        )

        assertEquals("/images/profile.JPG", content.hero.profileImagePath)
        assertEquals("/cv/portfolio.pdf", content.hero.cvPath)
        assertEquals(emptyList(), content.metrics)
        assertEquals(null, content.about)
    }

    @Test
    fun siteContentDecodesAboutFocusAreas() {
        val content = Json.decodeFromString<SiteContent>(
            """
            {
              "identity": {"name": "Portfolio Owner", "role": "Mobile Developer"},
              "hero": {
                "headline": "Building mobile applications",
                "summary": "A verified professional summary."
              },
              "about": {
                "focusAreas": ["Architecture", "Testing", "CI/CD"]
              },
              "footer": {"copyright": "Portfolio"}
            }
            """.trimIndent()
        )

        assertEquals(listOf("Architecture", "Testing", "CI/CD"), content.about?.focusAreas)
    }

    @Test
    fun aboutContentRejectsBlankFocusAreas() {
        assertFailsWith<IllegalArgumentException> {
            AboutContent(listOf(" "))
        }
    }

    @Test
    fun projectDecodesSeparateScreenshotsAndOptionalContent() {
        val project = Json.decodeFromString<Project>(
            """
            {
              "slug": "sample-project",
              "title": "Sample Project",
              "category": "Mobile application",
              "screenshots": [
                {
                  "image": "/images/projects/sample-project/screen-01.jpg",
                  "alt": "Sample Project account dashboard"
                },
                {
                  "image": "/images/projects/sample-project/screen-02.jpg",
                  "alt": "Sample Project settings screen"
                }
              ],
              "technologies": []
            }
            """.trimIndent()
        )

        assertEquals("sample-project", project.slug)
        assertEquals(2, project.screenshots.size)
        assertEquals("Sample Project account dashboard", project.screenshots.first().alt)
        assertEquals(null, project.problem)
        assertEquals(emptyList(), project.technologies)
    }

    @Test
    fun projectScreenshotRequiresMeaningfulAltText() {
        assertFailsWith<IllegalArgumentException> {
            ProjectScreenshot(
                image = "/images/projects/sample-project/screen-01.jpg",
                alt = " "
            )
        }
    }

    @Test
    fun projectRejectsUnsafeScreenshotPathsAndExternalUrls() {
        assertFailsWith<IllegalArgumentException> {
            Project(
                slug = "sample-project",
                title = "Sample Project",
                category = "Mobile application",
                screenshots = listOf(
                    ProjectScreenshot(
                        image = "/images/projects/../private.png",
                        alt = "Sample Project account dashboard"
                    )
                )
            )
        }

        assertFailsWith<IllegalArgumentException> {
            Project(
                slug = "sample-project",
                title = "Sample Project",
                category = "Mobile application",
                caseStudyUrl = "javascript:alert(1)"
            )
        }
    }

    @Test
    fun serviceDecodesOptionalCapabilities() {
        val service = Json.decodeFromString<Service>(
            """
            {
              "id": "mobile-architecture",
              "title": "Mobile architecture",
              "description": "Architecture and implementation for mobile products."
            }
            """.trimIndent()
        )

        assertEquals(emptyList(), service.capabilities)
    }

    @Test
    fun serviceRejectsBlankRequiredFieldsAndCapabilities() {
        assertFailsWith<IllegalArgumentException> {
            Service(
                id = "mobile-architecture",
                title = " ",
                description = "Architecture and implementation."
            )
        }
        assertFailsWith<IllegalArgumentException> {
            Service(
                id = "mobile-architecture",
                title = "Mobile architecture",
                description = "Architecture and implementation.",
                capabilities = listOf(" ")
            )
        }
    }

    @Test
    fun experienceRequiresStartDateAndNonBlankImpactAndTechnologyItems() {
        assertFailsWith<IllegalArgumentException> {
            Experience(
                id = "experience",
                role = "Mobile Developer",
                organization = "Verified organization",
                startDate = " "
            )
        }
        assertFailsWith<IllegalArgumentException> {
            Experience(
                id = "experience",
                role = "Mobile Developer",
                organization = "Verified organization",
                startDate = "2024",
                achievements = listOf(" ")
            )
        }
        assertFailsWith<IllegalArgumentException> {
            Experience(
                id = "experience",
                role = "Mobile Developer",
                organization = "Verified organization",
                startDate = "2024",
                technologies = listOf(" ")
            )
        }
    }
}