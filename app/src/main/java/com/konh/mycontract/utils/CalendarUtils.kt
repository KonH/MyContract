package com.konh.mycontract.utils

import java.text.SimpleDateFormat
import java.util.*

fun calendarToString(date: Calendar) : String {
    val format = SimpleDateFormat.getDateTimeInstance()
    return format.format(date.time)
}

fun calendarToStringShort(date: Calendar) : String {
    val format = SimpleDateFormat.getDateInstance()
    return format.format(date.time)
}

fun resetToDayStart(date: Calendar) : Calendar {
    val year = date.get(Calendar.YEAR)
    val month = date.get(Calendar.MONTH)
    val day = date.get(Calendar.DATE)
    val startDay = Calendar.getInstance()
    startDay.set(year, month, day, 0, 0, 0)
    startDay.set(Calendar.MILLISECOND, 0)
    return startDay
}

fun nextDay(day: Calendar) : Calendar {
    val nextDay = day.clone() as Calendar
    nextDay.add(Calendar.DAY_OF_MONTH, 1)
    return nextDay
}