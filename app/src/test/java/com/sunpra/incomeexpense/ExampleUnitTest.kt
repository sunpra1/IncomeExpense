package com.sunpra.incomeexpense

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val caloriesBurn: Double = 350.0
        val dailyGoal : Double = 300.0
        val hasDailyGoalBeenAchieved =
            CaloriesUtil.hasDailyGoalBeenAchieved(caloriesBurn, dailyGoal)
        assertEquals(false, hasDailyGoalBeenAchieved)
    }
}
