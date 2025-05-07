package com.greenvenom.core_network.domain.source

import com.dailyweath.core_weather.data.dto.response.ForecastResponse
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult

interface RemoteDataSource {
    fun fetchForecast(
        pathParams: List<String>,
        queryParams: Map<String, String>
    ): NetworkResult<ForecastResponse, NetworkError>
}