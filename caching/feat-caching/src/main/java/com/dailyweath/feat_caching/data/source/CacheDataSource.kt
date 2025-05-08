package com.dailyweath.feat_caching.data.source

import com.dailyweath.core_caching.domain.source.LocalDataSource
import com.dailyweath.core_weather.data.entity.DayEntity
import com.dailyweath.core_weather.data.entity.ForecastEntity
import com.dailyweath.feat_caching.data.WeatherDao

class CacheDataSource(
    private val weatherDao: WeatherDao
): LocalDataSource {
    override fun upsertForecast(forecastEntity: ForecastEntity, daysEntity: List<DayEntity>) {
        val forecastId = weatherDao.upsertForecast(forecastEntity)
        daysEntity.forEach {
            weatherDao.insertDay(it, forecastId.toInt())
        }
    }

    override fun getForecast(): Pair<ForecastEntity, List<DayEntity>>? {
        weatherDao.getForecast()?.let {
            val days = weatherDao.getDaysForForecast(it.id)
            return Pair(it, days)
        }
        return null
    }
}