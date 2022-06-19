package com.mabahmani.instasave.ui.main.download

import com.mabahmani.instasave.domain.model.Download

sealed class DownloadUiState {
    object Loading : DownloadUiState()
    object EmptyList : DownloadUiState()
    class ShowDownloadsList(val downloads: List<Download>) : DownloadUiState()
    object FetchLinkDataFailed : DownloadUiState()
    class AddToDownload(val downloads: List<Download>) : DownloadUiState()
    object AlreadyDownloaded : DownloadUiState()
    object ShowCheckUrlDialog : DownloadUiState()
}
