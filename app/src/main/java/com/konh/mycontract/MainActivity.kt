package com.konh.mycontract

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.konh.mycontract.adapter.DateDealAdapter
import com.konh.mycontract.database.DealDatabase
import com.konh.mycontract.databinding.ActivityMainBinding
import com.konh.mycontract.model.DealModel
import com.konh.mycontract.model.DateDealModel
import com.konh.mycontract.repository.RepositoryManager
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class MainActivity : AppCompatActivity() {
    private val dealAdapter = DateDealAdapter(this, emptyList(), { doneDeal(it) })

    private lateinit var repo: RepositoryManager
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val dealListView = findViewById<ListView>(R.id.list_deals)
        dealListView?.adapter = dealAdapter

        val day = extractConcreteDayFromIntent()
        initRepository(day)
    }

    override fun onResume() {
        super.onResume()
        updateState()
    }

    private fun extractConcreteDayFromIntent() : Calendar? {
        val key = getString(R.string.concrete_day_arg)
        if ((intent.extras != null) && intent.extras.containsKey(key)) {
            return intent.extras[key] as Calendar
        }
        return null
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
        var dialog : AlertDialog? = null
        dialog = AlertDialog.Builder(this)
                .setTitle(getString(R.string.add_deal_header))
                .setView(R.layout.deal_edit_view)
                .setPositiveButton(getString(R.string.add_deal_ok_button), { _, _ ->
                    val name = dialog?.findViewById<EditText>(R.id.edit_deal_title)?.text.toString()
                    val price = dialog?.findViewById<EditText>(R.id.edit_deal_price)?.text.toString().toInt()
                    val message = "New deal: '$name'"
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    val newDeal = DealModel(0, name, price)
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

    private fun initRepository(wantedDay:Calendar?) {
        val db = DealDatabase.getInstance(this)
        if ( db != null ) {
            val day = wantedDay ?: Calendar.getInstance()
            val isPastTime = wantedDay != null
            repo = RepositoryManager(db, day, isPastTime, getSharedPreferences(getString(R.string.file_settings_prefs), Context.MODE_PRIVATE))
            updateState()
        }
    }

    private fun updateState() {
        doAsync {
           mainBinding.scores = repo.scores.getDayScores(repo.date.getCurrent())
            val dateDeals = repo.dateDeal.getAll()
            uiThread {
                mainBinding.executePendingBindings()
                dealAdapter.updateItems(dateDeals)
            }
        }
    }

    private fun addDeal(deal:DealModel) {
        doAsync {
            repo.deal.addDeal(deal)
            updateState()
        }
    }

    private fun doneDeal(deal:DateDealModel) {
        doAsync {
            repo.dateDeal.doneDeal(deal)
            updateState()
        }
    }
}
