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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
    var showBottomSheet by remember { mutableStateOf(false) }
    var showNoRoutesDialog by remember { mutableStateOf(false) }

    Scaffold(

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn {
                items(locations) { location ->
                    LocationItem(location) {
                        selectedLocation = location
                        showBottomSheet = true
                    }
                }
            }
        }

        if (showBottomSheet && selectedLocation != null) {
            LocationBottomSheet(
                location = selectedLocation!!,
                onDismiss = {
                    showBottomSheet = false
                },
                onRouteCheck = { origin, destination, onSuccess ->
                    viewModel.viewModelScope.launch {
                        try {
                            val routes = viewModel.fetchRoutes(authKey, origin, destination)
                            Log.d("API_SUCCESS5", "Fetched Routes: $routes")

                            if (routes.isEmpty()) {
                                showNoRoutesDialog = true
                            } else {
                                onSuccess()
                            }
                        } catch (e: Exception) {
                            Log.e("API_ERROR1", "Error fetching routes: ${e.message}")
                        }
                    }
                }
            )
        }

        if (showNoRoutesDialog) {
            AlertDialog(
                onDismissRequest = { showNoRoutesDialog = false },
                title = { Text("경로 없음") },
                text = { Text("선택한 위치에 대한 경로가 없습니다.") },
                confirmButton = {
                    Button(onClick = {
                        showNoRoutesDialog = false
                    }) {
                        Text("확인")
                    }
                }
            )
        }
    }
}
