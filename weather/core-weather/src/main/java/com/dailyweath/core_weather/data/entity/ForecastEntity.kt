package com.dailyweath.core_weather.data.entity

data class ForecastEntity(
    val id: Int = 0,
    val address: String,
    val timeZone: String,
    val tzOffset: Double
)
