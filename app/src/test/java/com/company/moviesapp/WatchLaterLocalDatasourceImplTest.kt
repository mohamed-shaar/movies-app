package com.company.moviesapp

import android.content.SharedPreferences
import com.company.moviesapp.shared.data.local.datasource.WatchLaterLocalDatasourceImpl
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.anySet
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class WatchLaterLocalDatasourceImplTest {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesEditor: SharedPreferences.Editor
    private lateinit var watchLaterLocalDatasource: WatchLaterLocalDatasourceImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        sharedPreferences = mock(SharedPreferences::class.java)
        sharedPreferencesEditor = mock(SharedPreferences.Editor::class.java)
        watchLaterLocalDatasource = WatchLaterLocalDatasourceImpl(sharedPreferences)

        // Mock the behavior of SharedPreferences and its Editor
        `when`(sharedPreferences.edit()).thenReturn(sharedPreferencesEditor)
        `when`(sharedPreferencesEditor.putStringSet(anyString(), anySet())).thenReturn(
            sharedPreferencesEditor
        )
    }

    @Test
    fun `addToWatchLater should add movieId to watch later list`() {
        // Arrange
        val movieId = "123"
        val watchLaterSet = mutableSetOf<String>()
        `when`(sharedPreferences.getStringSet("watch_later_ids", emptySet())).thenReturn(
            watchLaterSet
        )

        // Act
        watchLaterLocalDatasource.addToWatchLater(movieId)

        // Assert
        verify(sharedPreferencesEditor).putStringSet("watch_later_ids", setOf(movieId))
        verify(sharedPreferencesEditor).apply()
    }

    @Test
    fun `removeFromWatchLater should remove movieId from watch later list`() {
        // Arrange
        val movieId = "123"
        val watchLaterSet = mutableSetOf(movieId)
        `when`(sharedPreferences.getStringSet("watch_later_ids", emptySet())).thenReturn(
            watchLaterSet
        )

        // Act
        watchLaterLocalDatasource.removeFromWatchLater(movieId)

        // Assert
        verify(sharedPreferencesEditor).putStringSet("watch_later_ids", emptySet())
        verify(sharedPreferencesEditor).apply()
    }

    @Test
    fun `isInWatchLater should return true if movieId is in watch later list`() {
        // Arrange
        val movieId = "123"
        val watchLaterSet = setOf(movieId)
        `when`(sharedPreferences.getStringSet("watch_later_ids", emptySet())).thenReturn(
            watchLaterSet
        )

        // Act
        val result = watchLaterLocalDatasource.isInWatchLater(movieId)

        // Assert
        assertTrue(result)
    }

    @Test
    fun `isInWatchLater should return false if movieId is not in watch later list`() {
        // Arrange
        val movieId = "123"
        val watchLaterSet = setOf("456")
        `when`(sharedPreferences.getStringSet("watch_later_ids", emptySet())).thenReturn(
            watchLaterSet
        )

        // Act
        val result = watchLaterLocalDatasource.isInWatchLater(movieId)

        // Assert
        assertFalse(result)
    }
}