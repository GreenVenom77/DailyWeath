package com.dailyweath.core_weather.data.entity

import com.dailyweath.core_weather.domain.model.Day

data class DayEntity(
    val id: Int = 0,
    val datetime: String,
    val timestamp: Long,
    val temp: Double,
    val conditions: String,
    val icon: String,
    val humidity: Double,
    val windSpeed: Double,
    val forecastId: Int = 0
) {
    fun extractDay(): Day {
        return Day(
            id = id,
            datetime = datetime,
            timestamp = timestamp,
            temp = temp,
            conditions = conditions,
            icon = icon,
            humidity = humidity,
            windSpeed = windSpeed,
            forecastId = forecastId
        )
    }
}