package com.konh.mycontract.repository

import com.konh.mycontract.model.HistoryAggregateModel
import com.konh.mycontract.utils.calendarToStringShort
import com.konh.mycontract.utils.resetToDayStart

class HistoryAggregateRepository(private val history:HistoryRepository) {

    fun getAll() : List<HistoryAggregateModel> {
        val allHistory = history.getAll()
        val map = mutableMapOf<Long, HistoryAggregateModel>()
        allHistory.forEach {
            val day = resetToDayStart(it.day)
            val key = day.time.time
            val model = map.getOrPut(key, { HistoryAggregateModel(day, calendarToStringShort(day), 0) } )
            model.score += it.score
        }
        return map.map { it.value }
    }
}