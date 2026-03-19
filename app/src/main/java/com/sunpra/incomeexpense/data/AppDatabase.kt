package com.sunpra.incomeexpense.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [UserTable::class, IncomeExpenseTable::class], version = 2)
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
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { instance = it }
            }
        }
    }

    abstract fun getUserTableDao(): UserTableDao

    abstract fun getIncomeOrExpenseTableDao(): IncomeExpenseTableDao

}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE income_expense_table ADD COLUMN date_created INTEGER NOT NULL DEFAULT 0")
    }
}