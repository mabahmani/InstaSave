package com.mabahmani.instasave.ui.main.download

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mabahmani.instasave.data.db.entity.DownloadEntity
import com.mabahmani.instasave.domain.interactor.*
import com.mabahmani.instasave.domain.model.Download
import com.mabahmani.instasave.domain.model.enums.DownloadStatus
import com.mabahmani.instasave.domain.model.enums.MediaType
import com.mabahmani.instasave.ui.main.livestream.LiveStreamUiState
import com.mabahmani.instasave.util.DownloadManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val fetchLinkDataUseCase: FetchLinkDataUseCase,
    private val getAllDownloadsUseCase: GetAllDownloadsUseCase,
    private val addToDownloadUseCase: AddToDownloadUseCase,
    private val downloadIsExistsUseCase: DownloadIsExistsUseCase,
    private val updateDownloadStatusUseCase: UpdateDownloadStatusUseCase,
    private val updateDownloadInfoUseCase: UpdateDownloadInfoUseCase,
) : ViewModel() {

    private val _downloadUiState = MutableStateFlow<DownloadUiState>(DownloadUiState.Loading)
    val downloadUiState: StateFlow<DownloadUiState> = _downloadUiState

    fun getDownloadList() {
        viewModelScope.launch {
            getAllDownloadsUseCase().collect {

                if (it.isEmpty()) {
                    _downloadUiState.emit(DownloadUiState.EmptyList)
                } else {
                    _downloadUiState.emit(DownloadUiState.ShowDownloadsList(
                        it.map {
                            Download(
                                it.id,
                                it.fileId,
                                it.filePath,
                                it.userName,
                                it.url,
                                it.previewImageUrl,
                                mutableStateOf(
                                    kotlin.run {
                                        if (it.downloadStatus == DownloadStatus.DOWNLOADING.name) {
                                            if (DownloadManager.isInDownloadList(it.fileId))
                                                enumValueOf(it.downloadStatus)
                                            else {
                                                DownloadStatus.FAILED
                                            }
                                        } else {
                                            enumValueOf(it.downloadStatus)
                                        }
                                    }
                                ),
                                mutableStateOf(0),
                                it.createdAt,
                                enumValueOf(it.mediaType),
                                it.contentLength,
                                it.code
                            )
                        }
                    ))
                }

            }
        }
    }

    fun fetchLinkData(url: String) {

        viewModelScope.launch {

            if (url.contains("instagram.com", true)){
                _downloadUiState.emit(
                    DownloadUiState.ShowCheckUrlDialog
                )

                val result = fetchLinkDataUseCase(url)

                if (result.isSuccess) {
                    _downloadUiState.emit(
                        DownloadUiState.AddToDownload(result.getOrNull() ?: listOf())
                    )
                    Timber.d("fetchLinkData isSuccess %s", result.getOrNull())
                } else {
                    _downloadUiState.emit(
                        DownloadUiState.FetchLinkDataFailed
                    )

                    Timber.d("fetchLinkData isFailure %s", result.exceptionOrNull())
                }

            }


        }
    }

    fun addToDownloads(downloads: List<Download>) {
        viewModelScope.launch {

            if (downloadIsExistsUseCase(downloads[0].code)) {
                _downloadUiState.emit(DownloadUiState.AlreadyDownloaded)
            } else {
                addToDownloadUseCase(
                    downloads.map {
                        DownloadEntity(
                            0,
                            it.code,
                            it.url,
                            "",
                            "",
                            it.fileId,
                            it.username,
                            0,
                            it.createdAt,
                            it.previewImageUrl,
                            it.status.value.name,
                            it.mediaType.name
                        )
                    }
                )

                downloads.forEach {
                    DownloadManager.startDownload(
                        it
                    )
                }
            }

        }
    }

    fun updateDownloadStatus(fileId: String, status: DownloadStatus) {
        viewModelScope.launch {
            updateDownloadStatusUseCase(fileId, status)
        }
    }

    fun updateDownloadInfoStatus(
        fileId: String,
        downloadStatus: DownloadStatus,
        filePath: String,
        fileName: String,
        fileLength: Long
    ) {
        viewModelScope.launch {
            updateDownloadInfoUseCase(fileId, downloadStatus, filePath, fileName, fileLength)
        }
    }
}