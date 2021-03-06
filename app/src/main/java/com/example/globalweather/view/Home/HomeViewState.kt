package com.example.globalweather.view.Home

import com.example.globalweather.interfaces.IState
import com.example.globalweather.models.CurrentWeather
import com.example.globalweather.models.WeatherForecast

sealed class HomeViewState: IState {
    object Loading: HomeViewState()
    data class CurrentWeatherData(val currentWeather: CurrentWeather): HomeViewState()
    data class WeatherForecastData(val weatherForecast: WeatherForecast): HomeViewState()
    data class OnError(val errorMessage: String): HomeViewState()
}