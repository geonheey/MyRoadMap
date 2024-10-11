package com.example.myroadmap.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myroadmap.data.remote.model.Location

@Composable
fun LocationItem(location: Location, onClick: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .clickable(onClick = onClick)) {
        Column {
            Text(location.origin, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(location.destination, style = MaterialTheme.typography.bodyMedium)
        }
    }
}