package com.dailyweath.feat_weather.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dailyweath.feat_weather.R
import com.dailyweath.feat_weather.presentation.fragments.state.ForecastUiState
import com.dailyweath.feat_weather.presentation.models.DayUI
import com.dailyweath.feat_weather.presentation.models.toUI
import com.dailyweath.feat_weather.presentation.utils.getWeatherIconRes
import com.dailyweath.feat_weather.presentation.viewmodel.WeatherViewModel
import com.dailyweath.feat_weather.presentation.viewmodel.WeatherViewModelFactory

class ForecastFragment : Fragment() {
    private var dayId: Int = 1
    private val sharedViewModel: WeatherViewModel by activityViewModels()

    private lateinit var loadingOverlay: View
    private lateinit var dateText: TextView
    private lateinit var detailIcon: ImageView
    private lateinit var detailTemp: TextView
    private lateinit var detailConditions: TextView
    private lateinit var detailHumidity: TextView
    private lateinit var detailWind: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dayId = it.getInt("dayId", 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_forecast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        observeViewModel()
    }

    private fun initViews(view: View) {
        loadingOverlay = view.findViewById(R.id.loadingOverlay)
        dateText = view.findViewById(R.id.dateText)
        detailIcon = view.findViewById(R.id.detailIcon)
        detailTemp = view.findViewById(R.id.detailTemp)
        detailConditions = view.findViewById(R.id.detailConditions)
        detailHumidity = view.findViewById(R.id.detailHumidity)
        detailWind = view.findViewById(R.id.detailWind)
    }

    private fun observeViewModel() {
        sharedViewModel.forecastState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ForecastUiState.Loading -> showLoading()
                is ForecastUiState.Success -> showForecast(
                    state.forecast.days.find { it.id == dayId }?.toUI() ?: DayUI(
                        id = dayId,
                        datetime = "",
                        temp = "",
                        conditions = "",
                        humidity = "",
                        windSpeed = "",
                        icon = ""
                    )
                )
                is ForecastUiState.Error -> showError(state.message)
            }
        }
    }

    private fun showLoading() {
        loadingOverlay.visibility = View.VISIBLE
    }

    private fun showForecast(day: DayUI) {
        updateUi(day)
        loadingOverlay.visibility = View.GONE
    }

    private fun showError(message: String) {
        loadingOverlay.visibility = View.GONE
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun updateUi(day: DayUI) {
        dateText.text = day.datetime
        detailTemp.text = day.temp
        detailConditions.text = day.conditions
        detailHumidity.text = day.humidity
        detailWind.text = day.windSpeed
        detailIcon.setImageResource(getWeatherIconRes(day.icon))
    }
}