package com.konh.mycontract.utils

import android.arch.persistence.room.TypeConverter
import java.util.*

class CalendarConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Calendar? {
        if (value != null) {
            val cal = GregorianCalendar()
            cal.time = Date(value)
            return cal
        }
        return null
    }

    @TypeConverter
    fun toTimestamp(date: Calendar?): Long? {
        return date?.time?.time
    }
}