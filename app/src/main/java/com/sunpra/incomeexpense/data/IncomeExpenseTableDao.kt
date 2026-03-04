package com.sunpra.incomeexpense.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeExpenseTableDao {

    @Query("SELECT * FROM income_expense_table WHERE user_id = :userId")
    fun getAll(userId: String): Flow<List<IncomeExpenseTable>>

    @Insert
    suspend fun insert(incomeExpenseTable: IncomeExpenseTable)

    @Update
    suspend fun update(incomeExpenseTable: IncomeExpenseTable)

    @Delete
    suspend fun delete(incomeExpenseTable: IncomeExpenseTable)
}