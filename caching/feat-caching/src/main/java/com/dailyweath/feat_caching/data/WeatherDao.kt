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
            db.delete("forecast", null, null)

            val values = ContentValues().apply {
                put("address", forecast.address)
                put("timeZone", forecast.timeZone)
                put("tzOffset", forecast.tzOffset)
            }
            newId = db.insertOrThrow("forecast", null, values)

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }

        return newId
    }

    fun insertDay(day: DayEntity): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("datetime", day.datetime)
            put("temp", day.tempe)
            put("conditions", day.conditions)
            put("icon", day.icon)
            put("humidity", day.humidity)
            put("wind_speed", day.windSpeed)
            put("forecast_id", day.forecastId)
        }
        return db.insert("day", null, values)
    }

    fun getForecast(): ForecastEntity? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "forecast",
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
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                address = cursor.getString(cursor.getColumnIndexOrThrow("address")),
                timeZone = cursor.getString(cursor.getColumnIndexOrThrow("timeZone")),
                tzOffset = cursor.getDouble(cursor.getColumnIndexOrThrow("tzOffset"))
            )
        }
        cursor.close()
        return forecast
    }

    fun getDaysForForecast(forecastId: Int): List<DayEntity> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "day",
            null,
            "forecast_id = ?",
            arrayOf(forecastId.toString()),
            null,
            null,
            "datetime ASC"
        )
        val days = mutableListOf<DayEntity>()
        while (cursor.moveToNext()) {
            days.add(
                DayEntity(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    datetime = cursor.getString(cursor.getColumnIndexOrThrow("datetime")),
                    tempe = cursor.getDouble(cursor.getColumnIndexOrThrow("tempe")),
                    conditions = cursor.getString(cursor.getColumnIndexOrThrow("conditions")),
                    icon = cursor.getString(cursor.getColumnIndexOrThrow("icon")),
                    humidity = cursor.getDouble(cursor.getColumnIndexOrThrow("humidity")),
                    windSpeed = cursor.getDouble(cursor.getColumnIndexOrThrow("wind_speed")),
                    forecastId = cursor.getInt(cursor.getColumnIndexOrThrow("forecast_id"))
                )
            )
        }
        cursor.close()
        return days
    }

    fun deleteForecastById(id: Int) {
        val db = dbHelper.writableDatabase
        db.delete("forecast", "id = ?", arrayOf(id.toString()))
    }
}
