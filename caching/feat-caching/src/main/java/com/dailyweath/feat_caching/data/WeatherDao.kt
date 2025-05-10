package com.dailyweath.feat_caching.data

import android.content.ContentValues
import com.dailyweath.core_weather.data.entity.DayEntity
import com.dailyweath.core_weather.data.entity.ForecastEntity

class WeatherDao(private val dbHelper: DailyWeathDBHelper) {
    fun upsertForecast(forecast: ForecastEntity): Long {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        var newId: Long

        try {
            db.delete(DailyWeathDBHelper.FORECAST_TABLE_NAME, null, null)

            val values = ContentValues().apply {
                put(DailyWeathDBHelper.FORECAST_ADDRESS, forecast.address)
                put(DailyWeathDBHelper.FORECAST_TIMEZONE, forecast.timeZone)
                put(DailyWeathDBHelper.FORECAST_TZ_OFFSET, forecast.tzOffset)
            }
            newId = db.insertOrThrow(DailyWeathDBHelper.FORECAST_TABLE_NAME, null, values)

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }

        return newId
    }

    fun insertDay(day: DayEntity, forecastId: Int): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DailyWeathDBHelper.DAY_DATETIME, day.datetime)
            put(DailyWeathDBHelper.DAY_TIMESTAMP, day.timestamp)
            put(DailyWeathDBHelper.DAY_TEMP, day.temp)
            put(DailyWeathDBHelper.DAY_CONDITIONS, day.conditions)
            put(DailyWeathDBHelper.DAY_ICON, day.icon)
            put(DailyWeathDBHelper.DAY_HUMIDITY, day.humidity)
            put(DailyWeathDBHelper.DAY_WIND_SPEED, day.windSpeed)
            put(DailyWeathDBHelper.DAY_FORECAST_ID, forecastId)
        }
        return db.insert(DailyWeathDBHelper.DAY_TABLE_NAME, null, values)
    }

    fun getForecast(): ForecastEntity? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DailyWeathDBHelper.FORECAST_TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null,
            "1"
        )
        var forecast: ForecastEntity? = null
        if (cursor.moveToFirst()) {
            forecast = ForecastEntity(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(DailyWeathDBHelper.FORECAST_ID)),
                address = cursor.getString(cursor.getColumnIndexOrThrow(DailyWeathDBHelper.FORECAST_ADDRESS)),
                timeZone = cursor.getString(cursor.getColumnIndexOrThrow(DailyWeathDBHelper.FORECAST_TIMEZONE)),
                tzOffset = cursor.getDouble(cursor.getColumnIndexOrThrow(DailyWeathDBHelper.FORECAST_TZ_OFFSET))
            )
        }
        cursor.close()
        return forecast
    }

    fun getDaysForForecast(forecastId: Int): List<DayEntity> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DailyWeathDBHelper.DAY_TABLE_NAME,
            null,
            "${DailyWeathDBHelper.DAY_FORECAST_ID} = ?",
            arrayOf(forecastId.toString()),
            null,
            null,
            "${DailyWeathDBHelper.DAY_TIMESTAMP} ASC"
        )
        val days = mutableListOf<DayEntity>()
        while (cursor.moveToNext()) {
            days.add(
                DayEntity(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(DailyWeathDBHelper.DAY_ID)),
                    datetime = cursor.getString(cursor.getColumnIndexOrThrow(DailyWeathDBHelper.DAY_DATETIME)),
                    timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(DailyWeathDBHelper.DAY_TIMESTAMP)),
                    temp = cursor.getDouble(cursor.getColumnIndexOrThrow(DailyWeathDBHelper.DAY_TEMP)),
                    conditions = cursor.getString(cursor.getColumnIndexOrThrow(DailyWeathDBHelper.DAY_CONDITIONS)),
                    icon = cursor.getString(cursor.getColumnIndexOrThrow(DailyWeathDBHelper.DAY_ICON)),
                    humidity = cursor.getDouble(cursor.getColumnIndexOrThrow(DailyWeathDBHelper.DAY_HUMIDITY)),
                    windSpeed = cursor.getDouble(cursor.getColumnIndexOrThrow(DailyWeathDBHelper.DAY_WIND_SPEED)),
                    forecastId = cursor.getInt(cursor.getColumnIndexOrThrow(DailyWeathDBHelper.DAY_FORECAST_ID))
                )
            )
        }
        cursor.close()
        return days
    }
}
