package com.dailyweath.core_caching.domain.source

import com.dailyweath.core_weather.data.entity.DayEntity
import com.dailyweath.core_weather.data.entity.ForecastEntity

interface LocalDataSource {
    fun upsertForecast(
        forecastEntity: ForecastEntity,
        daysEntity: List<DayEntity>
    )

    fun getForecast(): Pair<ForecastEntity, List<DayEntity>>?
}