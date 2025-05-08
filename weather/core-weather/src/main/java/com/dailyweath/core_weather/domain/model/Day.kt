package com.dailyweath.core_weather.domain.model

import com.dailyweath.core_weather.data.entity.DayEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Day(
    val id: Int = 0,
    val datetime: String,
    val temp: Double,
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
            temp = temp,
            conditions = conditions,
            icon = icon,
            humidity = humidity,
            windSpeed = windSpeed,
            forecastId = forecastId
        )
    }

    fun formatDateTime(): String {
        val localDate = LocalDate.parse(this.datetime)
        val formatter = DateTimeFormatter.ofPattern("d MMM, yyyy") // "7 May, 2025"
        return localDate.format(formatter)
    }
}
