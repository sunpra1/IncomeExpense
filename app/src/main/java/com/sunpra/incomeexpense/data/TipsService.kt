package com.sunpra.incomeexpense.data

import com.sunpra.incomeexpense.model.HealthTip
import retrofit2.Response
import retrofit2.http.GET

interface TipsService {

    @GET("health-tips")
    suspend fun getHealthTips(): Response<List<HealthTip>>

}