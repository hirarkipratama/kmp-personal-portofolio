package me.hirarki.pp

import kotlinx.serialization.json.Json
import me.hirarki.pp.domain.model.AboutContent
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
}