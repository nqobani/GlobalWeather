package com.example.globalweather.view.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.globalweather.ViewModels.WeatherViewModel
import com.example.globalweather.adaptersAndViewHolders.WeatherForecastRecyclerViewAdapter
import com.example.globalweather.databinding.FragmentHomeBinding
import com.example.globalweather.models.list
import com.example.globalweather.utils.DateTimeUtils
import com.example.globalweather.utils.DisplayImageHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


private const val LON = "lon"
private const val LAT = "lat"

@AndroidEntryPoint
class HomeFragment : Fragment(), HomeView<HomeViewState> {
    private var lon: Float = 30.9758303f
    private var lat: Float = -29.8917232f

    private val weatherViewModel by viewModels<WeatherViewModel>()
    lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            lat = it.getFloat(LAT)
//            lon = it.getFloat(LON)
//        }

        weatherViewModel.state.observe(this) { viewState ->
            render(viewState)
        }

        CoroutineScope(Dispatchers.IO).launch {
            weatherViewModel.intent.send(HomeIntents.GetCurrentWeather(lat.toDouble(), lon.toDouble()))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(lat: String, lon: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(LON, lon)
                    putString(LAT, lat)
                }
            }
    }

    override fun render(state: HomeViewState) {
        CoroutineScope(Dispatchers.Main).launch {
            when(state){
                is HomeViewState.Loading -> {
                }
                is HomeViewState.CurrentWeatherData -> {
                    binding.cityNameTextView.text = state.currentWeather.name
                    binding.currentTempTextView.text = "${state.currentWeather.main.temp}\u00B0"
                    binding.currentWeatherTempTextView.text = "${state.currentWeather.main.temp}\u00B0"
                    binding.minTempTextView.text = "${state.currentWeather.main.temp_min}\u00B0"
                    binding.maxTempTextView.text = "${state.currentWeather.main.temp_max}\u00B0"
                    binding.currentWeatherDescriptionTextView.text = state.currentWeather.weather[0].description
                    DisplayImageHelper.displayImage(this@HomeFragment.context!!, binding.weatherIconImageView, "https://openweathermap.org/img/wn/${state.currentWeather.weather[0].icon}.png")

                    CoroutineScope(Dispatchers.IO).launch {
                        weatherViewModel.intent.send(HomeIntents.GetWeatherForecast(lat.toDouble(), lon.toDouble()))
                    }
                }
                is HomeViewState.WeatherForecastData -> {
                    var iterms : MutableList<list> = mutableListOf()
                    state.weatherForecast.list.forEach{ iterm ->
                        if(DateTimeUtils.getHour(iterm.dt_txt) == 12){
                            iterms.add(iterm)
                        }
                    }
                    var adapter = WeatherForecastRecyclerViewAdapter(this@HomeFragment.context!!, iterms)
                    binding.weatherForecastRecyclerView.adapter = adapter
                    binding.weatherForecastRecyclerView.layoutManager = LinearLayoutManager(this@HomeFragment.context)
                    binding.weatherForecastRecyclerView.setHasFixedSize(true)
                }
                is HomeViewState.OnError -> {
                }
            }
        }
    }
}