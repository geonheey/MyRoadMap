package com.example.myroadmap.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myroadmap.ui.theme.BlueBox
import com.example.myroadmap.ui.theme.YelloText

@Composable
fun TimeDistanceBox(distance: String, routeTimeText: String) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = BlueBox, contentColor =YelloText)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center) {
            Text(
                text = "시간 : $routeTimeText\n거리 : $distance",
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Start,
            )
        }
    }
}
