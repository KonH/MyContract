package com.konh.mycontract.repository

import android.util.Log
import com.konh.mycontract.model.ScoresModel
import java.util.*

class ScoresRepository(private val history:HistoryRepository) {
    private val logTag = "ScoresRepository"

    fun getDayScores(day: Calendar) : ScoresModel {
        val dayScores = history.getOnDay(day).sumBy { it.score }
        val scores = ScoresModel(dayScores, 10) // TODO: set & read max scores
        Log.d(logTag, "getDayScores: $scores")
        return scores
    }
}