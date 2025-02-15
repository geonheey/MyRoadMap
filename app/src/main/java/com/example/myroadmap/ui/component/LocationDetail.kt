package com.example.myroadmap.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myroadmap.data.model.Location
import com.example.myroadmap.ui.theme.BlueBox
import com.example.myroadmap.ui.theme.BlueText

@Composable
fun LocationDetail(location: Location) {
    Row {
        Text(
            text = "출발지 : ",
            style = MaterialTheme.typography.labelLarge,
        )
        Text(
            text = location.origin,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }

    Spacer(modifier = Modifier.height(4.dp))

    Row {
        Text(
            text = "도착지 : ",
            style = MaterialTheme.typography.labelLarge,
        )
        Text(
            text = location.destination,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}
