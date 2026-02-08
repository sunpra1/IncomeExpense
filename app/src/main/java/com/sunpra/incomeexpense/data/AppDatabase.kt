package com.sunpra.incomeexpense.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserTable::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                    .also { instance = it }
            }
        }
    }

    abstract fun getUserTableDao(): UserTableDao

}