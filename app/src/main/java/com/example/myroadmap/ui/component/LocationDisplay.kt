package com.example.myroadmap.ui.component


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myroadmap.data.model.Location

@Composable
fun LocationDisplay(
    location: Location,
    title: String,
    isSuccessful: Boolean,
    errorCode: Int? = null,
    errorMessage: String? = null
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(bottom = 12.dp)
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (isSuccessful) {
                RouteDetail(location)
            } else {
                Text(
                    text = "[${location.origin}~${location.destination}] : 경로가 존재하지 않습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Error Code: $errorCode",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
                Text(
                    text = "Error Message: $errorMessage",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )


            }
            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}
