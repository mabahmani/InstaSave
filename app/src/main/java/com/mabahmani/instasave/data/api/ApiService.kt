package com.mabahmani.instasave.data.api

import com.mabahmani.instasave.data.api.response.FeedReelsTray
import com.mabahmani.instasave.data.api.response.Media
import io.ktor.client.*
import io.ktor.client.request.*
import javax.inject.Inject

class ApiService @Inject constructor(private val httpClient: HttpClient) {

    suspend fun getFeedReelsTray(): FeedReelsTray = httpClient.get("https://i.instagram.com/api/v1/feed/reels_tray/")
    suspend fun getInstagramShortLinkJsonData(url: String): Media = httpClient.get("%s?__a=1&__d=dis".format(url))
    suspend fun getInstagramMediaJsonData(mediaId: String): Media = httpClient.get("https://i.instagram.com/api/v1/media/%s/info/".format(mediaId))

}