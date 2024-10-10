package com.example.myroadmap.domain

import android.util.Log
import com.example.myroadmap.data.remote.model.DistanceTimeResponse
import com.example.myroadmap.data.remote.model.Location
import com.example.myroadmap.data.remote.model.RouteResponse
import com.example.myroadmap.data.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InfoRepository(private val apiService: ApiService) {

    // 출발지/도착지 리스트 가져오기
    suspend fun fetchLocations(authKey: String): List<Location> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getLocations(authKey).execute()
                Log.d("API_RESPONSE", "Location: $response")

                if (response.isSuccessful) {
                    response.body()?.locations ?: emptyList()
                } else {
                    Log.e("API_ERROR", "Error fetching locations: ${response.message()}")
                    emptyList()
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error: ${e}")
                emptyList()
            }
        }
    }

    // 경로 조회
    suspend fun fetchRoutes(
        authKey: String,
        origin: String,
        destination: String
    ): List<RouteResponse> {
        return withContext(Dispatchers.IO) {

            try {
                val response = apiService.getRoutes(authKey, origin, destination).execute()

                if (response.isSuccessful) {
                    response.body() ?: emptyList()
                } else {
                    Log.e("API_ERROR", "Error fetching routes: ${response.message()}")
                    emptyList()
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error: ${e}")
                emptyList()
            }
        }
    }

    // 거리 및 시간 조회
    suspend fun fetchDistanceTime(
        authKey: String,
        origin: String,
        destination: String
    ): DistanceTimeResponse {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getDistanceTime(authKey, origin, destination).execute()

                if (response.isSuccessful) {
                    response.body() ?: DistanceTimeResponse(0, 0)
                } else {
                    Log.e("API_ERROR", "Error fetching distance and time: ${response.message()}")
                    DistanceTimeResponse(0, 0)
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error: ${e.message}")
                DistanceTimeResponse(0, 0)
            }
        }
    }
}