package com.mabahmani.instasave.domain.interactor

import com.mabahmani.instasave.domain.repository.LiveStreamRepository
import javax.inject.Inject

class GetCurrentLiveStreamsUseCase @Inject constructor(private val liveStreamRepository: LiveStreamRepository) {
    suspend operator fun invoke() = liveStreamRepository.getCurrentLiveStreams()
}