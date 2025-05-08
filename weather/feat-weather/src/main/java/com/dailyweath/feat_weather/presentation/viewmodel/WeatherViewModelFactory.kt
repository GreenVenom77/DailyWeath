package com.dailyweath.feat_weather.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dailyweath.feat_weather.domain.WeatherRepository
import com.dailyweath.feat_weather.utils.LocationHandler

class WeatherViewModelFactory(
    private val repository: WeatherRepository,
    private val locationHandler: LocationHandler
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(repository, locationHandler) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}