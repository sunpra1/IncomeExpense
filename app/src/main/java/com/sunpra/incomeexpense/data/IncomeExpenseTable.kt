package com.sunpra.incomeexpense.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// CalorieBurnOrIntake
@Entity(
    tableName = "income_expense_table",
    foreignKeys = [
        ForeignKey(
            entity = UserTable::class,
            childColumns = ["user_id"],
            parentColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class IncomeExpenseTable(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "income_expense")
    val incomeOrExpense: IncomeOrExpense,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "amount")
    val amount: Double, // calorie
    @ColumnInfo(name = "income_type")
    val incomeType: IncomeType?, // CalorieIntakeType
    @ColumnInfo(name = "expense_type")
    val expenseType: ExpenseType?, // CalorieBurnType
    @ColumnInfo(name = "note")
    val note: String?,
    @ColumnInfo(name = "date_created")
    val dateCreated: Long,
    @ColumnInfo(name = "user_id")
    val userId: String
)

//enum class CalorieIntakeOrBurn {
//    CalorieIntake, Burn
//}

enum class IncomeOrExpense {
    Income, Expense
}

enum class IncomeType {
    Salary, Business, HouseRent, Interest, Dividend, Others
}

//enum class CalorieIntakeType {
//    Breakfast, Launch, Brunch, Dinner, Snacks, Others
//}

enum class ExpenseType {
    Travelling, Food, HouseRent, Others
}

//enum class CalorieBurnType {
//    Running, Workout, Yoga, Jumba, Others
//}

