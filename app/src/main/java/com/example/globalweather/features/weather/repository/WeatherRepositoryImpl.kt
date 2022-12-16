package com.example.globalweather.features.weather.repository

import android.content.Context
import com.example.globalweather.R
import com.example.globalweather.features.weather.dataLayer.WeatherServiceBuilder
import com.example.globalweather.models.CurrentWeather
import com.example.globalweather.models.WeatherForecast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepositoryImpl(val context: Context) : WeatherRepository {
    override suspend fun getCurrentWeather(lat: Double, lon: Double): Result<CurrentWeather> {
        return withContext(Dispatchers.IO) {
            val currentWeatherResponse = WeatherServiceBuilder
                .weatherAPIService
                .getRemoteCurrentWeather(
                    lat,
                    lon,
                    context.getString(
                        R.string.api_key
                    ),
                    "metric"
                )
            if (currentWeatherResponse.isSuccessful) {
                return@withContext Result.success(currentWeatherResponse.body()!!)
            } else {
                return@withContext Result.failure(java.lang.Exception("Some exception"))
            }
        }
    }

    override suspend fun getWeatherForecast(lat: Double, lon: Double): Result<WeatherForecast> {
        return withContext(Dispatchers.IO) {
            val weatherForecastResponse = WeatherServiceBuilder
                .weatherAPIService
                .getRemoteWeatherForecast(
                    lat,
                    lon,
                    context.getString(R.string.api_key), "metric"
                )
            if (weatherForecastResponse.isSuccessful) {
                return@withContext Result.success(weatherForecastResponse.body()!!)
            } else {
                return@withContext Result.failure(java.lang.Exception("Some exception"))
            }
        }
    }
}