package com.konh.mycontract

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.konh.mycontract.adapter.DateDealAdapter
import com.konh.mycontract.databinding.ActivityMainBinding
import com.konh.mycontract.model.DateDealModel
import com.konh.mycontract.model.DealModel
import com.konh.mycontract.repository.getRepo
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread
import java.util.*

class MainActivity : AppCompatActivity() {
    private val logTag = "MainActivity"

    private val dealAdapter = DateDealAdapter(this, emptyList(), clickHandler = { tryDoneDeal(it) }, longClickHandler = { editDeal(it) })

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val dealListView = findViewById<ListView>(R.id.list_deals)
        dealListView?.adapter = dealAdapter

        val day = extractConcreteDayFromIntent()
        initRepository(day)

        updateState()
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
            R.id.action_add_deal -> showAddDealDialog()
            R.id.action_go_to_settings -> goToSettings()
            R.id.action_go_to_history -> goToHistory()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDealDialogCommon(header:String, positive:String, negative:String, id:Int, name:String, price:String, onPositive:(DealModel)->Unit, onNegative:()->Unit) {
        var nameView : EditText? = null
        var priceView : EditText? = null
        val dialog = AlertDialog.Builder(this)
                .setTitle(header)
                .setView(R.layout.deal_edit_view)
                .setPositiveButton(positive, { _, _ ->
                    try {
                        val model = DealModel(
                                id,
                                nameView?.text.toString(),
                                priceView?.text.toString().toInt()
                        )
                        onPositive(model)
                    } catch (e:Exception) {
                        Log.e(logTag, "showDealDialogCommon: $e")
                    }
                })
                .setNegativeButton(negative, {_, _ ->
                    onNegative()
                })
                .create()
        dialog.show()
        nameView = dialog.findViewById(R.id.edit_deal_title)
        priceView = dialog.findViewById(R.id.edit_deal_price)
        nameView?.setText(name)
        priceView?.setText(price)
    }

    private fun showAddDealDialog() {
        showDealDialogCommon(
                getString(R.string.add_deal_header),
                getString(R.string.add_deal_ok_button),
                getString(R.string.add_deal_cancel_button),
                0, "", "",
                { addDeal(it) }, { })
    }

    private fun shoeEditDealDialog(model:DealModel) {
        showDealDialogCommon(
                getString(R.string.edit_deal_header),
                getString(R.string.edit_deal_confirm_button),
                getString(R.string.edit_deal_delete_button),
                model.id, model.name, model.score.toString(),
                { updateDeal(it) }, { deleteDeal(model) })
    }

    private fun goToSettings() {
        startActivity<SettingsActivity>()
    }

    private fun goToHistory() {
        startActivity<HistoryActivity>()
    }

    private fun initRepository(wantedDay:Calendar?) {
        val repo = getRepo()
        if ( repo != null ) {
            if ( wantedDay != null ) {
                repo.date.setCurrent(wantedDay)
            } else {
                repo.date.setToday()
            }
        }
    }

    private fun updateState() {
        doAsync {
            val repo = getRepo()
            if ( repo != null ) {
                mainBinding.scores = repo.scores.getDayScores(repo.date.getCurrent())
                val dateDeals = repo.dateDeal.getAll()
                uiThread {
                    mainBinding.executePendingBindings()
                    dealAdapter.updateItems(dateDeals)
                }
            }
        }
    }

    private fun addDeal(deal:DealModel) {
        val message = "New deal: '${deal.name}'"
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        doAsync {
            val repo = getRepo()
            if ( repo != null ) {
                repo.deal.addDeal(deal)
                updateState()
            }
        }
    }

    private fun tryDoneDeal(deal:DateDealModel) {
        if ( !deal.interactable ) {
            return
        }
        doAsync {
            val repo = getRepo()
            if ( repo != null ) {
                repo.dateDeal.doneDeal(deal)
                updateState()
            }
        }
    }

    private fun editDeal(dateDeal:DateDealModel) : Boolean {
        val deal = DealModel(dateDeal.dealId, dateDeal.name, dateDeal.score)
        shoeEditDealDialog(deal)
        return true
    }

    private fun updateDeal(deal:DealModel) {
        val repo = getRepo()
        if ( repo != null ) {
            doAsync {
                repo.deal.updateDeal(deal)
                updateState()
            }
        }
    }

    private fun deleteDeal(deal: DealModel) {
        val repo = getRepo()
        if ( repo != null ) {
            doAsync {
                repo.deal.deleteDeal(deal)
                updateState()
            }
        }
    }
}
