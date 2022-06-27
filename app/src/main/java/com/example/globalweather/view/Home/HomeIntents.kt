package com.example.globalweather.view.Home

import com.example.globalweather.interfaces.IIntent

sealed class HomeIntents: IIntent {
    data class GetCurrentWeather(val lat: Double, val lon: Double): HomeIntents()
    data class GetWeatherForecast(val lat: Double, val lon: Double): HomeIntents()
}