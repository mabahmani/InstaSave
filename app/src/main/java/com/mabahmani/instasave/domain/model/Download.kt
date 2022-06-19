package com.mabahmani.instasave.domain.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.mabahmani.instasave.domain.model.enums.DownloadStatus
import com.mabahmani.instasave.domain.model.enums.MediaType
import java.util.concurrent.ExecutorService

data class Download(
    val dbId: Int,
    val fileId: String,
    val filePath: String = "",
    val username: String,
    val url: String,
    val previewImageUrl: String,
    var status: MutableState<DownloadStatus>  = mutableStateOf(DownloadStatus.CREATED),
    var downloadProgress: MutableState<Int> = mutableStateOf(0),
    var createdAt: Long,
    val mediaType: MediaType,
    val fileLength: Long = 0,
    val code: String,
    var executer: ExecutorService? = null
)
