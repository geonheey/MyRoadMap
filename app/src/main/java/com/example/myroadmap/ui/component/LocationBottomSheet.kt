package com.example.myroadmap.ui.component

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Origin: ${location.origin}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Destination: ${location.destination}",
                style = MaterialTheme.typography.bodyMedium
            )
            Log.d(
                "API_SUCCESS6",
                "Origin: ${location.origin}, Destination: ${location.destination}"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                onRouteCheck(location.origin, location.destination) {
                    val intent = Intent(context, MapActivity::class.java).apply {
                        putExtra("ORIGIN", location.origin)
                        putExtra("DESTINATION", location.destination)
                    }
                    context.startActivity(intent)
                }
            }) {
                Text("경로 확인")
            }
        }
    }
}
