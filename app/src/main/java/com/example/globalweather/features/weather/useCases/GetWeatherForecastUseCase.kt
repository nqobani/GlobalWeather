package com.example.globalweather.features.weather.useCases

import com.example.globalweather.features.weather.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherForecastUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(
        lat: Double,
        lon: Double
    ) = weatherRepository.getWeatherForecast(lon, lat)
}