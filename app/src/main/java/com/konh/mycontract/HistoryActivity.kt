package com.konh.mycontract

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import com.konh.mycontract.adapter.HistoryAggregateAdapter
import com.konh.mycontract.database.DealDatabase
import com.konh.mycontract.databinding.ActivityHistoryBinding
import com.konh.mycontract.model.HistoryAggregateModel
import com.konh.mycontract.repository.RepositoryManager
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class HistoryActivity : AppCompatActivity() {
    private val historyAdapter = HistoryAggregateAdapter(this, emptyList(), { clickItem(it) })

    private lateinit var repo: RepositoryManager
    private lateinit var historyBinding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        historyBinding = DataBindingUtil.setContentView(this, R.layout.activity_history)

        val historyListView = findViewById<ListView>(R.id.list_history)
        historyListView.isClickable = true
        historyListView.onItemClickListener = AdapterView.OnItemClickListener {
            _, _, position, _ ->
            val item = historyAdapter.getItem(position)
            if ( item is HistoryAggregateModel ) {
                clickItem(item)
            }

        }
        historyListView?.adapter = historyAdapter

        initRepository()
    }

    private fun initRepository() {
        val db = DealDatabase.getInstance(this)
        if ( db != null ) {
            repo = RepositoryManager(db, Calendar.getInstance(), false, getSharedPreferences(getString(R.string.file_settings_prefs), Context.MODE_PRIVATE))
            updateState()
        }
    }

    private fun updateState() {
        doAsync {
            historyBinding.scores = repo.scores.getTotalScores()
            val aggregates = repo.historyAggregate.getAll()
            uiThread {
                historyBinding.executePendingBindings()
                historyAdapter.updateItems(aggregates)
            }
        }
    }

    private fun clickItem(item: HistoryAggregateModel) {
        goToMainActivity(item.day)
    }

    private fun goToMainActivity(day:Calendar) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(getString(R.string.concrete_day_arg), day)
        startActivity(intent)
    }
}
