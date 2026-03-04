package com.sunpra.incomeexpense.data

import com.sunpra.incomeexpense.model.HealthTip
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class Repository(
    private val tipsService: TipsService,
    private val appDatabase: AppDatabase
) {

    fun getAllIncomeExpenses(userId: String): Flow<List<IncomeExpenseTable>> =
        appDatabase.getIncomeExpenseTableDao().getAll(userId)

    suspend fun login(email: String, password: String): UserTable? {
        val userTableDao = appDatabase.getUserTableDao()
        val userTable = userTableDao.login(email, password)
        return userTable
    }

    suspend fun registerUser(userTable: UserTable): Result<UserTable> {
        val userHavingEmail: UserTable? = appDatabase.getUserTableDao()
            .getUserByEmail(userTable.email)

        if (userHavingEmail != null) {
            return Result.failure(Exception("Email address already taken."))
        } else {
            appDatabase.getUserTableDao().insert(userTable)
            return Result.success(userTable)
        }
    }

    suspend fun getUserById(id: String) = appDatabase.getUserTableDao().getUserById(id)

    suspend fun insertIncomeExpense(incomeExpenseTable: IncomeExpenseTable) =
        appDatabase.getIncomeExpenseTableDao().insert(incomeExpenseTable)

    suspend fun updateIncomeExpense(incomeExpenseTable: IncomeExpenseTable) =
        appDatabase.getIncomeExpenseTableDao().update(incomeExpenseTable)

    suspend fun deleteIncomeExpense(incomeExpenseTable: IncomeExpenseTable) =
        appDatabase.getIncomeExpenseTableDao().delete(incomeExpenseTable)

    suspend fun getHealthTips(): Result<List<HealthTip>> {
        val response: Response<List<HealthTip>> = tipsService.getHealthTips()
        val responseBody: List<HealthTip>? = response.body()
        return if (response.isSuccessful && responseBody != null) {
            Result.success(responseBody)
        } else {
            Result.failure(
                Exception(
                    response.errorBody()?.string() ?: "Failed getting tips from server."
                )
            )
        }
    }

}