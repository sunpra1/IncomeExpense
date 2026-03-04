package com.sunpra.incomeexpense.utility

import kotlinx.serialization.json.Json

const val ReadableDateFormat = "dd MMM yyyy, hh:mm a"

val TheJson = Json{ ignoreUnknownKeys = true; explicitNulls = true; }