package com.greenvenom.core_weather.data.utils

import com.greenvenom.core_weather.data.dto.response.DayResponse
import com.greenvenom.core_weather.data.dto.response.ForecastResponse
import org.json.JSONArray
import org.json.JSONObject

object ForecastParser {
    fun parseDayResponse(json: JSONObject): DayResponse {
        return DayResponse(
            datetime = json.optString("datetime", ""),
            datetimeEpoch = json.optInt("datetimeEpoch", 0),
            tempmax = json.optDouble("tempmax", 0.0),
            tempmin = json.optDouble("tempmin", 0.0),
            temp = json.optDouble("temp", 0.0),
            feelslikemax = json.optDouble("feelslikemax", 0.0),
            feelslikemin = json.optDouble("feelslikemin", 0.0),
            feelslike = json.optDouble("feelslike", 0.0),
            dew = json.optDouble("dew", 0.0),
            humidity = json.optDouble("humidity", 0.0),
            precip = json.optDouble("precip", 0.0),
            precipprob = json.optDouble("precipprob", 0.0),
            precipcover = json.optDouble("precipcover", 0.0),
            preciptype = if (json.has("preciptype") && !json.isNull("preciptype")) json.optString("preciptype") else null,
            snow = json.optDouble("snow", 0.0),
            snowdepth = json.optDouble("snowdepth", 0.0),
            windgust = json.optDouble("windgust", 0.0),
            windspeed = json.optDouble("windspeed", 0.0),
            winddir = json.optDouble("winddir", 0.0),
            pressure = json.optDouble("pressure", 0.0),
            cloudcover = json.optDouble("cloudcover", 0.0),
            visibility = json.optDouble("visibility", 0.0),
            solarradiation = json.optDouble("solarradiation", 0.0),
            solarenergy = json.optDouble("solarenergy", 0.0),
            uvindex = json.optDouble("uvindex", 0.0),
            severerisk = json.optDouble("severerisk", 0.0),
            sunrise = json.optString("sunrise", ""),
            sunriseEpoch = json.optInt("sunriseEpoch", 0),
            sunset = json.optString("sunset", ""),
            sunsetEpoch = json.optInt("sunsetEpoch", 0),
            moonphase = json.optDouble("moonphase", 0.0),
            conditions = json.optString("conditions", ""),
            description = json.optString("description", ""),
            icon = json.optString("icon", ""),
            source = json.optString("source", "")
        )
    }

    fun parseForecastResponse(jsonString: String): ForecastResponse {
        val json = JSONObject(jsonString)

        val daysList = mutableListOf<DayResponse>()
        val daysArray = json.optJSONArray("days") ?: JSONArray()

        (0 until daysArray.length()).forEach { index ->
            val dayJson = daysArray.getJSONObject(index)
            daysList.add(parseDayResponse(dayJson))
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
}