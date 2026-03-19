package com.sunpra.incomeexpense.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface IncomeExpenseTableDao {

    @Insert
    suspend fun insert(incomeExpenseTable: IncomeExpenseTable)

    @Update
    suspend fun update(incomeExpenseTable: IncomeExpenseTable)

    @Delete
    suspend fun delete(incomeExpenseTable: IncomeExpenseTable)

}