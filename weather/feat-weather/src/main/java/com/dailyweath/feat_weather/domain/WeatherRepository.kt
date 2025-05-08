package com.dailyweath.feat_weather.domain

import com.dailyweath.core_weather.domain.model.Forecast
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult

interface WeatherRepository {
    fun fetchForecasts(location: String): NetworkResult<Forecast, NetworkError>
    fun fetchLocalForecasts(): Forecast?
}