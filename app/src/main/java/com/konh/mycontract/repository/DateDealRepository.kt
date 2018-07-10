package com.konh.mycontract.repository

import android.util.Log
import com.konh.mycontract.model.DateDealModel
import com.konh.mycontract.model.DealModel
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
                    .map { createFromDeal(it) }.toMutableList()
            dateDeals.addAll(history.map { createFromHistory(it) })
        return dateDeals
    }

    private fun createFromDeal(it : DealModel) : DateDealModel {
        return DateDealModel(it.id, it.name, it.score, !dateRepo.isPastTime)
    }

    private fun createFromHistory(it : HistoryModel) : DateDealModel {
        return DateDealModel(it.dealId, it.name, it.score, false)
    }

    fun doneDeal(deal:DateDealModel) {
        val date = dateRepo.getCurrent()
        val item = HistoryModel(0, date, deal.dealId, deal.name, deal.score)
        historyRepo.addItem(item)
    }
}