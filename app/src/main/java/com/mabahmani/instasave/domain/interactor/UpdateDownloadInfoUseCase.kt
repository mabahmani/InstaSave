package com.mabahmani.instasave.domain.interactor

import com.mabahmani.instasave.domain.model.enums.DownloadStatus
import com.mabahmani.instasave.domain.repository.DownloadRepository
import javax.inject.Inject

class UpdateDownloadInfoUseCase @Inject constructor(private val downloadRepository: DownloadRepository) {
    suspend operator fun invoke(
        fileId: String,
        downloadStatus: DownloadStatus,
        filePath: String,
        fileName: String,
        fileLength: Long
    ) = downloadRepository.updateDownloadInfo(fileId, downloadStatus, filePath, fileName, fileLength)
}