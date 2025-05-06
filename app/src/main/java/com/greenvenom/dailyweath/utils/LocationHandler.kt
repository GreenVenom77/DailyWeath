package com.greenvenom.dailyweath.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class LocationHandler(private val activity: Activity) {

    fun hasPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    fun requestLocation(callback: (Double, Double) -> Unit) {
        if (!hasPermissions()) return

        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val provider = when {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) -> LocationManager.GPS_PROVIDER
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> LocationManager.NETWORK_PROVIDER
            else -> {
                Toast.makeText(activity, "No location provider enabled", Toast.LENGTH_SHORT).show()
                return
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            locationManager.getCurrentLocation(provider, null, activity.mainExecutor) { location ->
                if (location != null) {
                    callback(location.latitude, location.longitude)
                } else {
                    Toast.makeText(activity, "Failed to get location", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            locationManager.requestSingleUpdate(provider,
                { location -> callback(location.latitude, location.longitude) }, null)
        }
    }

    fun requestLocationPermission(requestCode: Int = 100) {
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