package com.example.globalweather.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.globalweather.interfaces.IModel
import com.example.globalweather.useCases.GetCurrentWeatherUseCase
import com.example.globalweather.useCases.GetWeatherForecastUseCase
import com.example.globalweather.view.Home.HomeIntents
import com.example.globalweather.view.Home.HomeViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel
@Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getWeatherForecastUseCase: GetWeatherForecastUseCase) : ViewModel(), IModel<HomeViewState, HomeIntents> {

    val _state = MutableLiveData<HomeViewState>()
    override val intent: Channel<HomeIntents> = Channel(Channel.UNLIMITED)
    override val state: LiveData<HomeViewState> get() = _state

    init {
        viewModelScope.launch {
            intent.consumeAsFlow().collect { homeIntent ->
                when(homeIntent){
                    is HomeIntents.GetCurrentWeather -> getCurrentWeather(homeIntent.lat, homeIntent.lon)
                    is HomeIntents.GetWeatherForecast -> getWeatherForecast(homeIntent.lat, homeIntent.lon)
                }
            }
        }
    }

    private suspend fun getCurrentWeather(lat: Double, lon: Double) {
        updateViewState(HomeViewState.Loading)
        val currentWeatherResponse = getCurrentWeatherUseCase.invoke(lat, lon)
        if (currentWeatherResponse.isSuccessful){
            currentWeatherResponse.body()?.let{
                    responseBody -> HomeViewState.CurrentWeatherData(responseBody)
            }?.let { currentWeatherDataState ->
                updateViewState(currentWeatherDataState)
            }
        } else {
            updateViewState(HomeViewState.OnError(currentWeatherResponse.message()))
        }
    }

    private suspend fun getWeatherForecast(lat: Double, lon: Double) {
        updateViewState(HomeViewState.Loading)
        val weatherForecastResponse = getWeatherForecastUseCase.invoke(lat, lon)
        if (weatherForecastResponse.isSuccessful){
            weatherForecastResponse.body()?.let{
                    responseBody -> HomeViewState.WeatherForecastData(responseBody)
            }?.let { weatherForecastDataState ->
                updateViewState(weatherForecastDataState)
            }
        } else {
            updateViewState(HomeViewState.OnError(weatherForecastResponse.message()))
        }
    }

    private fun updateViewState(viewState: HomeViewState) {
        _state.postValue(viewState)
    }
}