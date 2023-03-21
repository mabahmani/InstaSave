package com.mabahmani.instasave.data.repository

import com.mabahmani.instasave.data.api.response.SearchTagRes
import com.mabahmani.instasave.data.datasource.RemoteDataSource
import com.mabahmani.instasave.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource): SearchRepository{
    override suspend fun searchTag(tag: String, maxId: String): Result<SearchTagRes?> {
        val result = remoteDataSource.searchTag(tag, maxId)

        return if (result.isSuccess) {
            Result.success(
                result.getOrNull()
            )
        } else{
            Result.failure(result.exceptionOrNull()?: Exception())
        }
    }
}