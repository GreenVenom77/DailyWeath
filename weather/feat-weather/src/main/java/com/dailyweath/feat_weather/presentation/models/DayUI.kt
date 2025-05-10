package com.dailyweath.feat_weather.presentation.models

import com.dailyweath.core_weather.domain.model.Day
import java.text.DecimalFormat

data class DayUI(
    val id: Int,
    val datetime: String,
    val timestamp: Long,
    val temp: String,
    val conditions: String,
    val icon: String,
    val humidity: String,
    val windSpeed: String,
)

fun Day.toUI(): DayUI {
    val decimalFormat = DecimalFormat("#.#")
    return DayUI(
        id = id,
        datetime = formatDateTime(),
        timestamp = timestamp,
        temp = "${decimalFormat.format(temp)}°C",
        conditions = conditions,
        icon = icon,
        humidity = "${decimalFormat.format(humidity)}%",
        windSpeed = "${decimalFormat.format(windSpeed)} km/h"
    )
}