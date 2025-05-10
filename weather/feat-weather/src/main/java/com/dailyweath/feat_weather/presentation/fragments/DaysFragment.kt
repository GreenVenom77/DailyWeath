package com.dailyweath.feat_weather.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dailyweath.core_weather.domain.model.Forecast
import com.dailyweath.feat_weather.R
import com.dailyweath.feat_weather.presentation.fragments.adapters.DayAdapter
import com.dailyweath.feat_weather.presentation.fragments.state.ForecastUiState
import com.dailyweath.feat_weather.presentation.models.toUI
import com.dailyweath.feat_weather.presentation.viewmodel.WeatherViewModel

class DaysFragment : Fragment() {
    private val sharedViewModel: WeatherViewModel by activityViewModels()

    private lateinit var loadingOverlay: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var locationText: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_days, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        observeViewModel()
        swipeRefreshLayout.setOnRefreshListener {
            sharedViewModel.refreshCurrentForecast()
        }
    }

    private fun initViews(view: View) {
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        loadingOverlay = view.findViewById(R.id.loadingOverlay)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        locationText = view.findViewById(R.id.locationText)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
    }

    private fun observeViewModel() {
        sharedViewModel.forecastState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ForecastUiState.Loading -> showLoading()
                is ForecastUiState.Success -> showForecast(state.forecast)
                is ForecastUiState.Error -> showError(getString(state.errorType.resId))
            }
        }
    }

    private fun showLoading() {
        loadingOverlay.visibility = View.VISIBLE
    }

    private fun showForecast(forecast: Forecast) {
        recyclerView.adapter = DayAdapter(forecast.days.map { it.toUI() }) { timestamp ->
            val bundle = Bundle().apply { putLong(ForecastFragment.DAY_TIMESTAMP_KEY, timestamp) }
            view?.findNavController()?.navigate(R.id.action_daysFragment_to_forecastFragment, bundle)
        }
        locationText.text = forecast.toUI(requireContext()).areaDetails
        loadingOverlay.visibility = View.GONE
        swipeRefreshLayout.isRefreshing = false
    }

    private fun showError(message: String) {
        loadingOverlay.visibility = View.GONE
        swipeRefreshLayout.isRefreshing = false
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}