package com.mabahmani.instasave.di

import com.mabahmani.instasave.data.datasource.LocalDataSource
import com.mabahmani.instasave.data.datasource.LocalDataSourceImpl
import com.mabahmani.instasave.data.datasource.RemoteDataSource
import com.mabahmani.instasave.data.datasource.RemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    abstract fun bindRemoteDataSource(
        remoteDataSourceImpl: RemoteDataSourceImpl
    ): RemoteDataSource

    @Binds
    abstract fun bindLocalDataSource(
        localDataSourceImpl: LocalDataSourceImpl
    ): LocalDataSource
}