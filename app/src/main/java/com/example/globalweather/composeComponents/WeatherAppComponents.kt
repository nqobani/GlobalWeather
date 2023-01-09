package com.example.globalweather.composeComponents

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.globalweather.models.list
import com.example.globalweather.utils.DateTimeUtils

@Composable
fun CurrentWeatherTempLevels(
    minTemp: Double,
    currentTemp: Double,
    maxTemp: Double
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        elevation = 1.dp
    ) {
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            TempItem("Min Temp", minTemp)
            TempItem("Current Temp", currentTemp)
            TempItem("Max Temp", maxTemp)
        }
    }
}

@Composable
fun TempItem(label: String, value: Double) {
    Column(
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = label,
            modifier = Modifier.wrapContentWidth()
        )
        Text(
            text = "${value}\u00B0",
            modifier = Modifier.wrapContentWidth()
        )
    }
}

@Composable
fun DailyWeatherItemCard(item: list) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(
            start = 16.dp,
            end = 16.dp,
            top = 4.dp,
            bottom = 4.dp
        ),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 1.dp
    ) {
        Row() {
            Text(
                text = DateTimeUtils.getWeekDay(item.dt_txt),
                modifier = Modifier.padding(start = 16.dp)
                    .height(50.dp)
                    .wrapContentHeight(Alignment.CenterVertically),
                style = MaterialTheme.typography.body1
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = "https://openweathermap.org/img/wn/${item.weather[0].icon}.png",
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(50.dp)
                )

                Text(
                    text = "${item.main.temp_min}\u00B0",
                    modifier = Modifier
                        .width(90.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = "${item.main.temp_max}\u00B0",
                    modifier = Modifier
                        .width(90.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}