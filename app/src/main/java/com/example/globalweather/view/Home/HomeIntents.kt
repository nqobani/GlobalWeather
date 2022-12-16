package com.example.globalweather.view.home

import com.example.globalweather.interfaces.IIntent

sealed class HomeIntents : IIntent {
    object GetCurrentWeather : HomeIntents()
    object GetWeatherForecast : HomeIntents()
}