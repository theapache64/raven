package com.theapache64.raven.data.repos

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

/**
 * Created by theapache64 : Oct 12 Mon,2020 @ 22:32
 */
class PrefRepo @Inject constructor(
    private val ravenPref: SharedPreferences
) {
    companion object {
        private const val KEY_IS_AUTO_WALLPAPER_ON = "is_auto_wallpaper_on"
    }

    fun isAutoWallpaperOn() = ravenPref.getBoolean(KEY_IS_AUTO_WALLPAPER_ON, false)
    fun storeIsAutoWallpaper(newState: Boolean) {
        ravenPref.edit {
            putBoolean(KEY_IS_AUTO_WALLPAPER_ON, newState)
        }
    }

}