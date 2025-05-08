package com.greenvenom.dailyweath

import android.app.Application
import com.dailyweath.core_caching.domain.source.LocalDataSource
import com.dailyweath.feat_caching.data.DailyWeathDBHelper
import com.dailyweath.feat_caching.data.WeatherDao
import com.dailyweath.feat_caching.data.source.CacheDataSource
import com.dailyweath.feat_network.data.HttpClient
import com.dailyweath.feat_network.data.source.VisualCrossingDataSource
import com.dailyweath.feat_weather.data.WeatherRepositoryImpl
import com.dailyweath.feat_weather.domain.WeatherRepository
import com.dailyweath.feat_weather.utils.LocationHandler
import com.greenvenom.core_network.domain.source.RemoteDataSource

class DailyWeathApplication : Application() {
    lateinit var remoteDataSource: RemoteDataSource
    lateinit var localDataSource: LocalDataSource
    lateinit var repository: WeatherRepository
    lateinit var locationHandler: LocationHandler

    override fun onCreate() {
        super.onCreate()
        initializeDependencies()
    }

    private fun initializeDependencies() {
        remoteDataSource = VisualCrossingDataSource(HttpClient())
        localDataSource = CacheDataSource(WeatherDao(DailyWeathDBHelper(this)))
        repository = WeatherRepositoryImpl(remoteDataSource, localDataSource)
        locationHandler = LocationHandler(this)
    }
}