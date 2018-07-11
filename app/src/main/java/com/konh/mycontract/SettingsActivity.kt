package com.konh.mycontract

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import com.konh.mycontract.database.DealDatabase
import com.konh.mycontract.databinding.ActivitySettingsBinding
import com.konh.mycontract.model.SettingsModel
import com.konh.mycontract.repository.RepositoryManager
import org.jetbrains.anko.defaultSharedPreferences
import java.util.*

class SettingsActivity : AppCompatActivity() {
    private val logTag = "SettingsActivity"

    private lateinit var repo: RepositoryManager
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)

        val dayScoresEditText = findViewById<EditText>(R.id.day_scores_edit_text)
        dayScoresEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if ( p0 != null ) {
                    onDayScoresTextChanged(p0)
                }
            }
        })

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
        binding.settings = repo.settings.get()
        binding.executePendingBindings()
    }

    private fun onDayScoresTextChanged(it:Editable) {
        try {
            val newDayScores = it.toString().toInt()
            repo.settings.update(SettingsModel(newDayScores))
            updateState()
        } catch (e:Exception) {
            Log.e(logTag, "onDayScoresTextChanged: $e")
        }
    }
}
