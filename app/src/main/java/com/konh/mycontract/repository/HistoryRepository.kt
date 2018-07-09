package com.konh.mycontract.repository

import android.util.Log
import com.konh.mycontract.dao.HistoryDao
import com.konh.mycontract.model.HistoryModel

class HistoryRepository(private val dao: HistoryDao) {
    private val logTag = "HistoryRepository"

    fun getAll() : List<HistoryModel> {
        return dao.getAll()
    }

    fun addItem(item:HistoryModel) {
        Log.d(logTag, "addItem: $item")
        dao.insert(item)
    }
}