package com.pechuro.cashdebts.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pechuro.cashdebts.data.local.database.dao.DebtDao
import com.pechuro.cashdebts.data.model.Debt

@Database(
    entities = [Debt::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun debtDao(): DebtDao
}