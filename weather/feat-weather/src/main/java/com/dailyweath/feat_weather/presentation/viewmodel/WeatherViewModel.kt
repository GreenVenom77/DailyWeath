package com.dailyweath.feat_weather.presentation.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dailyweath.core_weather.domain.model.Day
import com.dailyweath.feat_weather.domain.WeatherRepository
import com.dailyweath.feat_weather.presentation.fragments.state.ForecastUiState
import com.dailyweath.feat_weather.utils.LocationHandler
import com.greenvenom.core_network.data.NetworkResult
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class WeatherViewModel(
    private val repository: WeatherRepository,
    private val locationHandler: LocationHandler
): ViewModel() {
    private val _forecastState = MutableLiveData<ForecastUiState>()
    val forecastState: LiveData<ForecastUiState> = _forecastState

    private val executor: ExecutorService = Executors.newCachedThreadPool()

    init {
        _forecastState.postValue(ForecastUiState.Loading)
    }

    fun requestFetchPermission(activity: Activity) {
        if (locationHandler.hasPermissions()) {
            locationHandler.requestLocation() { lat, lon ->
                fetchForecastForLocation("$lat,$lon")
            }
            return
        }
        locationHandler.requestLocationPermission(activity)
    }

    fun getDayByTimestamp(timestamp: Long): Day? {
        // TODO: Better UI State handling with new state
        val forecastState = _forecastState.value as? ForecastUiState.Success
        return forecastState?.forecast?.days?.firstOrNull{ it.timestamp == timestamp }
    }

    private fun fetchForecastForLocation(location: String, isRefresh: Boolean = false) {
        _forecastState.postValue(ForecastUiState.Loading)

        executor.execute {
            when (val result = repository.fetchForecasts(location, isRefresh)) {
                is NetworkResult.Success -> {
                    _forecastState.postValue(ForecastUiState.Success(result.data))
                }
                is NetworkResult.Error -> {
                    _forecastState.postValue(ForecastUiState.Error(result.error.errorType))
                }
            }
        }
    }

    fun refreshCurrentForecast() {
        locationHandler.requestLocation() { lat, lon ->
            fetchForecastForLocation("$lat,$lon", true)
        }
    }

    override fun onCleared() {
        super.onCleared()
        executor.shutdown()
    }
}