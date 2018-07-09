package com.konh.mycontract.repository

import com.konh.mycontract.database.DealDatabase

class RepositoryManager(private val db:DealDatabase) {
    val deal:DealRepository = DealRepository(db.dealDao())
    val history:HistoryRepository = HistoryRepository(db.historyDao())
    val dateDeal:DateDealRepository = DateDealRepository(deal, history)
}