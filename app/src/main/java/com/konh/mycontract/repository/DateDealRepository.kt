package com.konh.mycontract.repository

import android.util.Log
import com.konh.mycontract.model.DateDealModel
import com.konh.mycontract.model.HistoryModel

class DateDealRepository(private val dateRepo:DateRepository, private val dealRepo:DealRepository, private val historyRepo:HistoryRepository) {
    private val logTag = "DateDealRepository"

    fun getAll() : List<DateDealModel> {
        val deals = dealRepo.getAll()
        val date = dateRepo.getCurrent()
        val history = historyRepo.getOnDay(date)
        Log.d(logTag, "getAll: deals: ${deals.size}, history: ${history.size}")
        deals.forEach { Log.d(logTag, "getAll: deal: $it") }
        history.forEach { Log.d(logTag, "getAll: history: $it") }
        val dateDeals = deals
                    .filter { d -> !history.any { h -> h.dealId == d.id } }
                    .map { DateDealModel(it.id, it.name, it.score, true) }.toMutableList()
            dateDeals.addAll(history.map { DateDealModel(it.dealId, it.name, it.score, false) })
        return dateDeals
    }

    fun doneDeal(deal:DateDealModel) {
        val date = dateRepo.getCurrent()
        val item = HistoryModel(0, date, deal.dealId, deal.name, deal.score)
        historyRepo.addItem(item)
    }
}