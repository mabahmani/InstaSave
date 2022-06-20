package com.mabahmani.instasave.domain.interactor

import com.mabahmani.instasave.domain.repository.DownloadRepository
import javax.inject.Inject

class DeleteDownloadUseCase @Inject constructor(private val downloadRepository: DownloadRepository) {
    suspend operator fun invoke(fileId: String) = downloadRepository.deleteDownload(fileId)
}