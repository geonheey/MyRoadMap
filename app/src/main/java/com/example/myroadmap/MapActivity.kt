package com.example.myroadmap

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.viewModelScope
import com.example.myroadmap.data.di.InfoViewModel
import com.example.myroadmap.data.remote.model.RouteResponse
import com.example.myroadmap.data.service.RetrofitClient
import com.example.myroadmap.domain.InfoRepository
import com.example.myroadmap.utils.displayRoutes
import com.example.myroadmap.utils.markRoutes
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import kotlinx.coroutines.launch

class MapActivity : ComponentActivity() {
    private lateinit var mapView: MapView
    private var kakaoMap: KakaoMap? = null
    private lateinit var viewModel: InfoViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val origin = intent.getStringExtra("ORIGIN")
        val destination = intent.getStringExtra("DESTINATION")

        mapView = findViewById(R.id.map_view)
        viewModel = InfoViewModel(InfoRepository(RetrofitClient.instance))
        val authKey = "069821e7-54a7-4171-8d6d-352936084e18"

        KakaoMapSdk.init(this, "11a77d025bcc6597303aed81d23dd3f1")

        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
            }

            override fun onMapError(p0: Exception?) {
                Log.e("KakaoMap", "onMapError: ${p0?.message}")
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                this@MapActivity.kakaoMap = kakaoMap

                if (origin != null && destination != null) {
                    viewModel.viewModelScope.launch {
                        try {
                            val routes = viewModel.fetchRoutes(authKey, origin, destination)
                            val (distance, time) = viewModel.fetchDistanceTime(authKey, origin, destination)

                            if (routes.isEmpty()) {
                                Log.d("API_SUCCESS8", "No routes found.")
                            } else {
                                markRoutes(routes)
                                displayRoutes(routes)
                                infoUtils(distance, time)
                            }
                        } catch (e: Exception) {
                            Log.e("API_ERROR5", "${e.message}")
                        }
                    }
                }
            }
        })

        Log.d("KakaoMap", "MapView initialized")
    }

    private fun displayRoutes(routes: List<RouteResponse>) {
        displayRoutes(kakaoMap, routes, this)
    }

    private fun markRoutes(routes: List<RouteResponse>) {
        markRoutes(kakaoMap, routes, this)
    }
    @SuppressLint("DefaultLocale")
    private fun infoUtils(distanceInMeters: Int, timeInSeconds: Int) {
        val timeTextView = findViewById<TextView>(R.id.timeText)
        val distanceTextView = findViewById<TextView>(R.id.distanceText)

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

        timeTextView.text = "시간 : $timeString"
        distanceTextView.text = "거리 : $distanceString"
    }


    override fun onResume() {
        super.onResume()
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.finish()
    }
}


