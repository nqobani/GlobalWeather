package com.example.globalweather.di

import android.content.Context
import com.example.globalweather.Repositories.WeatherAPIRepositoryImpl
import com.example.globalweather.Repositories.interfaces.WeatherAPIRepository
import com.example.globalweather.useCases.GetCurrentWeatherUseCase
import com.example.globalweather.useCases.GetWeatherForecastUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class WeatherModules {
    @Provides
    fun provideWeatherRepository(@ApplicationContext context: Context): WeatherAPIRepository = WeatherAPIRepositoryImpl(context)

    @Provides
    fun provideGetCurrentWeatherUseCase(weatherAPIRepository: WeatherAPIRepository): GetCurrentWeatherUseCase = GetCurrentWeatherUseCase(weatherAPIRepository)

    @Provides
    fun provideGetWeatherForecastUseCase(weatherAPIRepository: WeatherAPIRepository): GetWeatherForecastUseCase = GetWeatherForecastUseCase(weatherAPIRepository)
}