package com.mabahmani.instasave.domain.interactor

import com.mabahmani.instasave.data.db.entity.DownloadEntity
import com.mabahmani.instasave.domain.repository.DownloadRepository
import javax.inject.Inject

class AddToDownloadUseCase @Inject constructor(private val downloadRepository: DownloadRepository) {
    suspend operator fun invoke(downloadEntities: List<DownloadEntity>) = downloadRepository.addToDownloads(downloadEntities)
}