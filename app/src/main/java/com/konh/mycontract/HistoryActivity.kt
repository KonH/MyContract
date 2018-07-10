package com.konh.mycontract

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.konh.mycontract.adapter.HistoryAggregateAdapter
import com.konh.mycontract.database.DealDatabase
import com.konh.mycontract.databinding.ActivityHistoryBinding
import com.konh.mycontract.model.HistoryAggregateModel
import com.konh.mycontract.repository.RepositoryManager
import com.konh.mycontract.utils.WorkerThread

class HistoryActivity : AppCompatActivity() {
    private val workerThread = WorkerThread("dbThread")
    private val historyAdapter = HistoryAggregateAdapter(this, emptyList(), { clickItem(it) })

    private lateinit var repo: RepositoryManager
    private lateinit var historyBinding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        workerThread.start()

        historyBinding = DataBindingUtil.setContentView(this, R.layout.activity_history)

        val historyListView = findViewById<ListView>(R.id.list_history)
        historyListView?.adapter = historyAdapter

        initRepository()
    }

    private fun initRepository() {
        val db = DealDatabase.getInstance(this)
        if ( db != null ) {
            repo = RepositoryManager(db)
            updateState()
        }
    }

    private fun updateState() {
        workerThread.postTask(Runnable {
            historyBinding.scores = repo.scores.getTotalScores()
            val aggregates = repo.historyAggregate.getAll()
            runOnUiThread {
                historyBinding.executePendingBindings()
                historyAdapter.updateItems(aggregates)
            }
        })
    }

    private fun clickItem(item: HistoryAggregateModel) {
        // TODO
    }
}
