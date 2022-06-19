package com.mabahmani.instasave.domain.interactor

import com.mabahmani.instasave.domain.model.enums.DownloadStatus
import com.mabahmani.instasave.domain.repository.DownloadRepository
import javax.inject.Inject

class UpdateDownloadStatusUseCase @Inject constructor(private val downloadRepository: DownloadRepository) {
    suspend operator fun invoke(fileId: String, status: DownloadStatus) = downloadRepository.updateDownloadStatus(fileId, status)
}