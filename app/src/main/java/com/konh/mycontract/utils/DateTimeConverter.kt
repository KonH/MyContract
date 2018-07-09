package com.konh.mycontract.utils

import android.arch.persistence.room.TypeConverter
import java.util.*

class DateTimeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value != null) Date(value) else null
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time;
    }
}