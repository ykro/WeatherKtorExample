package com.example.weatherktorexample

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable data class WeatherResponse(val main: Main, val weather: List<Weather>)
@Serializable data class Main(val temp: Double)
@Serializable data class Weather(val description: String)

class WeatherViewModel : ViewModel() {
    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private val _city = MutableStateFlow("")
    val city: StateFlow<String> = _city

    private val _weather = MutableStateFlow<WeatherResponse?>(null)
    val weather: StateFlow<WeatherResponse?> = _weather

    fun onCityChange(newCity: String) {
        _city.value = newCity
    }

    fun getWeather() {
        viewModelScope.launch {
            try {
                val response: WeatherResponse = client.get("https://api.openweathermap.org/data/2.5/weather") {
                    parameter("q", city.value)
                    parameter("appid", "d49f768bd5ea4f249972455f214cadfe")
                    parameter("units", "metric")
                }.body()
                _weather.value = response
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error: ${e.message}")
            }
        }
    }
}
