package com.mabahmani.instasave.domain.repository

import com.mabahmani.instasave.data.db.entity.DownloadEntity
import com.mabahmani.instasave.domain.model.Download
import com.mabahmani.instasave.domain.model.enums.DownloadStatus
import kotlinx.coroutines.flow.Flow

interface DownloadRepository {
    suspend fun addToDownloads(downloadEntities: List<DownloadEntity>)

    suspend fun updateDownload(downloadEntity: DownloadEntity)

    suspend fun deleteDownload(downloadEntity: DownloadEntity)

    fun getAllDownloads(): Flow<List<DownloadEntity>>

    fun getDownload(id: Int): Flow<DownloadEntity>

    suspend fun fetchLinkJsonData(url: String): Result<List<Download>>

    suspend fun isExists(code: String): Boolean

    suspend fun updateDownloadStatus(fileId: String, status: DownloadStatus)

    suspend fun updateDownloadInfo(fileId: String, downloadStatus: DownloadStatus, filePath: String, fileName: String, fileLength: Long)
}