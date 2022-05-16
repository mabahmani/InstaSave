package com.mabahmani.instasave.data.repository

import com.mabahmani.instasave.data.datasource.RemoteDataSource
import com.mabahmani.instasave.domain.repository.StoryRepository
import javax.inject.Inject

class StoryRepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource): StoryRepository {
}