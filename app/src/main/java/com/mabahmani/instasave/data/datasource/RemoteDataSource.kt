package com.mabahmani.instasave.data.datasource

import com.mabahmani.instasave.data.api.response.FeedReelsTray
import com.mabahmani.instasave.data.api.response.Media
import com.mabahmani.instasave.data.api.response.SearchTagRes

interface RemoteDataSource {
    suspend fun getFeedReelsTray() : Result<FeedReelsTray>
    suspend fun getInstagramShortLinkJsonData(url: String) : Result<Media>
    suspend fun getInstagramShortLink(url: String) : Result<String>
    suspend fun getInstagramMediaJsonData(mediaId: String) : Result<Media>
    suspend fun searchTag(tag: String,  maxId: String) : Result<SearchTagRes>
}