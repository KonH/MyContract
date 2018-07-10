package com.konh.mycontract.repository

import com.konh.mycontract.database.DealDatabase
import java.util.*

class RepositoryManager(db:DealDatabase) {
    val date = DateRepository(Calendar.getInstance())
    val deal = DealRepository(db.dealDao())
    val history = HistoryRepository(db.historyDao())
    val dateDeal = DateDealRepository(date, deal, history)
}