package com.mabahmani.instasave.data.api

import com.mabahmani.instasave.data.api.response.FeedReelsTray
import io.ktor.client.*
import io.ktor.client.request.*
import javax.inject.Inject

class ApiService @Inject constructor(private val httpClient: HttpClient) {
    suspend fun getFeedReelsTray(): FeedReelsTray = httpClient.get("https://i.instagram.com/api/v1/feed/reels_tray/")
}