package com.dailyweath.feat_weather.presentation.fragments.state

import com.dailyweath.core_weather.domain.model.Forecast

sealed class ForecastUiState {
    data object Loading : ForecastUiState()
    data class Success(val forecast: Forecast) : ForecastUiState()
    data class Error(val message: String) : ForecastUiState()
}