package com.example.myroadmap.ui.component

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myroadmap.MapActivity
import com.example.myroadmap.data.remote.model.Location

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationBottomSheet(
    location: Location,
    onDismiss: () -> Unit,
    onRouteCheck: (String, String, () -> Unit) -> Unit
) {
    val context = LocalContext.current

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "경로 조회 결과",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Box(
                modifier = Modifier.fillMaxWidth().padding(vertical=8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "출발지: ${location.origin}",
                        style = MaterialTheme.typography.bodyLarge,
                    )

                    Text(
                        text = "도착지: ${location.destination}",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }

            Log.d(
                "API_SUCCESS6",
                "Origin: ${location.origin}, Destination: ${location.destination}"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onRouteCheck(location.origin, location.destination) {
                        val intent = Intent(context, MapActivity::class.java).apply {
                            putExtra("ORIGIN", location.origin)
                            putExtra("DESTINATION", location.destination)
                        }
                        context.startActivity(intent)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSecondaryContainer )
            ) {
                Text("경로 확인", color = Color.White)
            }
        }
    }
}
