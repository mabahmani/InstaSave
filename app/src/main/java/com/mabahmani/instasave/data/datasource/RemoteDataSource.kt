package com.mabahmani.instasave.data.datasource

import com.mabahmani.instasave.data.api.response.FeedReelsTray

interface RemoteDataSource {
    suspend fun getFeedReelsTray() : Result<FeedReelsTray>
}