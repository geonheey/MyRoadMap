package com.example.myroadmap.data.service

import com.example.myroadmap.data.remote.model.DistanceTimeResponse
import com.example.myroadmap.data.remote.model.LocationResponse
import com.example.myroadmap.data.remote.model.RouteResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    // 출발지/도착지 리스트 API
    @GET("api/v1/coding-assignment/locations")
    fun getLocations(
        @Header("Authorization") authKey: String
    ): Call<LocationResponse>

    // 경로 조회 API
    @GET("api/v1/coding-assignment/routes")
    fun getRoutes(
        @Header("Authorization") authKey: String,
        @Query("origin") origin: String,
        @Query("destination") destination: String
    ): Call<List<RouteResponse>>

    // 시간 및 거리 조회 API
    @GET("api/v1/coding-assignment/distance-time")
    fun getDistanceTime(
        @Header("Authorization") authKey: String,
        @Query("origin") origin: String,
        @Query("destination") destination: String
    ): Call<DistanceTimeResponse>

}
