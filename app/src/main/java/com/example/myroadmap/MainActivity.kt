package com.example.myroadmap

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import com.example.myroadmap.data.di.InfoViewModel
import com.example.myroadmap.data.remote.model.Location
import com.example.myroadmap.data.service.RetrofitClient
import com.example.myroadmap.domain.InfoRepository
import com.example.myroadmap.ui.component.LocationBottomSheet
import com.example.myroadmap.ui.component.LocationItem
import com.example.myroadmap.ui.theme.MyRoadMapTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: InfoViewModel
    private lateinit var locationRepository: InfoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationRepository = InfoRepository(RetrofitClient.instance)
        viewModel = InfoViewModel(locationRepository)
        setContent {
            TaxiApp()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaxiApp() {
    val viewModel = InfoViewModel(InfoRepository(RetrofitClient.instance))
    val authKey = remember { "069821e7-54a7-4171-8d6d-352936084e18" }

    LaunchedEffect(Unit) {
        viewModel.fetchLocations(authKey)
    }

    val locations by viewModel.locations.collectAsState()
    var selectedLocation by remember { mutableStateOf<Location?>(null) }
    var hasRoutes by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var errorCode by remember { mutableStateOf(0) }

    MyRoadMapTheme {
        Scaffold {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                LazyColumn {
                    items(locations) { location ->
                        LocationItem(location) {
                            selectedLocation = location
                            viewModel.viewModelScope.launch {
                                try {
                                    val routes = viewModel.fetchRoutes(
                                        authKey,
                                        location.origin,
                                        location.destination
                                    )
                                    hasRoutes = true
                                } catch (e: Exception) {
                                    val errorParts = e.message?.split(", ") ?: listOf("0", "Unknown error")
                                    errorCode = errorParts[0].substringAfter("Code: ").toIntOrNull() ?: -1
                                    errorMessage = errorParts[1].substringAfter("Message: ")
                                    hasRoutes = false
                                }
                            }
                        }
                    }
                }
            }

            if (selectedLocation != null) {
                LocationBottomSheet(
                    location = selectedLocation!!,
                    routesCheck = hasRoutes,
                    errorCode = if (errorCode != 0) errorCode else null,
                    errorMessage = errorMessage.ifEmpty { null },
                    onDismiss = {
                        hasRoutes = false
                        selectedLocation = null
                    },
                    onRouteCheck = { origin, destination, onSuccess ->
                        viewModel.viewModelScope.launch {
                            try {
                                val routes = viewModel.fetchRoutes(authKey, origin, destination)
                                onSuccess()
                                Log.d("API_ERROR", "${selectedLocation}")

                            } catch (e: Exception) {
                                Log.e("API_ERROR", "${e.message}")
                            }
                        }
                    }
                )
            }
        }
    }
}
