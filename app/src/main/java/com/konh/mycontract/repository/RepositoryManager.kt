package com.konh.mycontract.repository

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.konh.mycontract.R
import com.konh.mycontract.database.DealDatabase
import java.util.*

class RepositoryManager(db:DealDatabase, prefs:SharedPreferences) {
    private val history = HistoryRepository(db.historyDao())
    val date = DateRepository()
    val deal = DealRepository(db.dealDao())
    val dateDeal = DateDealRepository(date, deal, history)
    val settings = SettingsRepository(prefs)
    val scores = ScoresRepository(history, settings)
    val historyAggregate = HistoryAggregateRepository(history, settings)

    companion object {
        private var Instance: RepositoryManager? = null

        fun getInstance(context: Context): RepositoryManager? {
            if (Instance == null) {
                synchronized(RepositoryManager::class) {
                    val db = DealDatabase.getInstance(context)
                    val prefs = context.getSharedPreferences(context.getString(R.string.file_settings_prefs), Context.MODE_PRIVATE)
                    if ( db != null ) {
                        Instance = RepositoryManager(db, prefs)
                    }
                }
            }
            return Instance
        }

        fun destroyInstance() {
            Instance = null
        }
    }
}

fun Activity.getRepo() : RepositoryManager? {
    return RepositoryManager.getInstance(applicationContext)
}