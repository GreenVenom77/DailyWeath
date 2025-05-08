package com.dailyweath.core_weather.data.entity

import com.dailyweath.core_weather.domain.model.Forecast

data class ForecastEntity(
    val id: Int = 0,
    val address: String,
    val timeZone: String,
    val tzOffset: Double
) {
    fun extractForecast(days: List<DayEntity>): Forecast {
        return Forecast(
            address = address,
            timeZone = timeZone,
            tzOffset = tzOffset,
            days = days.map { it.extractDay() }
        )
    }
}