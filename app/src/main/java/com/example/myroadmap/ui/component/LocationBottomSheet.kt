package com.example.myroadmap.ui.component

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myroadmap.data.model.Location
import com.example.myroadmap.ui.screen.MapActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationBottomSheet(
    location: Location,
    routesCheck: Boolean,
    errorCode: Int? = null,
    errorMessage: String? = null,
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
            if (routesCheck) {
                LocationBottomSheetContent(location, "경로 조회 결과", true)
                Button(
                    onClick = {
                        onRouteCheck(location.origin, location.destination) {
                            val intent = Intent(context, MapActivity::class.java).apply {
                                putExtra("ORIGIN", location.origin)
                                putExtra("DESTINATION", location.destination)
                            }
                            context.startActivity(intent)
                            onDismiss()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSecondaryContainer)
                ) {
                    Text("경로 확인", color = Color.White)
                }
            } else {
                LocationBottomSheetContent(location, "경로 조회 실패", false, errorCode, errorMessage)
                Button(
                    onClick = {
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onErrorContainer)
                ) {
                    Text("확인", color = Color.White)
                }
            }
        }
    }
}
