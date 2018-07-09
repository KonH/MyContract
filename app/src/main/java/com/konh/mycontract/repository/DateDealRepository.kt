package com.konh.mycontract.repository

import android.util.Log
import com.konh.mycontract.model.DateDealModel
import com.konh.mycontract.model.HistoryModel
import java.util.*

class DateDealRepository(private val dealRepository:DealRepository, private val historyRepository:HistoryRepository) {
    private val logTag = "DateDealRepository"

    fun getAll() : List<DateDealModel> {
        val deals = dealRepository.getAll()
        val history = historyRepository.getAll()
        Log.d(logTag, "getAll: deals: ${deals.size}, history: ${history.size}")
        deals.forEach { Log.d(logTag, "updateTodayDeals: deal: $it") }
        history.forEach { Log.d(logTag, "updateTodayDeals: history: $it") }
        val dateDeals = deals
                    .filter { d -> !history.any { h -> h.dealId == d.id } }
                    .map { it -> DateDealModel(it.id, it.name, it.score, true) }.toMutableList()
            dateDeals.addAll(history.map { it -> DateDealModel(it.dealId, it.name, it.score, false) })
        return dateDeals
    }

    fun doneDeal(deal:DateDealModel) {
        val item = HistoryModel(0, Calendar.getInstance().time, deal.dealId, deal.name, deal.score)
        historyRepository.addItem(item)
    }
}