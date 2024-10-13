package com.example.myroadmap.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.myroadmap.BuildConfig
import com.example.myroadmap.data.repository.InfoRepository
import com.example.myroadmap.data.service.RetrofitClient
import com.example.myroadmap.viewmodel.InfoViewModel
import com.kakao.vectormap.KakaoMapSdk

class MapActivity : ComponentActivity() {
    private lateinit var viewModel: InfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = InfoViewModel(InfoRepository(RetrofitClient.instance))

        val origin = intent.getStringExtra("ORIGIN")
        val destination = intent.getStringExtra("DESTINATION")

        setContent {
            MapScreen(origin, destination, viewModel)
        }

        KakaoMapSdk.init(this, BuildConfig.NATIVE_APP_KEY)
    }
}
