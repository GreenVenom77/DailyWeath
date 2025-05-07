package com.dailyweath.feat_network.data

import com.dailyweath.core_weather.data.dto.response.DayResponse
import com.dailyweath.core_weather.data.dto.response.ForecastResponse
import com.dailyweath.core_weather.data.dto.response.toDayResponse
import com.dailyweath.core_weather.data.dto.response.toForecastResponse
import org.json.JSONObject

class HttpResponse(
    val status: Int,
    val body: String
) {
    inline fun <reified T> body(): T {
        return when (T::class.java) {
            String::class.java -> body as T
            ForecastResponse::class.java -> body.toForecastResponse() as T
            DayResponse::class.java -> JSONObject(body).toDayResponse() as T
            else -> throw IllegalArgumentException("Unsupported type: ${T::class.java.name}")
        }
    }
}