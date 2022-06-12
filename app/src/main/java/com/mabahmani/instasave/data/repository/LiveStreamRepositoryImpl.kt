package com.mabahmani.instasave.data.repository

import android.text.format.DateUtils
import com.mabahmani.instasave.data.datasource.RemoteDataSource
import com.mabahmani.instasave.domain.model.LiveStream
import com.mabahmani.instasave.domain.repository.LiveStreamRepository
import com.mabahmani.instasave.util.orZero
import javax.inject.Inject

class LiveStreamRepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource) :
    LiveStreamRepository {

    override suspend fun getCurrentLiveStreams(): Result<List<LiveStream>> {
        val result = remoteDataSource.getFeedReelsTray()

        if (result.isSuccess) {
            return Result.success(
                result.getOrNull()?.broadcasts?.map {
                    LiveStream(
                        it?.id.orZero(),
                        it?.coverFrameUrl.orEmpty(),
                        it?.broadcastOwner?.profilePicUrl.orEmpty(),
                        it?.broadcastOwner?.username.orEmpty(),
                        DateUtils.getRelativeTimeSpanString(it?.publishedTime.orZero() * 1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString(),
                        it?.dashAbrPlaybackUrl.toString()
                    )
                } ?: listOf()
            )
        }

        else{
            return Result.failure(result.exceptionOrNull()?: Exception())
        }
    }
}