package com.company.moviesapp.shared.data.local.datasource

import android.content.SharedPreferences

interface WatchLaterLocalDataSource {
    fun addToWatchLater(movieId: String)
    fun removeFromWatchLater(movieId: String)
    fun isInWatchLater(movieId: String): Boolean
}

class WatchLaterLocalDatasourceImpl(private val sharedPreferences: SharedPreferences) :
    WatchLaterLocalDataSource {

    // Add a movie ID to the "Watch Later" list
    override fun addToWatchLater(movieId: String) {
        val watchLaterSet = getWatchLaterSet().toMutableSet()
        watchLaterSet.add(movieId)
        sharedPreferences.edit().putStringSet("watch_later_ids", watchLaterSet).apply()
    }

    // Remove a movie ID from the "Watch Later" list
    override fun removeFromWatchLater(movieId: String) {
        val watchLaterSet = getWatchLaterSet().toMutableSet()
        watchLaterSet.remove(movieId)
        sharedPreferences.edit().putStringSet("watch_later_ids", watchLaterSet).apply()
    }

    // Check if a movie is in the "Watch Later" list
    override fun isInWatchLater(movieId: String): Boolean {
        return getWatchLaterSet().contains(movieId)
    }

    // Get the set of movie IDs in the "Watch Later" list
    private fun getWatchLaterSet(): Set<String> {
        return sharedPreferences.getStringSet("watch_later_ids", emptySet()) ?: emptySet()
    }
}