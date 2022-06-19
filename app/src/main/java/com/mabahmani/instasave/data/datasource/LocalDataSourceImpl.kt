package com.mabahmani.instasave.data.datasource

import com.mabahmani.instasave.data.db.dao.DownloadDao
import com.mabahmani.instasave.data.db.entity.DownloadEntity
import com.mabahmani.instasave.di.IoDispatcher
import com.mabahmani.instasave.domain.model.enums.DownloadStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val downloadDao: DownloadDao
) : LocalDataSource {

    override suspend fun addToDownloads(downloadEntities: List<DownloadEntity>) {
        withContext(ioDispatcher) {
            downloadDao.insertAll(downloadEntities)
        }
    }

    override suspend fun updateDownload(downloadEntity: DownloadEntity) {
        withContext(ioDispatcher) {
            downloadDao.update(downloadEntity)
        }
    }

    override suspend fun deleteDownload(downloadEntity: DownloadEntity) {
        withContext(ioDispatcher) {
            downloadDao.delete(downloadEntity)
        }
    }

    override fun getDownloads(): Flow<List<DownloadEntity>> {
        return downloadDao.getAll()
    }

    override fun getDownloadById(id: Int): Flow<DownloadEntity> {
        return downloadDao.findById(id)
    }

    override suspend fun isExists(code: String): Boolean {
        return withContext(ioDispatcher) {
            downloadDao.isExist(code)
        }
    }

    override suspend fun updateDownloadStatus(fileId: String, downloadStatus: DownloadStatus) {
        return withContext(ioDispatcher){
            downloadDao.updateDownloadStatus(fileId, downloadStatus.name)
        }
    }

    override suspend fun updateDownloadInfo(
        fileId: String,
        downloadStatus: DownloadStatus,
        filePath: String,
        fileName: String,
        fileLength: Long
    ) {
        return withContext(ioDispatcher){
            downloadDao.updateDownloadInfo(fileId, downloadStatus.name, filePath, fileName, fileLength)
        }
    }
}