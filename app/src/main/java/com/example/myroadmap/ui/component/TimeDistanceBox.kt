package com.example.myroadmap.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TimeDistanceBox(routeTime: String, distance: String) {
    Text(
        text = "시간 : $routeTime\n거리 : $distance",
        modifier = Modifier
            .padding(16.dp)
            .background(Color.White)
    )
}
