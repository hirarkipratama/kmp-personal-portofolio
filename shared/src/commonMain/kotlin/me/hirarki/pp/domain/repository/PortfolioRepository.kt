package me.hirarki.pp.domain.repository

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import me.hirarki.pp.domain.model.*

@Serializable
private data class ProjectsWrapper(val projects: List<Project>)

@Serializable
private data class SkillGroupsWrapper(val skillGroups: List<SkillGroup>)

@Serializable
private data class ExperienceWrapper(val experience: List<Experience>)

@Serializable
private data class ServicesWrapper(val services: List<Service>)

@Serializable
private data class SocialLinksWrapper(val socialLinks: List<SocialLink>)

class PortfolioRepository {
    
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = false
    }
    
    suspend fun loadSiteContent(): Result<SiteContent> = runCatching {
        val jsonString = fetchContentJson("site.json")
        json.decodeFromString<SiteContent>(jsonString)
    }
    
    suspend fun loadProjects(): Result<List<Project>> = runCatching {
        val jsonString = fetchContentJson("projects.json")
        val wrapper = json.decodeFromString<ProjectsWrapper>(jsonString)
        wrapper.projects
    }
    
    suspend fun loadSkillGroups(): Result<List<SkillGroup>> = runCatching {
        val jsonString = fetchContentJson("skills.json")
        val wrapper = json.decodeFromString<SkillGroupsWrapper>(jsonString)
        wrapper.skillGroups
    }
    
    suspend fun loadExperience(): Result<List<Experience>> = runCatching {
        val jsonString = fetchContentJson("experience.json")
        val wrapper = json.decodeFromString<ExperienceWrapper>(jsonString)
        wrapper.experience
    }

    suspend fun loadServices(): Result<List<Service>> = runCatching {
        val jsonString = fetchContentJson("services.json")
        val wrapper = json.decodeFromString<ServicesWrapper>(jsonString)
        wrapper.services
    }

    suspend fun loadSocialLinks(): Result<List<SocialLink>> = runCatching {
        val jsonString = fetchContentJson("links.json")
        val wrapper = json.decodeFromString<SocialLinksWrapper>(jsonString)
        wrapper.socialLinks
    }
}
