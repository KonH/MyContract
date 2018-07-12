package com.konh.mycontract

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import android.widget.ListView
import com.konh.mycontract.adapter.HistoryAggregateAdapter
import com.konh.mycontract.databinding.ActivityHistoryBinding
import com.konh.mycontract.model.HistoryAggregateModel
import com.konh.mycontract.repository.getRepo
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class HistoryActivity : AppCompatActivity() {
    private val historyAdapter = HistoryAggregateAdapter(this, emptyList(), { clickItem(it) })

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

        updateState()
    }

    private fun updateState() {
        doAsync {
            val repo = getRepo()
            if ( repo != null ) {
                historyBinding.scores = repo.scores.getTotalScores()
                val aggregates = repo.historyAggregate.getAll()
                uiThread {
                    historyBinding.executePendingBindings()
                    historyAdapter.updateItems(aggregates)
                }
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
