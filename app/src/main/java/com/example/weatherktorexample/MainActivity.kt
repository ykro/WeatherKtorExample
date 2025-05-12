package com.example.weatherktorexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import com.example.weatherktorexample.ui.theme.WeatherKtorExampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherKtorExampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WeatherApp()
                }
            }
        }
    }
}

@Composable
fun WeatherApp(viewModel: WeatherViewModel = viewModel()) {
    val city by viewModel.city.collectAsState()
    val weatherInfo by viewModel.weather.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = city,
            onValueChange = viewModel::onCityChange,
            label = { Text("City") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = { viewModel.getWeather() }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Check weather")
        }

        weatherInfo?.let {
            Text(
                text = "Temperature: ${it.main.temp} °C\nWeather: ${it.weather.firstOrNull()?.description ?: "No info"}"
            )

            //Text("Temperature: ${it.main.temp} °C\nWeather: ${it.weather.firstOrNull()?.description}")
        }
    }
}