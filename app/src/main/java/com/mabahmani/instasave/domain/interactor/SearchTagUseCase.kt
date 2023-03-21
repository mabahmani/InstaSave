package com.mabahmani.instasave.domain.interactor

import com.mabahmani.instasave.domain.repository.SearchRepository
import javax.inject.Inject

class SearchTagUseCase @Inject constructor(private val searchRepository: SearchRepository) {
    suspend operator fun invoke(tag: String, maxId: String) = searchRepository.searchTag(tag, maxId)
}