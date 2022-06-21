package com.mabahmani.instasave.ui.main.download

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mabahmani.instasave.data.api.response.Failure
import com.mabahmani.instasave.data.db.entity.DownloadEntity
import com.mabahmani.instasave.domain.interactor.*
import com.mabahmani.instasave.domain.model.Download
import com.mabahmani.instasave.domain.model.enums.DownloadStatus
import com.mabahmani.instasave.ui.main.livestream.LiveStreamUiState
import com.mabahmani.instasave.util.DownloadManager
import com.mabahmani.instasave.util.FileHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val deleteDownloadUseCase: DeleteDownloadUseCase,
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
                        it.map { downloadEntity ->
                            Download(
                                downloadEntity.id,
                                downloadEntity.fileId,
                                downloadEntity.filePath,
                                downloadEntity.userName,
                                downloadEntity.url,
                                downloadEntity.previewImageUrl,
                                mutableStateOf(
                                    kotlin.run {
                                        if (downloadEntity.downloadStatus == DownloadStatus.DOWNLOADING.name || downloadEntity.downloadStatus == DownloadStatus.CONNECTING.name) {
                                            if (DownloadManager.isInDownloadList(downloadEntity.fileId))
                                                enumValueOf(downloadEntity.downloadStatus)
                                            else {
                                                DownloadStatus.FAILED
                                            }
                                        } else {
                                            enumValueOf(downloadEntity.downloadStatus)
                                        }
                                    }
                                ),
                                mutableStateOf(0),
                                downloadEntity.createdAt,
                                enumValueOf(downloadEntity.mediaType),
                                downloadEntity.contentLength,
                                downloadEntity.code,
                                downloadEntity.fullName,
                                downloadEntity.profilePictureUrl
                            )
                        }
                    ))
                }

            }
        }
    }

    fun fetchLinkData(url: String) {

        viewModelScope.launch {

            if (url.contains("instagram.com", true)) {
                _downloadUiState.emit(
                    DownloadUiState.ShowCheckUrlDialog
                )

                val result = fetchLinkDataUseCase(url)

                if (result.isSuccess) {
                    _downloadUiState.emit(
                        DownloadUiState.AddToDownload(result.getOrNull() ?: listOf())
                    )
                } else {

                    when (result.exceptionOrNull()) {
                        is Failure.NetworkConnectionError -> _downloadUiState.emit(
                            DownloadUiState.NetworkError
                        )
                        else -> _downloadUiState.emit(
                            DownloadUiState.FetchLinkDataFailed
                        )
                    }
                }

            }


        }
    }

    fun addToDownloads(downloads: List<Download>) {
        viewModelScope.launch {

            if (downloads.isNotEmpty()) {
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
                                it.mediaType.name,
                                it.fullName,
                                it.profilePictureUrl,
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

    fun setIdleState() {
        viewModelScope.launch {
            _downloadUiState.emit(
                DownloadUiState.Idle
            )
        }
    }

    fun onShowDeleteDialog(download: Download) {
        viewModelScope.launch {
            _downloadUiState.emit(
                DownloadUiState.ShowDeleteDialog(download)
            )
        }
    }

    fun deleteDownload(download: Download?, withFile: Boolean = false) {
        viewModelScope.launch {
            if (download != null) {
                deleteDownloadUseCase(download.fileId)

                if (withFile)
                    FileHelper.deleteMediaFile(download.filePath)
            }
        }
    }
}