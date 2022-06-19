package com.mabahmani.instasave.domain.interactor

import com.mabahmani.instasave.domain.repository.DownloadRepository
import javax.inject.Inject

class DownloadIsExistsUseCase @Inject constructor(private val downloadRepository: DownloadRepository) {
    suspend operator fun invoke(code: String) = downloadRepository.isExists(code)
}