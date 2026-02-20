package com.sunpra.incomeexpense.data

class Repository(private val appDatabase: AppDatabase) {

    suspend fun login(email: String, password: String) : UserTable? {
        val userTableDao = appDatabase.getUserTableDao()
        val userTable = userTableDao.login(email, password)
        return userTable
    }

    suspend fun registerUser(userTable: UserTable): Result<UserTable> {
        val userHavingEmail: UserTable? = appDatabase.getUserTableDao()
            .getUserByEmail(userTable.email)

        if(userHavingEmail != null){
            return Result.failure(Exception("Email address already taken."))
        }else{
            appDatabase.getUserTableDao().insert(userTable)
            return Result.success(userTable)
        }
    }

}