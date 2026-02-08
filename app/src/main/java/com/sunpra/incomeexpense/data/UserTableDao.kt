package com.sunpra.incomeexpense.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserTableDao {

    @Query("SELECT * FROM user_table WHERE email = :email AND password = :password LIMIT 1")
    fun login(email: String, password: String): UserTable?

    @Insert
    fun insert(userTable: UserTable)

    @Update
    fun update(userTable: UserTable)
}