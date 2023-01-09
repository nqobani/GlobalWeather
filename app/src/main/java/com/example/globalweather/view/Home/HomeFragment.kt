package com.example.globalweather.view.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import com.example.globalweather.composeComponents.CurrentWeatherTempLevels
import com.example.globalweather.composeComponents.DailyWeatherItemCard
import com.example.globalweather.databinding.FragmentHomeBinding
import com.example.globalweather.models.CurrentWeather
import com.example.globalweather.models.WeatherForecast
import com.example.globalweather.models.list
import com.example.globalweather.utils.DateTimeUtils
import com.google.accompanist.themeadapter.material.MdcTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), HomeView<HomeViewState> {
    private val homeViewModel by viewModels<HomeViewModel>()

    // TODO: Fully migrate to compose and delete the XML.
    //TODO: Use resources and remove hardcoded data
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
        binding.composeViewTop.setContent {
            MdcTheme {
                TopContentView(
                    currentWeather.name,
                    currentWeather.main.temp,
                    currentWeather.weather[0].icon,
                    currentWeather.weather[0].description,
                    currentWeather.main.temp_min,
                    currentWeather.main.temp_max
                )
            }
        }
    }

    private fun displayWeatherForecast(weatherForecast: WeatherForecast) {
        val items: MutableList<list> = mutableListOf()
        weatherForecast.list.forEach { iterm ->
            if (DateTimeUtils.getHour(iterm.dt_txt) == 12) {
                items.add(iterm)
            }
        }

        binding.composeView.setContent {
            MdcTheme() {
                WeatherForecast(items = items)
            }
        }
    }
}

@Composable
fun WeatherForecast(items: MutableList<list>) {
    LazyColumn(content = {
        items(items) {
            DailyWeatherItemCard(item = it)
        }
    })
}

@Composable
fun TopContentView(
    placeName: String,
    temperature: Double,
    icon: String,
    description: String,
    minTemp: Double,
    maxTemp: Double
) {

    Column(
        modifier = Modifier.padding(bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = placeName,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(),
            style = MaterialTheme.typography.h5
        )
        Text(
            text = "$temperature°",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(),
            style = MaterialTheme.typography.h3
        )
        AsyncImage(
            model = "https://openweathermap.org/img/wn/$icon.png",
            contentDescription = "Weather Icon",
            modifier = Modifier.size(130.dp)
        )
        Text(
            text = description,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(),
            style = MaterialTheme.typography.h6
        )

        Spacer(modifier = Modifier.height(8.dp))

        CurrentWeatherTempLevels(
            minTemp = minTemp,
            currentTemp = temperature,
            maxTemp = maxTemp
        )
    }
}