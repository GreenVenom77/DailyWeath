package com.dailyweath.feat_caching.data.source

import com.dailyweath.core_caching.domain.source.LocalDataSource
import com.dailyweath.core_weather.data.entity.DayEntity
import com.dailyweath.core_weather.data.entity.ForecastEntity
import com.dailyweath.feat_caching.data.WeatherDao

class CacheDataSource(
    private val weatherDao: WeatherDao
): LocalDataSource {
    override fun upsertForecast(forecastEntity: ForecastEntity, daysEntity: List<DayEntity>) {
        weatherDao.upsertForecast(forecastEntity)
        daysEntity.forEach {
            weatherDao.insertDay(it)
        }
    }

    override fun getForecast(): Pair<ForecastEntity?, List<DayEntity>> {
        val forecast = weatherDao.getForecast()
        val days = weatherDao.getDaysForForecast(forecast?.id ?: 1)
        return Pair(forecast, days)
    }

    override fun deleteForecast(id: Int) {
        weatherDao.deleteForecastById(id)
    }
}