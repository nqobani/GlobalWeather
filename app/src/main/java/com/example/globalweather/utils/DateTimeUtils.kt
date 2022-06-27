package com.example.globalweather.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateTimeUtils {
    companion object{
        fun getWeekDay(dateTimeString: String): String{
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val localDateTime: LocalDateTime = LocalDateTime.parse(dateTimeString, formatter)
            return localDateTime.dayOfWeek.name
        }

        fun getHour(dateTimeString: String): Int{
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val localDateTime: LocalDateTime = LocalDateTime.parse(dateTimeString, formatter)
            return localDateTime.hour
        }
    }

}