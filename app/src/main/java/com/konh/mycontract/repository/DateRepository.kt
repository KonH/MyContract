package com.konh.mycontract.repository

import android.util.Log
import com.konh.mycontract.utils.calendarToString
import java.util.*

class DateRepository {
    private val logTag = "DateRepository"

    private var today:Calendar = Calendar.getInstance()
    private var day:Calendar = today

    val isPastTime: Boolean
        get() = day != today

    fun getCurrent() : Calendar {
        Log.d(logTag, "getCurrent: ${calendarToString(day)}")
        return day
    }

    fun setCurrent(day:Calendar) {
        Log.d(logTag, "setCurrent: ${calendarToString(day)}")
        this.day = day
    }

    fun setToday() {
        Log.d(logTag, "setToday")
        setCurrent(today)
    }
}