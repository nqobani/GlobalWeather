package com.example.globalweather.view.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.globalweather.adaptersAndViewHolders.WeatherForecastRecyclerViewAdapter
import com.example.globalweather.databinding.FragmentHomeBinding
import com.example.globalweather.models.CurrentWeather
import com.example.globalweather.models.WeatherForecast
import com.example.globalweather.models.list
import com.example.globalweather.utils.DateTimeUtils
import com.example.globalweather.utils.DisplayImageHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), HomeView<HomeViewState> {
    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {
            homeViewModel.intent.send(HomeIntents.GetCurrentWeather)
            homeViewModel.intent.send(HomeIntents.GetWeatherForecast)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.state.collect {
                render(it)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun render(state: HomeViewState) {
        CoroutineScope(Dispatchers.Main).launch {
            when (state) {
                is HomeViewState.Loading -> {
                    // TODO: improve Loading indicator
                }
                is HomeViewState.CurrentWeatherData -> {
                    displayCurrentWeather(state.currentWeather)
                }
                is HomeViewState.WeatherForecastData -> {
                    displayWeatherForecast(state.weatherForecast)
                    state.currentWeather?.let { displayCurrentWeather(it) }
                }
                is HomeViewState.OnError -> {
                    // TODO: Handle error
                }
            }
        }
    }

    private fun displayCurrentWeather(currentWeather: CurrentWeather) {
        binding.cityNameTextView.text = currentWeather.name
        binding.currentTempTextView.text = "${currentWeather.main.temp}\u00B0"
        binding.currentWeatherTempTextView.text = "${currentWeather.main.temp}\u00B0"
        binding.minTempTextView.text = "${currentWeather.main.temp_min}\u00B0"
        binding.maxTempTextView.text = "${currentWeather.main.temp_max}\u00B0"
        binding.currentWeatherDescriptionTextView.text = currentWeather.weather[0].description
        DisplayImageHelper.displayImage(
            requireContext(),
            binding.weatherIconImageView,
            "https://openweathermap.org/img/wn/${currentWeather.weather[0].icon}.png"
        )
    }

    private fun displayWeatherForecast(weatherForecast: WeatherForecast) {
        val items: MutableList<list> = mutableListOf()
        weatherForecast.list.forEach { iterm ->
            if (DateTimeUtils.getHour(iterm.dt_txt) == 12) {
                items.add(iterm)
            }
        }
        val adapter = WeatherForecastRecyclerViewAdapter(requireContext(), items)
        binding.weatherForecastRecyclerView.adapter = adapter
        binding.weatherForecastRecyclerView.layoutManager = LinearLayoutManager(
            this@HomeFragment.context
        )
        binding.weatherForecastRecyclerView.setHasFixedSize(true)
    }
}