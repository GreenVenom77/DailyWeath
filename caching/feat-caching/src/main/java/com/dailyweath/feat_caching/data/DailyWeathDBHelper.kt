package com.dailyweath.feat_caching.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DailyWeathDBHelper(context: Context): SQLiteOpenHelper(
    context, "dailyweath.db", null, 1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.setForeignKeyConstraintsEnabled(true)

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
                tempe REAL NOT NULL,
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