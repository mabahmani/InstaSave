package com.mabahmani.instasave.ui.main.livestream

import com.mabahmani.instasave.domain.model.LiveStream

sealed class LiveStreamUiState {
    object Loading : LiveStreamUiState()
    object NetworkError : LiveStreamUiState()
    object Unauthorized : LiveStreamUiState()
    object EmptyList : LiveStreamUiState()
    class Error(val message: String) : LiveStreamUiState()
    class ShowLiveStreams(val liveStreams: List<LiveStream>) : LiveStreamUiState()
}
