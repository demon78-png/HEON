package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.LocalGameCatalogRepository
import com.example.data.SortOption
import com.example.ui.HeonViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun testAppNameResource() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("HEON", appName)
    }

    @Test
    fun testCatalogRepositorySearchAndFilter() = runTest {
        val repository = LocalGameCatalogRepository()

        // Test featured games
        val featured = repository.getFeaturedGames()
        assertTrue("Featured games must not be empty", featured.isNotEmpty())

        // Test search query
        val searchResults = repository.searchAndFilterGames(query = "CyberBlade")
        assertEquals(1, searchResults.size)
        assertEquals("game_01", searchResults.first().id)

        // Test category filtering
        val racingGames = repository.searchAndFilterGames(categoryId = "racing")
        assertTrue("Should find racing games", racingGames.isNotEmpty())
        assertTrue(racingGames.all { it.genres.any { g -> g.contains("Racing", ignoreCase = true) } })

        // Test sorting by rating
        val sortedByRating = repository.searchAndFilterGames(sortBy = SortOption.RATING_HIGH)
        assertTrue(sortedByRating.first().rating >= sortedByRating.last().rating)
    }

    @Test
    fun testViewModelDownloadAndInstallLifecycle() = runTest {
        val repository = LocalGameCatalogRepository()
        val viewModel = HeonViewModel(repository)

        val game = repository.getGameById("game_02")
        assertNotNull(game)

        // Start download
        viewModel.startDownload(game!!)
        assertTrue(viewModel.uiState.value.activeDownloads.containsKey("game_02"))

        // Pause download
        viewModel.pauseDownload("game_02")
        assertEquals(com.example.model.DownloadState.PAUSED, viewModel.uiState.value.activeDownloads["game_02"]?.state)

        // Resume download
        viewModel.resumeDownload("game_02")
        assertEquals(com.example.model.DownloadState.DOWNLOADING, viewModel.uiState.value.activeDownloads["game_02"]?.state)

        // Cancel download
        viewModel.cancelDownload("game_02")
        assertFalse(viewModel.uiState.value.activeDownloads.containsKey("game_02"))

        // Toggle wishlist on game_04
        assertFalse(viewModel.uiState.value.wishlistGameIds.contains("game_04"))
        viewModel.toggleWishlist("game_04")
        assertTrue(viewModel.uiState.value.wishlistGameIds.contains("game_04"))
        viewModel.toggleWishlist("game_04")
        assertFalse(viewModel.uiState.value.wishlistGameIds.contains("game_04"))
    }

    @Test
    fun testProfileUpdateAndCloudSessionLifecycle() = runTest {
        val repository = LocalGameCatalogRepository()
        val viewModel = HeonViewModel(repository)

        // Test Profile Update
        viewModel.updateProfile(displayName = "CyberPilotX", username = "Pilot99")
        assertEquals("CyberPilotX", viewModel.uiState.value.userProfile.displayName)
        assertEquals("Pilot99", viewModel.uiState.value.userProfile.username)

        // Test Cloud Gaming Session
        val game = repository.getGameById("game_01")
        assertNotNull(game)
        viewModel.startCloudSession(game!!)
        assertEquals(com.example.ui.SecondaryScreen.CLOUD_SESSION, viewModel.uiState.value.secondaryScreen)
        assertNotNull(viewModel.uiState.value.activeCloudSession)
        assertEquals("game_01", viewModel.uiState.value.activeCloudSession?.gameId)

        // Test Ending Cloud Session
        viewModel.endCloudSession()
        assertEquals(com.example.ui.SecondaryScreen.NONE, viewModel.uiState.value.secondaryScreen)
        assertEquals(null, viewModel.uiState.value.activeCloudSession)
    }
}
