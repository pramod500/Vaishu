package com.example.model

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    MARATHI("mr", "Marathi", "मराठी"),
    HINDI("hi", "Hindi", "हिन्दी")
}

data class Hotspot(
    val id: String,
    val title: String,
    val description: String,
    val x: Float, // -1.0 to 1.0
    val y: Float,
    val z: Float
)

data class AudioContent(
    val englishTranscript: String,
    val marathiTranscript: String,
    val hindiTranscript: String,
    val durationSeconds: Int = 180
)

data class TimelineEvent(
    val year: String,
    val event: String,
    val era: String
)

data class HeritageSite(
    val id: String,
    val name: String,
    val nativeName: String,
    val location: String,
    val district: String,
    val state: String,
    val country: String = "India",
    val latitude: Double,
    val longitude: Double,
    val period: String,
    val century: String,
    val category: String,
    val monumentType: String,
    val shortDescription: String,
    val historyDetail: String,
    val architectureDetail: String,
    val culturalImportance: String,
    val importantFacts: List<String>,
    val timelineEvents: List<TimelineEvent>,
    val preservationStatus: String,
    val preservationScore: Int, // e.g. 88 / 100
    val imageUrl: String,
    val meshType: String, // "fortress", "temple", "stupa", "arch", "cave", "chariot"
    val hotspots: List<Hotspot>,
    val audioContent: AudioContent,
    val qrIdentifier: String,
    val isArAvailable: Boolean = true,
    val is3dAvailable: Boolean = true,
    val isAudioAvailable: Boolean = true,
    val viewsCount: Int = 1240,
    val arLaunches: Int = 340
)

data class ArchiveItem(
    val id: String,
    val title: String,
    val type: String, // "Historical Photograph", "Ancient Manuscript", "Old Cartographic Map", "Rock Inscription", "Oral Ballad / Tradition"
    val yearOrEra: String,
    val description: String,
    val location: String,
    val preservationCondition: String,
    val archiveCode: String,
    val relatedSiteId: String? = null
)

data class QuizQuestion(
    val id: String,
    val siteName: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class CommunitySubmission(
    val id: String,
    val contributorName: String,
    val contributorRole: String, // "Student", "Archaeologist", "Local Historian", "Tourist"
    val title: String,
    val category: String,
    val location: String,
    val period: String,
    val description: String,
    val date: String,
    val status: String // "Pending Review", "Approved & Archived", "Needs Field Verification"
)

data class PreservationAnalytics(
    val totalSites: Int,
    val arEnabledSites: Int,
    val models3dCount: Int,
    val digitalDocuments: Int,
    val audioGuidesCount: Int,
    val totalUserVisits: Int,
    val arExperiencesCount: Int,
    val quizTakersCount: Int,
    val activePreservationRate: Int
)
