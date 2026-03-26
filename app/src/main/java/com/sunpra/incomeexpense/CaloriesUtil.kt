package com.sunpra.incomeexpense

object CaloriesUtil {

    fun hasDailyGoalBeenAchieved(
        totalCaloriesBurn: Double,
        dailyGoal: Double
    ): Boolean {

        return totalCaloriesBurn > dailyGoal
    }

}