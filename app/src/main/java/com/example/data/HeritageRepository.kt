package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object HeritageRepository {

    private val _sites = MutableStateFlow<List<HeritageSite>>(initialSites)
    val sites: StateFlow<List<HeritageSite>> = _sites.asStateFlow()

    private val _savedSiteIds = MutableStateFlow<Set<String>>(setOf("raigad-fort", "ellora-caves"))
    val savedSiteIds: StateFlow<Set<String>> = _savedSiteIds.asStateFlow()

    private val _completedArSiteIds = MutableStateFlow<Set<String>>(setOf("raigad-fort"))
    val completedArSiteIds: StateFlow<Set<String>> = _completedArSiteIds.asStateFlow()

    private val _archives = MutableStateFlow<List<ArchiveItem>>(initialArchives)
    val archives: StateFlow<List<ArchiveItem>> = _archives.asStateFlow()

    private val _submissions = MutableStateFlow<List<CommunitySubmission>>(initialSubmissions)
    val submissions: StateFlow<List<CommunitySubmission>> = _submissions.asStateFlow()

    private val _quizQuestions = MutableStateFlow<List<QuizQuestion>>(initialQuizQuestions)
    val quizQuestions: StateFlow<List<QuizQuestion>> = _quizQuestions.asStateFlow()

    private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _userQuizScore = MutableStateFlow(0)
    val userQuizScore: StateFlow<Int> = _userQuizScore.asStateFlow()

    private val _userRole = MutableStateFlow("Heritage Explorer") // Explorer, Student, Researcher, Admin
    val userRole: StateFlow<String> = _userRole.asStateFlow()

    fun setLanguage(lang: AppLanguage) {
        _currentLanguage.value = lang
    }

    fun setUserRole(role: String) {
        _userRole.value = role
    }

    fun toggleSaveSite(siteId: String) {
        _savedSiteIds.update { set ->
            if (set.contains(siteId)) set - siteId else set + siteId
        }
    }

    fun markArCompleted(siteId: String) {
        _completedArSiteIds.update { it + siteId }
        _sites.update { list ->
            list.map { if (it.id == siteId) it.copy(arLaunches = it.arLaunches + 1) else it }
        }
    }

    fun recordQuizScore(score: Int) {
        _userQuizScore.update { maxOf(it, score) }
    }

    fun addCommunitySubmission(submission: CommunitySubmission) {
        _submissions.update { listOf(submission) + it }
    }

    fun updateSubmissionStatus(id: String, newStatus: String) {
        _submissions.update { list ->
            list.map { if (it.id == id) it.copy(status = newStatus) else it }
        }
    }

    fun addHeritageSite(site: HeritageSite) {
        _sites.update { listOf(site) + it }
    }

    fun getAnalytics(): PreservationAnalytics {
        val allSites = _sites.value
        return PreservationAnalytics(
            totalSites = allSites.size,
            arEnabledSites = allSites.count { it.isArAvailable },
            models3dCount = allSites.count { it.is3dAvailable },
            digitalDocuments = _archives.value.size + 142,
            audioGuidesCount = allSites.count { it.isAudioAvailable },
            totalUserVisits = allSites.sumOf { it.viewsCount } + 8900,
            arExperiencesCount = allSites.sumOf { it.arLaunches } + 3420,
            quizTakersCount = 2840,
            activePreservationRate = 94
        )
    }
}
