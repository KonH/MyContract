package com.konh.mycontract.repository

import android.content.SharedPreferences
import android.util.Log
import com.konh.mycontract.model.SettingsModel

class SettingsRepository(private val prefs:SharedPreferences) {
    private val dayScoresKey = "settings_day_scores"
    private val logTag = "SettingsRepository"

    fun get() : SettingsModel {
        val dayScores = prefs.getInt(dayScoresKey, 10)
        val current = SettingsModel(dayScores)
        Log.d(logTag, "get: $current")
        return current
    }

    fun update(settings:SettingsModel) {
        Log.d(logTag, "update: $settings")
        with (prefs.edit()) {
            putInt(dayScoresKey, settings.dayScore)
            apply()
        }
    }
}