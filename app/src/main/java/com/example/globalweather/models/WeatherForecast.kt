package com.example.globalweather.models

data class WeatherForecast(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<list>,
    val message: String
)