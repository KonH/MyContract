package com.konh.mycontract

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    val logTag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_add_deal -> startCreatingTask()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startCreatingTask() {
        val editText = EditText(this)
        val dialog = AlertDialog.Builder(this)
                .setTitle("Add new deal:")
                .setView(editText)
                .setPositiveButton("Add", {dialog, which ->
                    val newTaskText = editText.text.toString()
                    val message = "New deal: '$newTaskText'"
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    Log.d(logTag, message)
                })
                .setNegativeButton("Cancel", null)
                .create()
        dialog.show()
    }
}
