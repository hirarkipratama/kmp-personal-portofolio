package me.hirarki.pp.domain.repository

import kotlinx.serialization.json.Json
import me.hirarki.pp.domain.model.*

class PortfolioRepository {
    
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = false
    }
    
    suspend fun loadSiteContent(): Result<SiteContent> = runCatching {
        val jsonString = loadJsonResource("site.json")
        json.decodeFromString<SiteContent>(jsonString)
    }
    
    suspend fun loadProjects(): Result<List<Project>> = runCatching {
        val jsonString = loadJsonResource("projects.json")
        json.decodeFromString<List<Project>>(jsonString)
    }
    
    suspend fun loadSkillGroups(): Result<List<SkillGroup>> = runCatching {
        val jsonString = loadJsonResource("skills.json")
        json.decodeFromString<List<SkillGroup>>(jsonString)
    }
    
    suspend fun loadExperience(): Result<List<Experience>> = runCatching {
        val jsonString = loadJsonResource("experience.json")
        json.decodeFromString<List<Experience>>(jsonString)
    }
    
    suspend fun loadSocialLinks(): Result<List<SocialLink>> = runCatching {
        val jsonString = loadJsonResource("links.json")
        json.decodeFromString<List<SocialLink>>(jsonString)
    }
    
    private fun loadJsonResource(filename: String): String {
        return """
            {
                "error": "Content not yet loaded - placeholder for $filename"
            }
        """.trimIndent()
    }
}
