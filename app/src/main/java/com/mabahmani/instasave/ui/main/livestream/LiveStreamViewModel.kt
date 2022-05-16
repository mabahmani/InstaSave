package com.mabahmani.instasave.ui.main.livestream

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mabahmani.instasave.data.api.response.Failure
import com.mabahmani.instasave.domain.interactor.GetCurrentLiveStreamsUseCase
import com.mabahmani.instasave.ui.intro.IntroUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LiveStreamViewModel @Inject constructor(
    private val getCurrentLiveStreamsUseCase: GetCurrentLiveStreamsUseCase
) : ViewModel() {

    private val _liveStreamUiState = MutableStateFlow<LiveStreamUiState>(LiveStreamUiState.Loading)
    val liveStreamUiState: StateFlow<LiveStreamUiState> = _liveStreamUiState

    fun launch() {

        viewModelScope.launch {
            val result = getCurrentLiveStreamsUseCase()

            if (result.isSuccess) {
                val liveStreams = result.getOrNull()

                if (liveStreams.isNullOrEmpty()){
                    _liveStreamUiState.emit(LiveStreamUiState.EmptyList)
                }

                else{
                    _liveStreamUiState.emit(LiveStreamUiState.ShowLiveStreams(liveStreams))
                }

            } else {
                when (result.exceptionOrNull()) {
                    is Failure.HttpErrorUnauthorized -> _liveStreamUiState.emit(LiveStreamUiState.Unauthorized)
                    is Failure.NetworkConnectionError -> _liveStreamUiState.emit(LiveStreamUiState.NetworkError)
                    else -> _liveStreamUiState.emit(LiveStreamUiState.Error(result.exceptionOrNull()?.message ?: ""))
                }
            }
        }
    }
}