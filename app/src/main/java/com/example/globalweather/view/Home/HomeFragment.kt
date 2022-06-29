package com.example.globalweather.view.Home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.globalweather.ViewModels.WeatherViewModel
import com.example.globalweather.adaptersAndViewHolders.WeatherForecastRecyclerViewAdapter
import com.example.globalweather.databinding.FragmentHomeBinding
import com.example.globalweather.models.list
import com.example.globalweather.utils.DateTimeUtils
import com.example.globalweather.utils.DisplayImageHelper
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class HomeFragment : Fragment(), HomeView<HomeViewState> {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val weatherViewModel by viewModels<WeatherViewModel>()
    lateinit var binding: FragmentHomeBinding

    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private val requestMultiplePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true && permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            activity?.let { activity ->
                getLocation(activity)
            }
        } else {
            //TODO: Handle Permission denied.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        weatherViewModel.state.observe(this) { viewState ->
            render(viewState)
        }

        activity?.let { activity ->
            getLocation(activity)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(lat: String, lon: String) =
            HomeFragment().apply {
            }
    }

    private fun getLocation(activity: Activity){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(30)
            fastestInterval = TimeUnit.SECONDS.toMillis(30)
            maxWaitTime = TimeUnit.MINUTES.toMillis(2)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation.let { location ->
                    CoroutineScope(Dispatchers.IO).launch {
                        weatherViewModel.intent.send(HomeIntents.GetCurrentWeather(location.altitude, location.longitude))
                    }
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestMultiplePermissions.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
            return
        } else {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }

    @SuppressLint("SetTextI18n")
    override fun render(state: HomeViewState) {
        CoroutineScope(Dispatchers.Main).launch {
            when(state){
                is HomeViewState.Loading -> {

                }
                is HomeViewState.CurrentWeatherData -> {
                    binding.cityNameTextView.text = state.currentWeather.name
                    binding.currentTempTextView.text = "${state.currentWeather.main.temp}\u00B0"
                    binding.currentWeatherTempTextView.text = "${state.currentWeather.main.temp}\u00B0"
                    binding.minTempTextView.text = "${state.currentWeather.main.temp_min}\u00B0"
                    binding.maxTempTextView.text = "${state.currentWeather.main.temp_max}\u00B0"
                    binding.currentWeatherDescriptionTextView.text = state.currentWeather.weather[0].description
                    DisplayImageHelper.displayImage(this@HomeFragment.context!!, binding.weatherIconImageView, "https://openweathermap.org/img/wn/${state.currentWeather.weather[0].icon}.png")

                    CoroutineScope(Dispatchers.IO).launch {
                        weatherViewModel.intent.send(HomeIntents.GetWeatherForecast(state.currentWeather.coord.lat, state.currentWeather.coord.lon))
                    }
                }
                is HomeViewState.WeatherForecastData -> {
                    val items : MutableList<list> = mutableListOf()
                    state.weatherForecast.list.forEach{ iterm ->
                        if(DateTimeUtils.getHour(iterm.dt_txt) == 12){
                            items.add(iterm)
                        }
                    }
                    val adapter = WeatherForecastRecyclerViewAdapter(this@HomeFragment.context!!, items)
                    binding.weatherForecastRecyclerView.adapter = adapter
                    binding.weatherForecastRecyclerView.layoutManager = LinearLayoutManager(this@HomeFragment.context)
                    binding.weatherForecastRecyclerView.setHasFixedSize(true)
                }
                is HomeViewState.OnError -> {
                }
            }
        }
    }
}