package com.sunpra.incomeexpense.model


import com.sunpra.incomeexpense.utility.TheJson
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HealthTip(
    @SerialName("description")
    val description: String,
    @SerialName("id")
    val id: String,
    @SerialName("image")
    val image: String,
    @SerialName("title")
    val title: String
)

private val healthTipStr = """
    {
        "id": "1",
        "title": "Stay Hydrated",
        "description": "Drink at least 7–8 glasses of water daily to maintain energy levels, improve digestion, and support overall body functions.",
        "image": "https://sunilprasai.com.np/images/health_tips/stay_hydarated.jpg"
    }
""".trimIndent()

val exampleHealthTip: HealthTip = TheJson.decodeFromString(healthTipStr)

