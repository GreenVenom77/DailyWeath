package com.dailyweath.feat_weather.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class LocationHandler(private val context: Context) {
    fun hasPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    fun requestLocation(callback: (Double, Double) -> Unit) {
        if (!hasPermissions()) {
            Toast.makeText(context, "Location permissions not granted", Toast.LENGTH_SHORT).show()
            return
        }

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val providers = locationManager.getProviders(true)
        if (providers.isEmpty()) {
            Toast.makeText(context, "No location provider enabled", Toast.LENGTH_SHORT).show()
            return
        }

        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                locationManager.removeUpdates(this)
                callback(location.latitude, location.longitude)
            }

            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        }

        for (provider in providers) {
            locationManager.requestLocationUpdates(provider, 0L, 0f, listener)

            val lastLocation = locationManager.getLastKnownLocation(provider)
            if (lastLocation != null) {
                locationManager.removeUpdates(listener)
                callback(lastLocation.latitude, lastLocation.longitude)
                return
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            locationManager.removeUpdates(listener)
            Toast.makeText(context, "Location request timed out", Toast.LENGTH_SHORT).show()
        }, 5000)
    }

    fun requestLocationPermission(activity: Activity, requestCode: Int = 100) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            requestCode
        )
    }
}