package com.mabahmani.instasave.data.repository

import androidx.compose.runtime.mutableStateOf
import com.mabahmani.instasave.data.datasource.LocalDataSource
import com.mabahmani.instasave.data.datasource.RemoteDataSource
import com.mabahmani.instasave.data.db.entity.DownloadEntity
import com.mabahmani.instasave.domain.model.Download
import com.mabahmani.instasave.domain.model.enums.DownloadStatus
import com.mabahmani.instasave.domain.model.enums.MediaType
import com.mabahmani.instasave.domain.repository.DownloadRepository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class DownloadRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : DownloadRepository {
    override suspend fun addToDownloads(downloadEntities: List<DownloadEntity>) {
        localDataSource.addToDownloads(downloadEntities)
    }

    override suspend fun updateDownload(downloadEntity: DownloadEntity) {
        localDataSource.updateDownload(downloadEntity)
    }

    override suspend fun deleteDownload(downloadEntity: DownloadEntity) {
        localDataSource.deleteDownload(downloadEntity)
    }

    override suspend fun deleteDownload(fileId: String) {
        localDataSource.deleteDownload(fileId)
    }

    override fun getAllDownloads(): Flow<List<DownloadEntity>> {
        return localDataSource.getDownloads()
    }

    override fun getDownload(id: Int): Flow<DownloadEntity> {
        return localDataSource.getDownloadById(id)
    }

    override suspend fun fetchLinkJsonData(url: String): Result<List<Download>> {
        if (url.contains("stories", true)) {
            val start = url.lastIndexOf('/')
            val end = url.indexOf("?")

            val result = remoteDataSource.getInstagramMediaJsonData(url.substring(start + 1, end))

            if (result.isSuccess) {
                val jsonResult = result.getOrNull()
                val list = mutableListOf<Download>()

                if (jsonResult != null && !jsonResult.items.isNullOrEmpty()){

                    val data = jsonResult.items[0]

                    if (data.mediaType == 1){
                        list.add(
                            Download(
                                0,
                                data.id.orEmpty(),
                                "",
                                data.user?.username.orEmpty(),
                                data.getBestImage()?.url.orEmpty(),
                                data.getLowImage()?.url.orEmpty(),
                                mutableStateOf(DownloadStatus.CREATED),
                                mutableStateOf(0),
                                System.currentTimeMillis(),
                                MediaType.IMAGE,
                                0,
                                data.code.orEmpty(),
                                data.user?.fullName.orEmpty(),
                                data.user?.profilePicUrl.orEmpty()
                            )
                        )
                    }

                    else if (data.mediaType == 2){
                        list.add(
                            Download(
                                0,
                                data.id.orEmpty(),
                                "",
                                data.user?.username.orEmpty(),
                                data.getBestVideo()?.url.orEmpty(),
                                data.getLowImage()?.url.orEmpty(),
                                mutableStateOf(DownloadStatus.CREATED),
                                mutableStateOf(0),
                                System.currentTimeMillis(),
                                MediaType.VIDEO,
                                0,
                                data.code.orEmpty(),
                                data.user?.fullName.orEmpty(),
                                data.user?.profilePicUrl.orEmpty()
                            )
                        )
                    }

                    return Result.success(list)
                }

                return Result.success(list)

            } else {
                return Result.failure(result.exceptionOrNull() ?: Exception())
            }
        }

        else{
            var finalUrl = url

            if (url.contains('?')){
                finalUrl = url.substring(0, url.indexOf('?'))
            }

            val result = remoteDataSource.getInstagramShortLinkJsonData(finalUrl)

            Timber.d("fetchLinkJsonData %s", result.getOrNull()?.items?.get(0)?.productType)

            if (result.isSuccess) {

                val jsonResult = result.getOrNull()

                val list = mutableListOf<Download>()

                if (jsonResult != null && !jsonResult.items.isNullOrEmpty()){

                    val data = jsonResult.items[0]

                    if (data.productType == "carousel_container" || data.mediaType == 8) {
                        data.slides?.forEach {

                            if (it.mediaType == 1) {
                                list.add(
                                    Download(
                                        0,
                                        it.id.orEmpty(),
                                        "",
                                        data.user?.username.orEmpty(),
                                        it.getBestImage()?.url.orEmpty(),
                                        it.getLowImage()?.url.orEmpty(),
                                        mutableStateOf(DownloadStatus.CREATED),
                                        mutableStateOf(0),
                                        System.currentTimeMillis(),
                                        MediaType.IMAGE,
                                        0,
                                        data.code.orEmpty(),
                                        data.user?.fullName.orEmpty(),
                                        data.user?.profilePicUrl.orEmpty()
                                    )
                                )
                            }

                            else if (it.mediaType == 2) {
                                list.add(
                                    Download(
                                        0,
                                        it.id.orEmpty(),
                                        "",
                                        data.user?.username.orEmpty(),
                                        it.getBestVideo()?.url.orEmpty(),
                                        it.getLowImage()?.url.orEmpty(),
                                        mutableStateOf(DownloadStatus.CREATED),
                                        mutableStateOf(0),
                                        System.currentTimeMillis(),
                                        MediaType.VIDEO,
                                        0,
                                        data.code.orEmpty(),
                                        data.user?.fullName.orEmpty(),
                                        data.user?.profilePicUrl.orEmpty()
                                    )
                                )
                            }

                        }
                    }

                    else{
                        if (data.mediaType == 1){
                            list.add(
                                Download(
                                    0,
                                    data.id.orEmpty(),
                                    "",
                                    data.user?.username.orEmpty(),
                                    data.getBestImage()?.url.orEmpty(),
                                    data.getLowImage()?.url.orEmpty(),
                                    mutableStateOf(DownloadStatus.CREATED),
                                    mutableStateOf(0),
                                    System.currentTimeMillis(),
                                    MediaType.IMAGE,
                                    0,
                                    data.code.orEmpty(),
                                    data.user?.fullName.orEmpty(),
                                    data.user?.profilePicUrl.orEmpty()
                                )
                            )
                        }

                        else if (data.mediaType == 2){
                            list.add(
                                Download(
                                    0,
                                    data.id.orEmpty(),
                                    "",
                                    data.user?.username.orEmpty(),
                                    data.getBestVideo()?.url.orEmpty(),
                                    data.getLowImage()?.url.orEmpty(),
                                    mutableStateOf(DownloadStatus.CREATED),
                                    mutableStateOf(0),
                                    System.currentTimeMillis(),
                                    MediaType.VIDEO,
                                    0,
                                    data.code.orEmpty(),
                                    data.user?.fullName.orEmpty(),
                                    data.user?.profilePicUrl.orEmpty()
                                )
                            )
                        }
                    }

                    return Result.success(list)
                }

                return Result.success(list)

            } else {
                return Result.failure(result.exceptionOrNull() ?: Exception())
            }
        }

    }

    override suspend fun isExists(code: String): Boolean {
        return localDataSource.isExists(code)
    }

    override suspend fun updateDownloadStatus(fileId: String, status: DownloadStatus) {
        localDataSource.updateDownloadStatus(fileId, status)
    }

    override suspend fun updateDownloadInfo(
        fileId: String,
        downloadStatus: DownloadStatus,
        filePath: String,
        fileName: String,
        fileLength: Long
    ) {
        localDataSource.updateDownloadInfo(fileId, downloadStatus, filePath, fileName, fileLength)
    }
}