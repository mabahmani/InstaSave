package com.mabahmani.instasave.domain.interactor

import com.mabahmani.instasave.domain.repository.DownloadRepository
import javax.inject.Inject

class GetAllDownloadsUseCase @Inject constructor(private val downloadRepository: DownloadRepository) {
    operator fun invoke() = downloadRepository.getAllDownloads()
}