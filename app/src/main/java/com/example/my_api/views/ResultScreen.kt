package com.example.my_api.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.my_api.viewmodels.ApiViewModel

@Composable
fun ResultScreen(apiViewModel: ApiViewModel, onBack: () -> Unit) {
    val lyrics by apiViewModel.currentLyrics.collectAsState()
    val errorMessage by apiViewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (errorMessage != null) {
            Text(
                text = "Error: $errorMessage",
                style = MaterialTheme.typography.bodyMedium
            )
        } else if (lyrics != null) {
            Text(
                text = "Lyrics:",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = lyrics!!.lyrics,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            Text(
                text = "Fetching Lyrics...",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}