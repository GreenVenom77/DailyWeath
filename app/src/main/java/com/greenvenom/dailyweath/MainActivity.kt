package com.greenvenom.dailyweath

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.dailyweath.feat_weather.presentation.viewmodel.WeatherViewModel
import com.dailyweath.feat_weather.presentation.viewmodel.WeatherViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val repository = (this.application as DailyWeathApplication).repository
        val locationHandler = (this.application as DailyWeathApplication).locationHandler

        viewModel = ViewModelProvider(
            this,
            WeatherViewModelFactory(repository, locationHandler)
        )[WeatherViewModel::class.java]

        viewModel.requestFetchPermission(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                viewModel.requestFetchPermission(this)
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder(this)
                    .setTitle("Location permission required")
                    .setMessage("This app requires location permission to get your current location and show you the forecast for your area.")
                    .setPositiveButton("Settings") { _, _ ->
                        val intent = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = android.net.Uri.fromParts("package", packageName, null)
                        startActivity(Intent(intent, uri))
                    }
                    .show()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}