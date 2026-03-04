package com.sunpra.incomeexpense.data

import android.icu.util.Calendar
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.sunpra.incomeexpense.utility.ReadableDateFormat
import java.text.SimpleDateFormat
import java.util.Locale

@Entity(
    tableName = "income_expense_table",
    foreignKeys = [
        ForeignKey(
            entity = UserTable::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class IncomeExpenseTable(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "income_expense")
    val incomeExpense: IncomeExpense,
    @ColumnInfo(name = "amount")
    val amount: Double,
    @ColumnInfo(name = "income_type")
    val incomeType: IncomeType?,
    @ColumnInfo(name = "expense_type")
    val expenseType: ExpenseType?,
    @ColumnInfo(name = "date")
    val date: Long,
    @ColumnInfo(name = "user_id")
    val userId: String
)

val exampleIncomeExpenseTable = IncomeExpenseTable(
    id = "",
    name = "Saleray of march",
    incomeExpense = IncomeExpense.Income,
    amount = 125000.0,
    incomeType = IncomeType.Salary,
    expenseType = null,
    date = Calendar.getInstance().timeInMillis,
    userId = ""
)

val IncomeExpenseTable.calendarDate: Calendar
    get() = Calendar.getInstance().apply { timeInMillis = date }

val IncomeExpenseTable.formattedDate: String
    get() {
        val javaDate = calendarDate.time
        val dateFormatter = SimpleDateFormat(ReadableDateFormat, Locale.getDefault())
        return dateFormatter.format(javaDate)
    }

enum class IncomeExpense {
    Income, Expense
}

enum class IncomeType {
    Salary, Business, Rental, Interest, Dividend, Others
}

enum class ExpenseType {
    Housing, FoodAndGroceries, Transportation, Utilities, HealthCare, Others
}