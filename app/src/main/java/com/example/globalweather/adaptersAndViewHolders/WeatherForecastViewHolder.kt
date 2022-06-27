package com.example.globalweather.adaptersAndViewHolders

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.globalweather.databinding.WeatherForecastItemBinding
import com.example.globalweather.utils.DisplayImageHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherForecastViewHolder(var context: Context, val binding: WeatherForecastItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun displayData(dayOfWeek: String, iconName:String, minTemp:Double, maxTem:Double){
        CoroutineScope(Dispatchers.Main).launch {
            DisplayImageHelper.displayImage(context, binding.weatherIcon, "https://openweathermap.org/img/wn/${iconName}.png")
            binding.dayOfTheWeekTextView.text = dayOfWeek
            binding.maxTempTextView.text = "${maxTem}\u00B0"
            binding.minTempTextView.text = "${minTemp}\u00B0"
        }
    }
}