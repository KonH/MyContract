package com.konh.mycontract.repository

import android.util.Log
import com.konh.mycontract.model.HistoryAggregateModel
import com.konh.mycontract.utils.calendarToStringShort
import com.konh.mycontract.utils.resetToDayStart

class HistoryAggregateRepository(private val history:HistoryRepository, private val settings:SettingsRepository) {
    private val logTag = "HistoryAggregateRepo"

    fun getAll() : List<HistoryAggregateModel> {
        val allHistory = history.getAll()
        val map = mutableMapOf<Long, HistoryAggregateModel>()
        allHistory.forEach {
            val day = resetToDayStart(it.day)
            val key = day.time.time
            val model = map.getOrPut(key, { HistoryAggregateModel(day, calendarToStringShort(day), 0, settings.get().dayScore) } )
            map[key] = model.copy(curScore = model.curScore + it.score)
        }
        map.forEach({ Log.d(logTag, "getAll: ${it.value} (${it.value.normalized})")})
        return map.map { it.value }
    }
}