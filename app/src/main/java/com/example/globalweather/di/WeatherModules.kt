package com.example.globalweather.di

import android.content.Context
import com.example.globalweather.features.weather.repository.WeatherRepositoryImpl
import com.example.globalweather.features.weather.repository.WeatherRepository
import com.example.globalweather.features.weather.useCases.GetCurrentWeatherUseCase
import com.example.globalweather.features.weather.useCases.GetWeatherForecastUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class WeatherModules {
    @Provides
    fun provideWeatherRepository(@ApplicationContext context: Context): WeatherRepository = WeatherRepositoryImpl(context)

    @Provides
    fun provideGetCurrentWeatherUseCase(weatherRepository: WeatherRepository): GetCurrentWeatherUseCase = GetCurrentWeatherUseCase(weatherRepository)

    @Provides
    fun provideGetWeatherForecastUseCase(weatherRepository: WeatherRepository): GetWeatherForecastUseCase = GetWeatherForecastUseCase(weatherRepository)
}