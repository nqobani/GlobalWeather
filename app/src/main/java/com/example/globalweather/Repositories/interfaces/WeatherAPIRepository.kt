package com.example.globalweather.Repositories.interfaces

import com.example.globalweather.models.CurrentWeather
import com.example.globalweather.models.WeatherForecast
import retrofit2.Response

interface WeatherAPIRepository {
    suspend fun getCurrentWeather(lat: Double, lon: Double): Response<CurrentWeather>
    suspend fun getWeatherForecast(lat: Double, lon: Double): Response<WeatherForecast>
}