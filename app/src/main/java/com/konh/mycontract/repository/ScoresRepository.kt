package com.konh.mycontract.repository

import android.util.Log
import com.konh.mycontract.model.ScoresModel
import com.konh.mycontract.model.TotalScoresModel
import java.util.*

class ScoresRepository(private val history:HistoryRepository, private val settings:SettingsRepository) {
    private val logTag = "ScoresRepository"

    fun getTotalScores() : TotalScoresModel {
        val allScores = history.getAll().sumBy { it.score }
        val scores = TotalScoresModel(allScores)
        Log.d(logTag, "getTotalScores: $scores")
        return scores
    }

    fun getDayScores(day: Calendar) : ScoresModel {
        val dayScores = history.getOnDay(day).sumBy { it.score }
        val maxScores = settings.get().dayScore
        val scores = ScoresModel(dayScores, maxScores)
        Log.d(logTag, "getDayScores: $scores")
        return scores
    }
}