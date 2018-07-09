package com.konh.mycontract

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import com.konh.mycontract.adapter.DateDealAdapter
import com.konh.mycontract.database.DealDatabase
import com.konh.mycontract.model.DealModel
import com.konh.mycontract.model.HistoryModel
import com.konh.mycontract.model.DateDealModel
import com.konh.mycontract.repository.DateDealRepository
import com.konh.mycontract.repository.RepositoryManager
import com.konh.mycontract.utils.WorkerThread
import java.util.*

class MainActivity : AppCompatActivity() {
    private val logTag = "MainActivity"

    private var workerThread: WorkerThread? = null

    private var dealAdapter : DateDealAdapter? = null

    private var repository:RepositoryManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        workerThread = WorkerThread("dbThread")
        workerThread?.start()

        dealAdapter = DateDealAdapter(applicationContext, emptyList(), { doneDeal(it) })

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
            repository = RepositoryManager(db)
            updateTodayDeals()
        }
    }

    private fun updateTodayDeals() {
        workerThread?.postTask(Runnable {
            val dateDeals = repository?.dateDeal?.getAll()
            runOnUiThread {
                if ( dateDeals != null ) {
                    dealAdapter?.updateItems(dateDeals)
                }
            }
        })
    }

    private fun addDeal(deal:DealModel) {
        workerThread?.postTask(Runnable {
            repository?.deal?.addDeal(deal)
            updateTodayDeals()
        })
    }

    private fun doneDeal(deal:DateDealModel) {
        workerThread?.postTask(Runnable {
            repository?.dateDeal?.doneDeal(deal)
            updateTodayDeals()
        })
    }
}
