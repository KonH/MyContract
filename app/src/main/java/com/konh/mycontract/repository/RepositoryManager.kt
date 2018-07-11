package com.konh.mycontract.repository

import android.content.SharedPreferences
import com.konh.mycontract.database.DealDatabase
import java.util.*

class RepositoryManager(db:DealDatabase, curDate:Calendar, pastTime:Boolean, prefs:SharedPreferences) {
    private val history = HistoryRepository(db.historyDao())
    val date = DateRepository(curDate, pastTime)
    val deal = DealRepository(db.dealDao())
    val dateDeal = DateDealRepository(date, deal, history)
    val settings = SettingsRepository(prefs)
    val scores = ScoresRepository(history, settings)
    val historyAggregate = HistoryAggregateRepository(history)
}