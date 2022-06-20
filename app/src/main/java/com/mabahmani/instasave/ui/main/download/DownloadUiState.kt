package com.mabahmani.instasave.ui.main.download

import com.mabahmani.instasave.domain.model.Download

sealed class DownloadUiState {
    object Loading : DownloadUiState()
    object EmptyList : DownloadUiState()
    object FetchLinkDataFailed : DownloadUiState()
    object AlreadyDownloaded : DownloadUiState()
    object ShowCheckUrlDialog : DownloadUiState()
    object Idle : DownloadUiState()
    class AddToDownload(val downloads: List<Download>) : DownloadUiState()
    class ShowDeleteDialog(val download: Download) : DownloadUiState()
    class ShowDownloadsList(val downloads: List<Download>) : DownloadUiState()
}
