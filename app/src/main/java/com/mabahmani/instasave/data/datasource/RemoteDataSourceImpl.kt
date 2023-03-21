package com.mabahmani.instasave.data.datasource

import android.content.Context
import com.mabahmani.instasave.data.api.ApiService
import com.mabahmani.instasave.data.api.response.FeedReelsTray
import com.mabahmani.instasave.data.api.response.Media
import com.mabahmani.instasave.data.api.response.SearchTagRes
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

    override suspend fun getInstagramShortLinkJsonData(url: String): Result<Media> {
        return withContext(ioDispatcher) {
            safeApiCall(context) {
                apiService.getInstagramShortLinkJsonData(url)
            }
        }
    }

    override suspend fun getInstagramMediaJsonData(mediaId: String): Result<Media> {
        return withContext(ioDispatcher) {
            safeApiCall(context) {
                apiService.getInstagramMediaJsonData(mediaId)
            }
        }
    }

    override suspend fun searchTag(tag: String, maxId: String): Result<SearchTagRes> {
        return withContext(ioDispatcher) {
            safeApiCall(context) {
                apiService.searchTag(tag, maxId)
            }
        }
    }
}