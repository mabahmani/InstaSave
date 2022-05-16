package com.mabahmani.instasave.data.repository

import com.mabahmani.instasave.data.datasource.RemoteDataSource
import com.mabahmani.instasave.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource): SearchRepository{
}