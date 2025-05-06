package com.greenvenom.core_weather.data.dto.response

data class ForecastResponse(
    val queryCost: Int = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val resolvedAddress: String = "",
    val address: String = "",
    val timezone: String = "",
    val tzoffset: Double = 0.0,
    val days: List<DayResponse> = listOf()
)