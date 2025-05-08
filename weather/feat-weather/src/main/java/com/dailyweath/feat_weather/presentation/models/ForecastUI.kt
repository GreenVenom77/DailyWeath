package com.dailyweath.feat_weather.presentation.models

import android.content.Context
import android.location.Geocoder
import com.dailyweath.core_weather.domain.model.Forecast
import java.util.Locale

data class ForecastUI(
    val location: String,
    val coordinates: String = "",
    val areaDetails: String = ""
)

fun Forecast.toUI(context: Context): ForecastUI {
    val lat = this.address.let { coordString ->
        val parts = coordString.split(",")
        parts[0].toDouble()
    }
    val lon = this.address.let { coordString ->
        val parts = coordString.split(",")
        parts[1].toDouble()
    }

    val baseLocation = timeZone.split("/").lastOrNull()?.replace("_", " ") ?: timeZone

    val coordDisplay = "Lat: ${"%.4f".format(lat)}, Lon: ${"%.4f".format(lon)}"
    val areaDisplay = try {
        Geocoder(context, Locale.getDefault()).getFromLocation(lat, lon, 1)
            ?.firstOrNull()
            ?.let { addr ->
                listOfNotNull(addr.locality, addr.adminArea, addr.countryName)
                    .take(2) // Just show city & country or region & country
                    .joinToString(", ")
            } ?: ""
    } catch (e: Exception) { "" }

    return ForecastUI(
        location = baseLocation,
        coordinates = coordDisplay,
        areaDetails = areaDisplay
    )
}