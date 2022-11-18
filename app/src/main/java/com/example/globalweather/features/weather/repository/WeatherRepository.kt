package com.example.globalweather.features.weather.repository

import com.example.globalweather.models.CurrentWeather
import com.example.globalweather.models.WeatherForecast

interface WeatherRepository {
    suspend fun getCurrentWeather(lat: Double, lon: Double): Result<CurrentWeather>
    suspend fun getWeatherForecast(lat: Double, lon: Double): Result<WeatherForecast>
}