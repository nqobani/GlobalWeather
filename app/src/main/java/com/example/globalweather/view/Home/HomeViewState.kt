package com.example.globalweather.view.home

import com.example.globalweather.interfaces.IState
import com.example.globalweather.models.CurrentWeather
import com.example.globalweather.models.WeatherForecast

sealed class HomeViewState: IState {
    object Loading: HomeViewState()

    data class CurrentWeatherData(
        val currentWeather: CurrentWeather,
        val weatherForecast: WeatherForecast?
    ): HomeViewState()

    data class WeatherForecastData(
        val weatherForecast: WeatherForecast,
        val currentWeather: CurrentWeather?
    ): HomeViewState()

    data class OnError(val errorMessage: String): HomeViewState()
}