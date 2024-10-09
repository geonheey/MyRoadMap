package com.example.myroadmap

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView

class MainActivity : ComponentActivity() {
    private lateinit var mapView: MapView
    private var kakaoMap: KakaoMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // MapView 초기화
        mapView = findViewById(R.id.map_view)

        // KakaoMapSdk 초기화
        KakaoMapSdk.init(this, "11a77d025bcc6597303aed81d23dd3f1")

        // MapView 시작
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d("KakaoMap", "MapView destroyed")
            }

            override fun onMapError(p0: Exception?) {
                Log.e("KakaoMap", "onMapError: ${p0?.message}")
            }

        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                this@MainActivity.kakaoMap = kakaoMap
                Log.d("KakaoMap", "Map is ready")
            }
        })

        Log.d("KakaoMap", "MapView initialized")
    }

    override fun onResume() {
        super.onResume()
        mapView.resume() // MapView resume
        Log.d("KakaoMap", "MapView resumed")
    }

    override fun onPause() {
        super.onPause()
        mapView.pause() // MapView pause
        Log.d("KakaoMap", "MapView paused")
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.finish() // MapView destroy
        Log.d("KakaoMap", "MapView destroyed")
    }
}
