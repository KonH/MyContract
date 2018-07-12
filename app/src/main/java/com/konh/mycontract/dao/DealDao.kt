package com.konh.mycontract.dao

import android.arch.persistence.room.*
import com.konh.mycontract.model.DealModel

@Dao
interface DealDao {
    @Query("SELECT * FROM Deal")
    fun getAll(): List<DealModel>

    @Insert()
    fun insert(model : DealModel)

    @Update()
    fun update(model : DealModel)

    @Delete()
    fun delete(model : DealModel)

}