package com.dailyweath.feat_weather.data

import com.dailyweath.core_caching.domain.source.LocalDataSource
import com.dailyweath.core_weather.domain.model.Forecast
import com.dailyweath.feat_weather.domain.WeatherRepository
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.map
import com.greenvenom.core_network.domain.source.RemoteDataSource
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WeatherRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
): WeatherRepository {
    override fun fetchForecasts(
        location: String,
        isRefresh: Boolean
    ): NetworkResult<Forecast, NetworkError> {
        return fetchLocalForecasts()?.takeUnless { isRefresh }?.let {
            NetworkResult.Success(it)
        } ?: run {
            val startDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
            val endDate = LocalDate.now().plusDays(5).format(DateTimeFormatter.ISO_DATE)
            remoteDataSource.fetchForecast(
                pathParams = listOf(location, startDate, endDate),
                queryParams = mapOf("unitGroup" to "metric")
            ).map {
                localDataSource.upsertForecast(
                    it.extractForecast().toEntity(),
                    it.days.map { dayResponse -> dayResponse.extractDay().toEntity() }
                )
                fetchLocalForecasts() as Forecast
            }
        }
    }

    private fun fetchLocalForecasts(): Forecast? {
        val cachedForecast = localDataSource.getForecast()
        return cachedForecast?.first?.extractForecast(cachedForecast.second)
    }
}