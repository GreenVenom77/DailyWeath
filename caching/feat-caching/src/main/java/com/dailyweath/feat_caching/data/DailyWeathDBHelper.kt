package com.dailyweath.feat_caching.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DailyWeathDBHelper(context: Context): SQLiteOpenHelper(
    context, DATABASE_NAME, null, 1
) {
    companion object {
        const val DATABASE_NAME = "dailyweath.db"
        const val FORECAST_TABLE_NAME = "forecast"
        const val DAY_TABLE_NAME = "day"

        // Forecast Columns
        const val FORECAST_ID = "id"
        const val FORECAST_ADDRESS = "address"
        const val FORECAST_TIMEZONE = "timeZone"
        const val FORECAST_TZ_OFFSET = "tzOffset"

        // Day Columns
        const val DAY_ID = "id"
        const val DAY_DATETIME = "datetime"
        const val DAY_TIMESTAMP = "timestamp"
        const val DAY_TEMP = "temperature"
        const val DAY_CONDITIONS = "conditions"
        const val DAY_ICON = "icon"
        const val DAY_HUMIDITY = "humidity"
        const val DAY_WIND_SPEED = "wind_speed"
        const val DAY_FORECAST_ID = "forecast_id"
    }
    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)
        db?.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""
            CREATE TABLE forecast (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                address TEXT NOT NULL,
                timeZone TEXT NOT NULL,
                tzOffset REAL NOT NULL
            );
        """)

        db?.execSQL("""
            CREATE TABLE day (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                datetime TEXT NOT NULL,
                timestamp INTEGER NOT NULL,
                temperature REAL NOT NULL,
                conditions TEXT NOT NULL,
                icon TEXT NOT NULL,
                humidity REAL NOT NULL,
                wind_speed REAL NOT NULL,
                forecast_id INTEGER NOT NULL,
                FOREIGN KEY(forecast_id) REFERENCES forecast(id) ON DELETE CASCADE
            );
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS day")
        db?.execSQL("DROP TABLE IF EXISTS forecast")
        onCreate(db)
    }
}