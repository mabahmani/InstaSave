package com.mabahmani.instasave.data.repository

import com.mabahmani.instasave.data.datasource.RemoteDataSource
import com.mabahmani.instasave.domain.repository.MediaRepository
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource): MediaRepository {
}