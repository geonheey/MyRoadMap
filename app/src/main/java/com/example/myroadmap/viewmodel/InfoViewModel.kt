package com.example.myroadmap.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myroadmap.data.model.DistanceTimeResponse
import com.example.myroadmap.data.model.Location
import com.example.myroadmap.data.model.RouteResponse
import com.example.myroadmap.data.repository.InfoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InfoViewModel(private val locationRepository: InfoRepository) : ViewModel() {
    private val _locations = MutableStateFlow<List<Location>>(emptyList())
    val locations: StateFlow<List<Location>> = _locations

    fun fetchLocations(authKey: String) {
        viewModelScope.launch {
            val locations = locationRepository.fetchLocations(authKey)
            _locations.value = locations
            Log.d("API_SUCCESS1", "Locations: $locations")
        }
    }

    suspend fun fetchRoutes(
        authKey: String,
        origin: String,
        destination: String
    ): List<RouteResponse> {
        return locationRepository.fetchRoutes(authKey, origin, destination).also {
            Log.d("API_SUCCESS2", "Routes: $it")
        }
    }


    suspend fun fetchDistanceTime(
        authKey: String,
        origin: String,
        destination: String
    ): DistanceTimeResponse {
        return locationRepository.fetchDistanceTime(authKey, origin, destination)
            .also { (dist, time) ->
                Log.d("API_SUCCESS3", "Distance: $dist, Time: $time")
            }
    }

}
