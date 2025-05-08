package com.dailyweath.core_weather.domain.model

import com.dailyweath.core_weather.data.entity.ForecastEntity

data class Forecast(
    val id: Int = 0,
    val address: String,
    val timeZone: String,
    val tzOffset: Double,
    val days: List<Day>
) {
    fun toEntity(): ForecastEntity {
        return ForecastEntity(
            id = id,
            address = address,
            timeZone = timeZone,
            tzOffset = tzOffset
        )
    }

    fun extractDays(): List<Day> {
        return days
    }
}
