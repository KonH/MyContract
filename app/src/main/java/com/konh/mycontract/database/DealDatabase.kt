package com.konh.mycontract.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.konh.mycontract.dao.DealDao
import com.konh.mycontract.model.DealModel

@Database(entities = arrayOf(DealModel::class), version = 1)
abstract class DealDatabase : RoomDatabase() {

    abstract fun dealDao() : DealDao

    companion object {
        private var Instance: DealDatabase? = null

        fun getInstance(context: Context): DealDatabase? {
            if (Instance == null) {
                synchronized(DealDatabase::class) {
                    Instance = Room.databaseBuilder(context.applicationContext,
                            DealDatabase::class.java, "deal.db")
                            .build()
                }
            }
            return Instance
        }

        fun destroyInstance() {
            Instance = null
        }
    }
}