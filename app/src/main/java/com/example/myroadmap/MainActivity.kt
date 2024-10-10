package com.example.myroadmap

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.myroadmap.data.di.InfoViewModel
import com.example.myroadmap.data.remote.model.Location
import com.example.myroadmap.domain.InfoRepository
import com.example.myroadmap.data.service.RetrofitClient
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
    val viewModel: InfoViewModel = InfoViewModel(InfoRepository(RetrofitClient.instance))
    val authKey = remember { "069821e7-54a7-4171-8d6d-352936084e18" }

    LaunchedEffect(Unit) {
        viewModel.fetchLocations(authKey)
    }

    val locations by viewModel.locations.collectAsState()
    var selectedLocation by remember { mutableStateOf<Location?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var showNoRoutesDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Taxi Locations") })
        }
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
                            Log.e("API_ERROR", "Error fetching routes: ${e.message}")
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
