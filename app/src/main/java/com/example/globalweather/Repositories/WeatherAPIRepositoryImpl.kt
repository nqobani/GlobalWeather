package com.example.globalweather.Repositories

import android.content.Context
import com.example.globalweather.R
import com.example.globalweather.Repositories.interfaces.WeatherAPIRepository
import com.example.globalweather.dataLayer.WeatherServiceBuilder
import com.example.globalweather.models.CurrentWeather
import com.example.globalweather.models.WeatherForecast
import retrofit2.Response

class WeatherAPIRepositoryImpl( val context: Context) : WeatherAPIRepository {
    override suspend fun getCurrentWeather(lat: Double, lon: Double): Response<CurrentWeather> {
        return WeatherServiceBuilder.weatherAPIService.getRemoteCurrentWeather(lat, lon, context.getString(R.string.api_key), "metric")
    }

    override suspend fun getWeatherForecast(lat: Double, lon: Double): Response<WeatherForecast> {
        return WeatherServiceBuilder.weatherAPIService.getRemoteWeatherForecast(lat, lon, context.getString(R.string.api_key), "metric")
    }
}