package com.konh.mycontract.repository

import java.util.*

class DateRepository(private val day:Calendar, val isPastTime:Boolean) {

    fun getCurrent() : Calendar {
        return day
    }
}