package me.hirarki.pp

import kotlinx.serialization.json.Json
import me.hirarki.pp.domain.model.SiteContent
import kotlin.test.Test
import kotlin.test.assertEquals

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
    }
}