package com.example

import com.example.data.HeritageRepository
import com.example.model.AppLanguage
import com.example.model.CommunitySubmission
import com.example.ui.components.generateMonumentMesh
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testHeritageRepositoryInitialSites() {
        val sites = HeritageRepository.sites.value
        assertTrue("Sites should not be empty", sites.isNotEmpty())
        assertTrue("Should contain Raigad Fort", sites.any { it.id == "raigad-fort" })
        assertTrue("Should contain Ajanta Caves", sites.any { it.id == "ajanta-caves" })
        assertTrue("Should contain Ellora Caves", sites.any { it.id == "ellora-caves" })
        assertTrue("Should contain Gateway of India", sites.any { it.id == "gateway-of-india" })
    }

    @Test
    fun testMultilingualAudioGuides() {
        val sites = HeritageRepository.sites.value
        sites.forEach { site ->
            assertTrue("Site ${site.name} must have English transcript", site.audioContent.englishTranscript.isNotBlank())
            assertTrue("Site ${site.name} must have Marathi transcript", site.audioContent.marathiTranscript.isNotBlank())
            assertTrue("Site ${site.name} must have Hindi transcript", site.audioContent.hindiTranscript.isNotBlank())
        }
    }

    @Test
    fun test3DMeshGeneration() {
        val fortressMesh = generateMonumentMesh("fortress")
        assertTrue("Fortress mesh must have polygons", fortressMesh.isNotEmpty())

        val templeMesh = generateMonumentMesh("temple")
        assertTrue("Temple mesh must have polygons", templeMesh.isNotEmpty())

        val stupaMesh = generateMonumentMesh("stupa")
        assertTrue("Stupa mesh must have polygons", stupaMesh.isNotEmpty())

        val archMesh = generateMonumentMesh("arch")
        assertTrue("Arch mesh must have polygons", archMesh.isNotEmpty())
    }

    @Test
    fun testPreservationAnalytics() {
        val analytics = HeritageRepository.getAnalytics()
        assertTrue("Total sites must be positive", analytics.totalSites >= 8)
        assertTrue("AR enabled sites must be positive", analytics.arEnabledSites >= 8)
        assertTrue("Preservation rate must be between 0 and 100", analytics.activePreservationRate in 1..100)
    }

    @Test
    fun testSaveAndToggleSite() {
        val testId = "konark-sun-temple"
        HeritageRepository.toggleSaveSite(testId)
        assertTrue(HeritageRepository.savedSiteIds.value.contains(testId))

        HeritageRepository.toggleSaveSite(testId)
        assertFalse(HeritageRepository.savedSiteIds.value.contains(testId))
    }

    @Test
    fun testLanguageSelection() {
        HeritageRepository.setLanguage(AppLanguage.MARATHI)
        assertEquals(AppLanguage.MARATHI, HeritageRepository.currentLanguage.value)

        HeritageRepository.setLanguage(AppLanguage.ENGLISH)
        assertEquals(AppLanguage.ENGLISH, HeritageRepository.currentLanguage.value)
    }
}
