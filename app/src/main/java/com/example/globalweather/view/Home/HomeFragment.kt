package com.example.globalweather.view.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
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

        CoroutineScope(Dispatchers.IO).launch {
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
        binding.composeView.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )
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

    @SuppressLint("SetTextI18n")
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

        binding.composeView.setContent {
            MaterialTheme() {
                WeatherForecast(items = items)
            }
        }
    }
}

@Composable
fun WeatherForecast(items: MutableList<list>) {
    LazyColumn(content = {
        items(items) {
            WeatherItem(item = it)
        }
    })
}

@Composable
fun WeatherItem(item: list) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Row() {
            Text(
                text = DateTimeUtils.getWeekDay(item.dt_txt),
                modifier = Modifier.height(50.dp).wrapContentHeight(CenterVertically),
                style = MaterialTheme.typography.body1
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = "https://openweathermap.org/img/wn/${item.weather[0].icon}.png",
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(50.dp)
                )

                Text(
                    text = "${item.main.temp_min}\u00B0",
                    modifier = Modifier.width(90.dp)
                        .wrapContentWidth(CenterHorizontally),
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = "${item.main.temp_max}\u00B0",
                    modifier = Modifier.width(90.dp)
                        .wrapContentWidth(CenterHorizontally),
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}