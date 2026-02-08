package com.sunpra.incomeexpense.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_table"
)
data class UserTable(
    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "full_name")
    val fullName: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "password")
    val password: String
)
