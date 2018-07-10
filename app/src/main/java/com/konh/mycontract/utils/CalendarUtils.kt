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

fun resetToDayStart(day: Calendar) : Calendar {
    val startDay = (day.clone() as Calendar)
    startDay.set(Calendar.HOUR, 0)
    startDay.set(Calendar.MINUTE, 0)
    startDay.set(Calendar.SECOND, 0)
    startDay.set(Calendar.MILLISECOND, 0)
    return startDay
}

fun nextDay(day: Calendar) : Calendar {
    val nextDay = day.clone() as Calendar
    nextDay.add(Calendar.DAY_OF_MONTH, 1)
    return nextDay
}