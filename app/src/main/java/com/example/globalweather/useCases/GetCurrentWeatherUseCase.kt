package com.example.globalweather.useCases

import com.example.globalweather.Repositories.interfaces.WeatherAPIRepository
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(private val weatherApiRepository: WeatherAPIRepository) {
    suspend operator fun invoke(lat: Double, lon: Double) = weatherApiRepository.getCurrentWeather(lat, lon)
}