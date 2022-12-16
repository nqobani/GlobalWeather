package com.example.globalweather.adaptersAndViewHolders

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.globalweather.databinding.WeatherForecastItemBinding
import com.example.globalweather.models.list
import com.example.globalweather.utils.DateTimeUtils

class WeatherForecastRecyclerViewAdapter(var context: Context, val weatherForecast: List<list>) :
    RecyclerView.Adapter<WeatherForecastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherForecastViewHolder {
        return WeatherForecastViewHolder(
            context,
            WeatherForecastItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: WeatherForecastViewHolder, position: Int) {
        val item = weatherForecast[position]
        holder.displayData(
            DateTimeUtils.getWeekDay(item.dt_txt),
            item.weather[0].icon,
            item.main.temp_min,
            item.main.temp_max
        )
    }

    override fun getItemCount() = weatherForecast.size
}