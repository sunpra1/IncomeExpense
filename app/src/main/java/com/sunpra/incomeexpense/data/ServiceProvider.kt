package com.sunpra.incomeexpense.data

import com.sunpra.incomeexpense.utility.TheJson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlin.time.Duration.Companion.seconds


object ServiceProvider {
    val client = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .readTimeout(20.seconds)
        .writeTimeout(20.seconds)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .addConverterFactory(
            TheJson.asConverterFactory("application/json".toMediaType())
        )
        .baseUrl("https://sunilprasai.com.np/api/")
        .build()

    val tipsService = retrofit.create(TipsService::class.java)

}