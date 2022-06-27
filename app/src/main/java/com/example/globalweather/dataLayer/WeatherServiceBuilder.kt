package com.example.globalweather.dataLayer

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class WeatherServiceBuilder {
    companion object{
        var retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var weatherAPIService: WeatherAPI = retrofit.create(WeatherAPI::class.java)
    }
}