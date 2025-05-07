package com.dailyweath.core_weather.data.entity

data class DayEntity(
    val id: Int = 0,
    val datetime: String,
    val tempe: Double,
    val conditions: String,
    val icon: String,
    val humidity: Double,
    val windSpeed: Double,
    val forecastId: Int = 0
)
