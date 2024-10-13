package com.example.myroadmap.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewModelScope
import com.example.myroadmap.BuildConfig
import com.example.myroadmap.ui.component.TimeDistanceBox
import com.example.myroadmap.utils.detailRoutes
import com.example.myroadmap.utils.displayRoutes
import com.example.myroadmap.utils.markRoutes
import com.example.myroadmap.viewmodel.InfoViewModel
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import kotlinx.coroutines.launch


@Composable
fun MapScreen(origin: String?, destination: String?, viewModel: InfoViewModel) {
    val context = LocalContext.current
    val mapView = MapView(context)

    var distanceText by remember { mutableStateOf("") }
    var routeTimeText by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize(),
            update = {
                it.start(object : MapLifeCycleCallback() {
                    override fun onMapDestroy() {}
                    override fun onMapError(p0: Exception?) {
                        Log.e("KakaoMap", "onMapError: ${p0?.message}")
                    }
                }, object : KakaoMapReadyCallback() {
                    override fun onMapReady(kakaoMap: KakaoMap) {
                        if (origin != null && destination != null) {
                            viewModel.viewModelScope.launch {
                                val routes = viewModel.fetchRoutes(
                                    BuildConfig.AUTHORIZATION_KEY,
                                    origin,
                                    destination
                                )
                                val (distance, time) = viewModel.fetchDistanceTime(
                                    BuildConfig.AUTHORIZATION_KEY,
                                    origin,
                                    destination
                                )

                                if (routes.isEmpty()) {
                                    Log.d("API_SUCCESS8", "No routes found.")
                                } else {
                                    markRoutes(kakaoMap, routes, context)
                                    displayRoutes(kakaoMap, routes, context)

                                    val (distText, timeString) = detailRoutes(distance, time)
                                    distanceText = distText
                                    routeTimeText = timeString
                                }
                            }
                        }
                    }
                })
            }
        )
        Box(
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            TimeDistanceBox(routeTimeText, distanceText)
        }
    }
}

