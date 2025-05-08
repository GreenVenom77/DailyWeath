package com.dailyweath.feat_weather.presentation.viewmodel

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailyweath.feat_weather.domain.WeatherRepository
import com.dailyweath.feat_weather.presentation.fragments.state.ForecastUiState
import com.dailyweath.feat_weather.utils.LocationHandler
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.utils.toString
import kotlinx.coroutines.launch
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
            fetchLocalForecasts()
            locationHandler.requestLocation() { lat, lon ->
                fetchForecastForLocation(activity, "$lat,$lon")
            }
            return
        }
        locationHandler.requestLocationPermission(activity)
    }

    fun fetchForecastForLocation(context: Context, location: String) {
        _forecastState.postValue(ForecastUiState.Loading)

        executor.execute {
            when (val result = repository.fetchForecasts(location)) {
                is NetworkResult.Success -> {
                    _forecastState.postValue(ForecastUiState.Success(result.data))
                }
                is NetworkResult.Error -> {
                    _forecastState.postValue(ForecastUiState.Error(
                        result.error.errorType?.toString(context) ?: "Unknown error"
                    ))
                }
            }
        }
    }

    fun fetchLocalForecasts() {
        executor.execute {
            val forecast = repository.fetchLocalForecasts()
            if (forecast != null) {
                _forecastState.postValue(ForecastUiState.Success(forecast))
            } else {
                _forecastState.postValue(ForecastUiState.Error("No local forecasts available"))
            }
        }
    }

    fun refreshCurrentForecast(context: Context) {
        locationHandler.requestLocation() { lat, lon ->
            fetchForecastForLocation(context, "$lat,$lon")
        }
    }

    override fun onCleared() {
        super.onCleared()
        executor.shutdown()
    }
}