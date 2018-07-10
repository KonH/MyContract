package com.konh.mycontract

import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.konh.mycontract.adapter.DateDealAdapter
import com.konh.mycontract.database.DealDatabase
import com.konh.mycontract.databinding.ActivityMainBinding
import com.konh.mycontract.model.DealModel
import com.konh.mycontract.model.DateDealModel
import com.konh.mycontract.model.ScoresModel
import com.konh.mycontract.repository.RepositoryManager
import com.konh.mycontract.utils.WorkerThread

class MainActivity : AppCompatActivity() {
    private val workerThread = WorkerThread("dbThread")
    private val dealAdapter = DateDealAdapter(this, emptyList(), { doneDeal(it) })

    private lateinit var repo: RepositoryManager
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        workerThread.start()

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val dealListView = findViewById<ListView>(R.id.list_deals)
        dealListView?.adapter = dealAdapter

        initRepository()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_add_deal -> startCreatingDeal()
            R.id.action_go_to_settings -> goToSettings()
            R.id.action_go_to_history -> goToHistory()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startCreatingDeal() {
        val layout = LinearLayout(this)
        val nameEditText = EditText(this)
        nameEditText.hint = getString(R.string.add_deal_hint_title)
        val priceEditText = EditText(this)
        priceEditText.inputType = InputType.TYPE_CLASS_NUMBER
        priceEditText.hint = getString(R.string.add_deal_hint_price)
        layout.addView(nameEditText)
        layout.addView(priceEditText)
        val dialog = AlertDialog.Builder(this)
                .setTitle(getString(R.string.add_deal_header))
                .setView(layout)
                .setPositiveButton(getString(R.string.add_deal_ok_button), { _, _ ->
                    val newDealText = nameEditText.text.toString()
                    val newDealPrice = priceEditText.text.toString().toInt()
                    val message = "New deal: '$newDealText'"
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    val newDeal = DealModel(0, newDealText, newDealPrice )
                    addDeal(newDeal)
                })
                .setNegativeButton(getString(R.string.add_deal_cancel_button), null)
                .create()
        dialog.show()
    }

    private fun goToSettings() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun goToHistory() {
        startActivity(Intent(this, HistoryActivity::class.java))
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
           mainBinding.scores = repo.scores.getDayScores(repo.date.getCurrent())
            val dateDeals = repo.dateDeal.getAll()
            runOnUiThread {
                mainBinding.executePendingBindings()
                dealAdapter.updateItems(dateDeals)
            }
        })
    }

    private fun addDeal(deal:DealModel) {
        workerThread.postTask(Runnable {
            repo.deal.addDeal(deal)
            updateState()
        })
    }

    private fun doneDeal(deal:DateDealModel) {
        workerThread.postTask(Runnable {
            repo.dateDeal.doneDeal(deal)
            updateState()
        })
    }
}
