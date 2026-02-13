package com.sunpra.incomeexpense.data

class Repository(private val appDatabase: AppDatabase) {

    suspend fun login(email: String, password: String) : UserTable? {
        val userTableDao = appDatabase.getUserTableDao()
        val userTable = userTableDao.login(email, password)
        return userTable
    }

}