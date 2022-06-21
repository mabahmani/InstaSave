package com.mabahmani.instasave.domain.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class LiveStream(
    val id: Long,
    val preview: String,
    val avatar: String,
    val username: String,
    val elapsedTime: String,
    val playbackUrl: String,
    var downloadState: MutableState<DownloadState> = mutableStateOf(DownloadState.IDLE),
    val videoSegmentsUrl: MutableList<String?> = mutableListOf(),
    val audioSegmentsUrl: MutableList<String?> = mutableListOf(),
    var lastVideoTimeStamp:Long = 0L,
    var lastAudioTimeStamp:Long = 0L,
    var refreshDuplicateCount:Int = 0,
    var videoInitDownloaded:Boolean = false,
    var audioInitDownloaded:Boolean = false,
    var remindSegments: MutableState<Int> = mutableStateOf(0)
){
    enum class DownloadState{
        IDLE,
        DOWNLOADING,
        MERGING,
        COMPLETED
    }
}
