package com.mabahmani.instasave.data.datasource

import android.content.Context
import com.mabahmani.instasave.data.api.ApiService
import com.mabahmani.instasave.data.api.response.FeedReelsTray
import com.mabahmani.instasave.data.api.safeApiCall
import com.mabahmani.instasave.di.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val apiService: ApiService
) : RemoteDataSource {

    override suspend fun getFeedReelsTray(): Result<FeedReelsTray> {
        return withContext(ioDispatcher) {
            safeApiCall(context) {
                apiService.getFeedReelsTray()
            }
        }
    }
}