package com.mabahmani.instasave.domain.repository

import com.mabahmani.instasave.data.api.response.SearchTagRes

interface SearchRepository {
    suspend fun searchTag(tag: String, maxId: String): Result<SearchTagRes?>

}