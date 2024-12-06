package com.mabahmani.instasave.data.datasource

import android.content.Context
import com.mabahmani.instasave.data.api.ApiService
import com.mabahmani.instasave.data.api.response.FeedReelsTray
import com.mabahmani.instasave.data.api.response.Media
import com.mabahmani.instasave.data.api.response.SearchTagRes
import com.mabahmani.instasave.data.api.safeApiCall
import com.mabahmani.instasave.di.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.http.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
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

    override suspend fun getInstagramShortLink(url: String): Result<String> {
        return withContext(ioDispatcher) {
            var c: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
            c.requestMethod = "GET"
            c.instanceFollowRedirects = false
            c.setRequestProperty(HttpHeaders.UserAgent, "Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1")
            c.connect()
            if (c.responseCode == HttpURLConnection.HTTP_MOVED_PERM || c.responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                val newURL = c.getHeaderField("Location")
                c = URL(newURL).openConnection() as HttpURLConnection
                c.responseMessage
                Result.success(c.url.toString())
            }
            else {
                Result.success("")
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