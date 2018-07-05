package com.konh.mycontract.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.konh.mycontract.model.DealModel

@Dao
interface DealDao {
    @Query("SELECT * FROM Deal")
    fun getAll(): List<DealModel>

    @Insert()
    fun insert(model : DealModel)

}