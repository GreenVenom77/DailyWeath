package com.dailyweath.feat_weather.data

import com.dailyweath.core_caching.domain.source.LocalDataSource
import com.dailyweath.core_weather.data.entity.DayEntity
import com.dailyweath.core_weather.data.entity.ForecastEntity
import com.dailyweath.core_weather.domain.model.Forecast
import com.dailyweath.feat_weather.domain.WeatherRepository
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.map
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_network.domain.source.RemoteDataSource

class WeatherRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
): WeatherRepository {
    override fun fetchForecasts(location: String): NetworkResult<Forecast, NetworkError> {
        return remoteDataSource.fetchForecast(
            pathParams = listOf(location),
            queryParams = mapOf("unitGroup" to "metric")
        ).map {
            localDataSource.upsertForecast(
                it.extractForecast().toEntity(),
                it.days.map { dayResponse ->  dayResponse.extractDay().toEntity() }
            )
            fetchLocalForecasts() as Forecast
        }
    }

    override fun fetchLocalForecasts(): Forecast? {
        val cachedForecast = localDataSource.getForecast()
        return cachedForecast?.first?.extractForecast(cachedForecast.second)
    }
}