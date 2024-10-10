package com.example.myroadmap.utils

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.myroadmap.R
import com.example.myroadmap.data.remote.model.RouteResponse
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.route.RouteLineStyles


fun displayRoutes(kakaoMap: KakaoMap?, routes: List<RouteResponse>, context: Context) {
    if (routes.isEmpty()) {
        Log.d("API_SUCCESS9", "루트 없음")
        return
    }

    routes.forEach { route ->
        if (route.points.isEmpty()) {
            Log.d("API_SUCCESS10", "Route points is empty for this route.")
            return@forEach
        }

        val trafficState = route.traffic_state
        Log.d("API_SUCCESS10", "Traffic State: $trafficState")

        val points = route.points.split(" ")
        val latLngs = points.map {
            val (lng, lat) = it.split(",").map(String::toDouble)
            LatLng.from(lat, lng)
        }

        val layer = kakaoMap!!.routeLineManager!!.layer

        val colorResId = when (trafficState) {
            "UNKNOWN" -> R.color.route_unknown
            "BLOCK" -> R.color.route_block
            "JAM" -> R.color.route_jam
            "DELAY" -> R.color.route_delay
            "SLOW" -> R.color.route_slow
            "NORMAL" -> R.color.route_normal
            else -> R.color.stroke_color
        }

        val color = ContextCompat.getColor(context, colorResId) // 실제 색상 값

        val styles = RouteLineStyles.from(RouteLineStyle.from(16F, color))

        val segments = listOf(
            RouteLineSegment.from(latLngs, styles)
        )

        val options = RouteLineOptions.from(segments)

        val routeLine = layer.addRouteLine(options)

        if (routeLine != null) {
            Log.d("API_SUCCESS12", "RouteLine added successfully with traffic state: $routeLine")
        } else {
            Log.e("API_ERROR", "Failed to add RouteLine")
        }
    }
    Log.d("API_SUCCESS10", "Routes displayed on the map")
}