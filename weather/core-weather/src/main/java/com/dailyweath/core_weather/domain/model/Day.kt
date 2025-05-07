package com.dailyweath.core_weather.domain.model

import com.dailyweath.core_weather.data.entity.DayEntity

data class Day(
    val id: Int = 0,
    val datetime: String,
    val tempe: Double,
    val conditions: String,
    val icon: String,
    val humidity: Double,
    val windSpeed: Double,
    val forecastId: Int = 1
) {
    fun toEntity(): DayEntity {
        return DayEntity(
            id = id,
            datetime = datetime,
            tempe = tempe,
            conditions = conditions,
            icon = icon,
            humidity = humidity,
            windSpeed = windSpeed,
            forecastId = forecastId
        )
    }
}
