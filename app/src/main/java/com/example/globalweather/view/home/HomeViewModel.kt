package com.example.globalweather.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.globalweather.interfaces.IModel
import com.example.globalweather.features.weather.useCases.GetCurrentWeatherUseCase
import com.example.globalweather.features.weather.useCases.GetWeatherForecastUseCase
import com.example.globalweather.models.CurrentWeather
import com.example.globalweather.models.WeatherForecast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
//import location.LocationHelper
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getWeatherForecastUseCase: GetWeatherForecastUseCase
) : ViewModel(), IModel<HomeViewState, HomeIntents> {


    private val _weatherUiState = MutableStateFlow<HomeViewState>(HomeViewState.Loading)
    override val state: StateFlow<HomeViewState> = _weatherUiState
    override val intent: Channel<HomeIntents> = Channel(Channel.UNLIMITED)

    private var currentWeather: CurrentWeather? = null
    private var weatherForecast: WeatherForecast? = null

    init {
        viewModelScope.launch {
            intent.consumeAsFlow().collect { homeIntent ->
                when(homeIntent){
                    is HomeIntents.GetCurrentWeather -> getCurrentWeather()
                    is HomeIntents.GetWeatherForecast -> getWeatherForecast()
                }
            }
        }
    }

    private fun getCurrentWeather() {
        updateViewState(HomeViewState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            val currentWeatherResult = getCurrentWeatherUseCase(-29.883333, 31.049999)
            currentWeatherResult.onSuccess { currentWeatherData ->
                currentWeather = currentWeatherData
                updateViewState(HomeViewState.CurrentWeatherData(currentWeatherData, weatherForecast))
            }
            currentWeatherResult.onFailure { exception ->
                updateViewState(HomeViewState.OnError(exception.message!!))
            }
        }
    }

    private fun getWeatherForecast() {
        updateViewState(HomeViewState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            val weatherForecastResult = getWeatherForecastUseCase(-29.883333, 31.049999)
            weatherForecastResult.onSuccess { weatherForecastData ->
                weatherForecast = weatherForecastData
                updateViewState(HomeViewState.WeatherForecastData(weatherForecastData, currentWeather))
            }
            weatherForecastResult.onFailure { exception ->
                updateViewState(HomeViewState.OnError(exception.message!!))
            }
        }
    }

    private fun updateViewState(viewState: HomeViewState) {
        _weatherUiState.value = viewState
    }
}