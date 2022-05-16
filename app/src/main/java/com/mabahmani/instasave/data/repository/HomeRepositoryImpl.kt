package com.mabahmani.instasave.data.repository

import com.mabahmani.instasave.data.datasource.RemoteDataSource
import com.mabahmani.instasave.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource):
    HomeRepository {
}