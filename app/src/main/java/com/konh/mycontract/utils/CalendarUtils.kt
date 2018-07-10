package com.konh.mycontract.utils

import java.text.SimpleDateFormat
import java.util.*

fun calendarToString(date: Calendar) : String {
    val format = SimpleDateFormat.getDateTimeInstance()
    return format.format(date.time)
}