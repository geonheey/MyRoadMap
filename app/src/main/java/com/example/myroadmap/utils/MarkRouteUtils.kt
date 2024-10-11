package com.example.myroadmap.utils

import android.content.Context
import android.util.Log
import com.example.myroadmap.R
import com.example.myroadmap.data.remote.model.RouteResponse
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.LatLngBounds
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

fun markRoutes(kakaoMap: KakaoMap?, routes: List<RouteResponse>, context: Context) {
    if (routes.isNotEmpty()) {
        val allLatLngs = mutableListOf<LatLng>()

        routes.forEach { route ->
            val points = route.points.split(" ")
            val latLngs = points.map {
                val (lng, lat) = it.split(",").map(String::toDouble)
                LatLng.from(lat, lng)
            }
            allLatLngs.addAll(latLngs)
        }

        val latLngArray = allLatLngs.toTypedArray()
        Log.d("API_SUCCESS11", "LatLng array size: ${latLngArray.size}")

        if (latLngArray.isNotEmpty()) {
            val firstLatLng = latLngArray.first()
            val lastLatLng = latLngArray.last()

            val boundsBuilder = LatLngBounds.Builder()
            boundsBuilder.include(firstLatLng)
            boundsBuilder.include(lastLatLng)

            val bounds = boundsBuilder.build()
            kakaoMap?.moveCamera(CameraUpdateFactory.fitMapPoints(bounds, 200))

            val originStyles = LabelStyles.from(
                "originStyleId",
                LabelStyle.from(R.drawable.ic_origin_mark).setZoomLevel(1)
            )

            val destinationStyles = LabelStyles.from(
                "destinationStyleId",
                LabelStyle.from(R.drawable.ic_destination_mark).setZoomLevel(1)
            )

            val labelManager = kakaoMap?.labelManager
            labelManager?.addLabelStyles(originStyles)
            labelManager?.addLabelStyles(destinationStyles)

            labelManager?.layer?.addLabel(
                LabelOptions.from(firstLatLng)
                    .setStyles(originStyles)
            )

            labelManager?.layer?.addLabel(
                LabelOptions.from(lastLatLng)
                    .setStyles(destinationStyles)
            )

            displayRoutes(kakaoMap, routes, context)
            Log.d("API_SUCCESS11", "$firstLatLng to $lastLatLng")
        } else {
            Log.d("API_SUCCESS11", "LatLng array empty 상태.")
        }
    } else {
        Log.d("API_SUCCESS11", "Routes list empty 상태.")
    }
}
