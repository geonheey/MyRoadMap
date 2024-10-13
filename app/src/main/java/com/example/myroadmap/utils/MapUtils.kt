package com.example.myroadmap.utils


import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.myroadmap.R
import com.example.myroadmap.data.model.RouteResponse
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.LatLngBounds
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.route.RouteLineStyles

fun displayRoutes(kakaoMap: KakaoMap?, routes: List<RouteResponse>, context: Context) {
    if (routes.isEmpty()) {
        return
    }

    val allSegments = mutableListOf<RouteLineSegment>()

    routes.forEach { route ->
        if (route.points.isEmpty()) {
            return@forEach
        }

        val trafficState = route.traffic_state
        val points = route.points.split(" ")
        val latLngs = points.map {
            val (lng, lat) = it.split(",").map(String::toDouble)
            LatLng.from(lat, lng)
        }

        val colorResId = when (trafficState) {
            "UNKNOWN" -> R.color.route_unknown
            "BLOCK" -> R.color.route_block
            "JAM" -> R.color.route_jam
            "DELAY" -> R.color.route_delay
            "SLOW" -> R.color.route_slow
            "NORMAL" -> R.color.route_normal
            else -> R.color.stroke_color
        }

        val color = ContextCompat.getColor(context, colorResId)
        val styles = RouteLineStyles.from(RouteLineStyle.from(16F, color))

        allSegments.add(RouteLineSegment.from(latLngs, styles))
    }

    val options = RouteLineOptions.from(allSegments)
    kakaoMap?.routeLineManager?.layer?.addRouteLine(options)
}

fun markRoutes(kakaoMap: KakaoMap?, routes: List<RouteResponse>, context: Context) {
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


}


@SuppressLint("DefaultLocale")
fun detailRoutes(distanceInMeters: Int, timeInSeconds: Int): Pair<String, String> {
    val hours = timeInSeconds / 3600
    val minutes = (timeInSeconds % 3600) / 60
    val seconds = timeInSeconds % 60

    val timeString = when {
        hours > 0 -> String.format("%d 시 %d 분 %d 초", hours, minutes, seconds)
        minutes > 0 -> String.format("%d 분 %d 초", minutes, seconds)
        else -> String.format("%d 초", seconds)
    }

    val distanceString = if (distanceInMeters >= 1000) {
        String.format("%.1f km", distanceInMeters / 1000.0)
    } else {
        String.format("%d m", distanceInMeters)
    }

    return Pair(timeString, distanceString)
}
