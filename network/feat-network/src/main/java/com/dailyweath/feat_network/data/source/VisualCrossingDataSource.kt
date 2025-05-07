package com.dailyweath.feat_network.data.source

import com.dailyweath.core_weather.data.dto.response.ForecastResponse
import com.dailyweath.feat_network.data.HttpClient
import com.dailyweath.feat_network.utils.safeCall
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.domain.source.RemoteDataSource
import com.greenvenom.core_network.utils.constructUrl

class VisualCrossingDataSource(
    private val httpClient: HttpClient
): RemoteDataSource {
    override fun fetchForecast(
        pathParams: List<String>,
        queryParams: Map<String, String>
    ): NetworkResult<ForecastResponse, NetworkError> {
        return safeCall {
            httpClient.get(
                fullUrl = constructUrl(
                    pathParams = pathParams,
                    queryParams = queryParams
                ),
            )
        }
    }
}