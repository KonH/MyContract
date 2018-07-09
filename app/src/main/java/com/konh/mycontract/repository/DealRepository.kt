package com.konh.mycontract.repository

import android.util.Log
import com.konh.mycontract.dao.DealDao
import com.konh.mycontract.model.DealModel

class DealRepository(private val dealDao:DealDao) {
    private val logTag = "DealRepository"

    fun getAll():List<DealModel> {
        return dealDao.getAll()
    }

    fun addDeal(deal: DealModel) {
        Log.d(logTag, "addDeal: $deal")
        dealDao.insert(deal)
    }
}