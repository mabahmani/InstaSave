package com.mabahmani.instasave.domain.repository

import com.mabahmani.instasave.domain.model.LiveStream

interface LiveStreamRepository {
    suspend fun getCurrentLiveStreams() : Result<List<LiveStream>>
}