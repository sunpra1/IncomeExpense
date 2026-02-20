package com.sunpra.incomeexpense.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserTableDao {

    @Query("SELECT * FROM user_table WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): UserTable?

    @Query("SELECT * FROM user_table WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserTable?
    @Insert
    suspend fun insert(userTable: UserTable)

    @Update
    suspend fun update(userTable: UserTable)
}