package com.konh.mycontract.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "Deal")
data class DealModel(
        @PrimaryKey(autoGenerate = true) val id:Int,
        @ColumnInfo(name = "name") val name:String,
        @ColumnInfo(name = "score") val score:Int
)