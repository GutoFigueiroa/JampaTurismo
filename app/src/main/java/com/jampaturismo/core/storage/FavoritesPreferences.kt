package com.jampaturismo.core.storage

import android.content.Context
import android.content.SharedPreferences

class FavoritesPreferences(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getFavoriteIds(): Set<String> {
        return prefs.getStringSet(KEY_FAVORITES, emptySet())?.toSet() ?: emptySet()
    }

    fun isFavorite(placeId: String): Boolean {
        return getFavoriteIds().contains(placeId)
    }

    fun toggleFavorite(placeId: String): Boolean {
        val currentFavorites = getFavoriteIds().toMutableSet()
        val isNowFavorite = if (currentFavorites.contains(placeId)) {
            currentFavorites.remove(placeId)
            false
        } else {
            currentFavorites.add(placeId)
            true
        }
        prefs.edit().putStringSet(KEY_FAVORITES, currentFavorites).apply()
        return isNowFavorite
    }

    companion object {
        private const val PREFS_NAME = "jampa_turismo_prefs"
        private const val KEY_FAVORITES = "favorite_places_ids"
    }
}
