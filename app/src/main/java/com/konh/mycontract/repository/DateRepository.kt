package com.konh.mycontract.repository

import java.util.*

class DateRepository(private val day:Calendar) {

    fun getCurrent() : Calendar {
        return day
    }
}