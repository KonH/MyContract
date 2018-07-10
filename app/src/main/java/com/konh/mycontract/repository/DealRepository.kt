package com.konh.mycontract.repository

import android.util.Log
import com.konh.mycontract.dao.DealDao
import com.konh.mycontract.model.DealModel

class DealRepository(private val dao:DealDao) {
    private val logTag = "DealRepository"

    fun getAll():List<DealModel> {
        return dao.getAll()
    }

    fun addDeal(deal: DealModel) {
        Log.d(logTag, "addDeal: $deal")
        dao.insert(deal)
    }
}