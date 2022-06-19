package com.mabahmani.instasave.data.datasource

import com.mabahmani.instasave.data.db.entity.DownloadEntity
import com.mabahmani.instasave.domain.model.enums.DownloadStatus
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun addToDownloads(downloadEntities: List<DownloadEntity>)

    suspend fun updateDownload(downloadEntity: DownloadEntity)

    suspend fun deleteDownload(downloadEntity: DownloadEntity)

    fun getDownloads(): Flow<List<DownloadEntity>>

    fun getDownloadById(id: Int): Flow<DownloadEntity>

    suspend fun isExists(code: String): Boolean

    suspend fun updateDownloadStatus(fileId: String, downloadStatus: DownloadStatus)

    suspend fun updateDownloadInfo(fileId:String, downloadStatus: DownloadStatus, filePath: String, fileName: String, fileLength: Long)
}