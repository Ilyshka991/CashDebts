package com.pechuro.cashdebts.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pechuro.cashdebts.data.model.Debt
import io.reactivex.Observable

@Dao
interface DebtDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(vararg debt: Debt)

    @Query("SELECT * FROM debts")
    fun getAll(): Observable<List<Debt>>
}