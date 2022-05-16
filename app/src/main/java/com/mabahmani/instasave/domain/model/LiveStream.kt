package com.mabahmani.instasave.domain.model

data class LiveStream(
    val preview: String,
    val avatar: String,
    val username: String,
    val elapsedTime: String,
    val playbackUrl: String,
    val isDownloading: Boolean = false
)
