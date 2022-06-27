package com.example.globalweather.useCases

import com.example.globalweather.Repositories.interfaces.WeatherAPIRepository
import javax.inject.Inject

class GetWeatherForecastUseCase  @Inject constructor(private val weatherAPIRepository: WeatherAPIRepository) {
    suspend operator fun invoke(lat: Double, lon: Double) = weatherAPIRepository.getWeatherForecast(lon, lat)
}