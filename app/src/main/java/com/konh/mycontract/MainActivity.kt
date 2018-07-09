package com.konh.mycontract

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.konh.mycontract.adapter.DealAdapter
import com.konh.mycontract.database.DealDatabase
import com.konh.mycontract.model.DealModel
import com.konh.mycontract.utils.WorkerThread

class MainActivity : AppCompatActivity() {
    private val logTag = "MainActivity"

    private var workerThread: WorkerThread? = null

    private  var dealListView : ListView? = null

    private var db: DealDatabase? = null
    private val deals = mutableListOf<DealModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        workerThread = WorkerThread("dbThread")
        workerThread?.start()

        db = DealDatabase.getInstance(this)

        val dealAdapter = DealAdapter(applicationContext)
        dealAdapter.items = deals

        dealListView = findViewById<ListView>(R.id.list_deals)
        dealListView?.adapter = dealAdapter

        loadDeals()
    }

    private fun loadDeals() {
        val task = Runnable {
            val dbDeals = db?.dealDao()?.getAll()
            if (dbDeals != null ) {
                Log.d(logTag, "loadDeals: ${dbDeals.size} items")
                dbDeals.forEach { Log.d(logTag, "loadDeals: $it") }
                deals.clear()
                deals.addAll(dbDeals)
            }
        }
        workerThread?.postTask(task)
    }

    private fun addDeal(deal: DealModel) {
        deals.add(deal)
        saveDeal(deal)
    }

    private fun saveDeal(deal: DealModel) {
        val task = Runnable {
            Log.d(logTag, "saveDeal: $deal")
            db?.dealDao()?.insert(deal)
        }
        workerThread?.postTask(task)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_add_deal -> startCreatingDeal()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startCreatingDeal() {
        val editText = EditText(this)
        val dialog = AlertDialog.Builder(this)
                .setTitle("Add new deal:")
                .setView(editText)
                .setPositiveButton("Add", {_, _ ->
                    val newDealText = editText.text.toString()
                    val message = "New deal: '$newDealText'"
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    val newDeal = DealModel(0, newDealText, 10 )
                    addDeal(newDeal)
                })
                .setNegativeButton("Cancel", null)
                .create()
        dialog.show()
    }
}
