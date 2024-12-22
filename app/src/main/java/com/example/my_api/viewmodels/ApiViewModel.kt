package com.example.my_api.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_api.models.LyricsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

class ApiViewModel : ViewModel() {
    private val _currentLyrics = MutableStateFlow<LyricsModel?>(null)
    val currentLyrics: StateFlow<LyricsModel?> = _currentLyrics.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    //moving request job to make back-button in UI cancel pending request
    private var apiRequestJob: Job? = null

    fun searchLyrics(artist: String, track: String) {
        _errorMessage.value = null
        _currentLyrics.value = null

        if (artist.isEmpty() || track.isEmpty()) {
            _errorMessage.value = "Please enter both artist and track."
            return
        }

        apiRequestJob?.cancel()

        apiRequestJob = viewModelScope.launch {
            try {
                val lyrics = fetchLyricsFromApi(artist, track)

                if (lyrics != null) {
                    _currentLyrics.value = lyrics
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Lyrics not found!"
                }
            }
            catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.localizedMessage}"
            }
        }
    }

    private suspend fun fetchLyricsFromApi(artist: String, track: String): LyricsModel? {
        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://api.lyrics.ovh/v1/$artist/$track")
                    .build()

                val response = client.newCall(request).execute()

                if (!response.isSuccessful) {
                    null
                } else {
                    val jsonString = response.body?.string().orEmpty()
                    Json { ignoreUnknownKeys = true }.decodeFromString<LyricsModel>(jsonString)
                }
            } catch (e: Exception) {
                 throw e
            }
        }
    }

    // Cancel the request explicitly when necessary (e.g., on back press or view destruction)
    fun cancelRequest() {
        apiRequestJob?.cancel()
    }
}

