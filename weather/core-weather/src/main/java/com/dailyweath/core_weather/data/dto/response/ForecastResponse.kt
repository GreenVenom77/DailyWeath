package com.dailyweath.core_weather.data.dto.response

import org.json.JSONArray
import org.json.JSONObject

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

fun String.toForecastResponse(): ForecastResponse {
    val json = JSONObject(this)

    val daysList = mutableListOf<DayResponse>()
    val daysArray = json.optJSONArray("days") ?: JSONArray()

    (0 until daysArray.length()).forEach { index ->
        val dayJson = daysArray.getJSONObject(index)
        daysList.add(dayJson.toDayResponse())
    }

    return ForecastResponse(
        queryCost = json.optInt("queryCost", 0),
        latitude = json.optDouble("latitude", 0.0),
        longitude = json.optDouble("longitude", 0.0),
        resolvedAddress = json.optString("resolvedAddress", ""),
        address = json.optString("address", ""),
        timezone = json.optString("timezone", ""),
        tzoffset = json.optDouble("tzoffset", 0.0),
        days = daysList
    )
}
