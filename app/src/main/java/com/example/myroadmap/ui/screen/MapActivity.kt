package com.example.myroadmap.ui.screen

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.viewModelScope
import com.example.myroadmap.BuildConfig
import com.example.myroadmap.R
import com.example.myroadmap.data.repository.InfoRepository
import com.example.myroadmap.data.service.RetrofitClient
import com.example.myroadmap.utils.detailRoutes
import com.example.myroadmap.utils.displayRoutes
import com.example.myroadmap.utils.markRoutes
import com.example.myroadmap.viewmodel.InfoViewModel
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
        val authKey = BuildConfig.AUTHORIZATION_KEY
        val timeTextView = findViewById<TextView>(R.id.timeText)
        val distanceTextView = findViewById<TextView>(R.id.distanceText)

        KakaoMapSdk.init(this, BuildConfig.NATIVE_APP_KEY)

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
                            val (distance, time) = viewModel.fetchDistanceTime(
                                authKey,
                                origin,
                                destination
                            )

                            if (routes.isEmpty()) {
                                Log.d("API_SUCCESS8", "No routes found.")
                            } else {
                                markRoutes(kakaoMap, routes, this@MapActivity)
                                displayRoutes(kakaoMap, routes, this@MapActivity)
                                detailRoutes(timeTextView, distanceTextView, distance, time)
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


