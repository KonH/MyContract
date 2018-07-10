package com.konh.mycontract.repository

import android.util.Log
import com.konh.mycontract.dao.HistoryDao
import com.konh.mycontract.model.HistoryModel
import com.konh.mycontract.utils.calendarToString
import com.konh.mycontract.utils.nextDay
import com.konh.mycontract.utils.resetToDayStart
import java.util.*

class HistoryRepository(private val dao: HistoryDao) {
    private val logTag = "HistoryRepository"

    fun getAll() : List<HistoryModel> {
        return dao.getAll()
    }

    fun getOnDay(day: Calendar) : List<HistoryModel> {
        val startDay = resetToDayStart(day)
        val endDay = nextDay(startDay)
        Log.d(logTag, "getOnDay: ${calendarToString(day)} => ${calendarToString(startDay)}-${calendarToString(endDay)}")
        return getAll().filter { (it.day.time >= startDay.time) && (it.day.time < endDay.time) }
    }

    fun addItem(item:HistoryModel) {
        Log.d(logTag, "addItem: $item")
        dao.insert(item)
    }
}