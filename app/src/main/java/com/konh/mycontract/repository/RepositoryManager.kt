package com.konh.mycontract.repository

import com.konh.mycontract.database.DealDatabase
import java.util.*

class RepositoryManager(db:DealDatabase, curDate:Calendar, pastTime:Boolean) {
    private val history = HistoryRepository(db.historyDao())
    val date = DateRepository(curDate, pastTime)
    val deal = DealRepository(db.dealDao())
    val dateDeal = DateDealRepository(date, deal, history)
    val scores = ScoresRepository(history)
    val historyAggregate = HistoryAggregateRepository(history)
}