package com.dailyweath.feat_weather.presentation.models

import com.dailyweath.core_weather.domain.model.Forecast

data class ForecastUI(
    val location: String,
)

fun Forecast.toUI(): ForecastUI {
    return ForecastUI(
        location = timeZone.split("/").lastOrNull()?.replace("_", " ") ?: timeZone
    )
}