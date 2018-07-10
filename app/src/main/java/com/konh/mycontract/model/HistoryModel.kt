package com.konh.mycontract.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.konh.mycontract.utils.calendarToString
import java.util.*

@Entity(tableName = "History")
data class HistoryModel(
        @PrimaryKey(autoGenerate = true) val id:Int,
        @ColumnInfo(name = "day") val day:Calendar,
        @ColumnInfo(name = "dealId") val dealId:Int,
        @ColumnInfo(name = "name") val name:String,
        @ColumnInfo(name = "score") val score:Int
) {
    override fun toString() : String {
        return "HistoryModel(id=$id, day=${calendarToString(day)}, dealId=$dealId, name=$name, score=$score)"
    }
}