package com.example.globalweather.features.weather.dataLayer

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherServiceBuilder {
    companion object {
        private var retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var weatherAPIService: WeatherAPIService = retrofit.create(WeatherAPIService::class.java)
    }
}