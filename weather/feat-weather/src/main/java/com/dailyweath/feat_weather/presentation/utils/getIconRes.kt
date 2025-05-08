package com.dailyweath.feat_weather.presentation.utils

import com.dailyweath.feat_weather.R

fun getWeatherIconRes(icon: String): Int {
    return when (icon) {
        "clear-day" -> R.drawable.ic_day
        "clear-night" -> R.drawable.ic_night
        "partly-cloudy-day" -> R.drawable.ic_partly_cloudy_day
        "partly-cloudy-night" -> R.drawable.ic_partly_cloudy_night
        "rain" -> R.drawable.ic_rainy
        "snow" -> R.drawable.ic_snow
        "wind" -> R.drawable.ic_windy
        "fog" -> R.drawable.ic_fog
        else -> R.drawable.ic_unknown_weather
    }
}