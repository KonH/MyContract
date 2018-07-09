package com.konh.mycontract.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.konh.mycontract.model.HistoryModel

@Dao
interface HistoryDao {
    @Query("SELECT * FROM History")
    fun getAll(): List<HistoryModel>

    @Insert()
    fun insert(model : HistoryModel)

}