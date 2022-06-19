package com.mabahmani.instasave.data.datasource

import com.mabahmani.instasave.data.api.response.FeedReelsTray
import com.mabahmani.instasave.data.api.response.Media

interface RemoteDataSource {
    suspend fun getFeedReelsTray() : Result<FeedReelsTray>
    suspend fun getInstagramShortLinkJsonData(url: String) : Result<Media>
    suspend fun getInstagramMediaJsonData(mediaId: String) : Result<Media>
}