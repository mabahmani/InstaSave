package com.mabahmani.instasave.domain.interactor

import com.mabahmani.instasave.domain.repository.DownloadRepository
import javax.inject.Inject

class FetchLinkDataUseCase @Inject constructor(private val downloadRepository: DownloadRepository) {
    suspend operator fun invoke(url: String) = downloadRepository.fetchLinkJsonData(url)
}