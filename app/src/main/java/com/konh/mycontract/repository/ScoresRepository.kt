package com.konh.mycontract.repository

import android.util.Log
import com.konh.mycontract.model.ScoresModel
import com.konh.mycontract.model.TotalScoresModel
import java.util.*

class ScoresRepository(private val history:HistoryRepository) {
    private val logTag = "ScoresRepository"

    fun getTotalScores() : TotalScoresModel {
        val allScores = history.getAll().sumBy { it.score }
        val scores = TotalScoresModel(allScores)
        Log.d(logTag, "getTotalScores: $scores")
        return scores
    }

    fun getDayScores(day: Calendar) : ScoresModel {
        val dayScores = history.getOnDay(day).sumBy { it.score }
        val scores = ScoresModel(dayScores, 10) // TODO: set & read max scores
        Log.d(logTag, "getDayScores: $scores")
        return scores
    }
}