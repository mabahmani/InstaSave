package com.mabahmani.instasave.util

import com.mabahmani.instasave.domain.model.Download
import com.mabahmani.instasave.domain.model.enums.DownloadStatus
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

object DownloadManager {

    private var finishCallback: ((String, DownloadStatus, String, String, Long) -> Unit?)? = null
    private var statusCallback: ((String, DownloadStatus) -> Unit?)? = null
    private var progressCallback: ((String, Int) -> Unit?)? = null

    private val downloadList = mutableListOf<Download>()

    fun startDownload(download: Download) {
        if (!isInDownloadList(download.fileId)) {
            downloadList.add(download)

            download.executer = Executors.newSingleThreadExecutor()

            download.executer!!.execute {

                var input: InputStream? = null
                var output: OutputStream? = null
                var connection: HttpURLConnection? = null
                var file: File? = null

                try {

                    if (statusCallback != null) {
                        try {
                            statusCallback!!.invoke(
                                download.fileId,
                                DownloadStatus.CONNECTING
                            )
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }

                    file = FileHelper.getDownloadOutputFile(
                        download.username,
                        download.fileId,
                        download.mediaType
                    )


                    connection = URL(download.url).openConnection() as HttpURLConnection
                    connection.setRequestProperty("Range", "bytes=" + file.length() + "-")
                    connection.connect()

                    var remainedSize = 0
                    var totalSize = 0

                    if (connection.contentLength < 0) {

                        output = file.outputStream()

                        try {
                            totalSize =
                                connection.getHeaderField("x-full-image-content-length").toInt()
                            remainedSize = totalSize
                        } catch (ex: Exception) {
                        }

                    } else {
                        remainedSize = connection.contentLength
                        totalSize = (file.length() + remainedSize).toInt()

                        output = if (FileHelper.isDownloadFileExists(
                                download.username,
                                download.fileId,
                                download.mediaType
                            )
                        ) {
                            FileOutputStream(
                                file,
                                true
                            )
                        } else {
                            file.outputStream()
                        }
                    }

                    if (statusCallback != null) {
                        try {
                            statusCallback!!.invoke(
                                download.fileId,
                                DownloadStatus.DOWNLOADING
                            )
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }

                    input = connection.inputStream

                    val data = ByteArray(65536)
                    var count: Int
                    var sum = (totalSize - remainedSize).toFloat()

                    download.downloadProgress.value = ((sum / totalSize) * 100).toInt()

                    while (input!!.read(data).also { count = it } != -1) {
                        sum += count
                        download.downloadProgress.value = ((sum / totalSize) * 100).toInt()
                        if (progressCallback != null) {

                            try {
                                progressCallback!!.invoke(
                                    download.fileId,
                                    download.downloadProgress.value
                                )
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }
                        }
                        output.write(data, 0, count)
                    }

                    if (finishCallback != null) {
                        try {
                            finishCallback!!.invoke(
                                download.fileId,
                                DownloadStatus.COMPLETED,
                                file.path.orEmpty(),
                                file.name.orEmpty(),
                                file.length() ?: 0
                            )
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }

                    }

                } catch (ex: Exception) {
                    ex.printStackTrace()
                    if (download.status.value == DownloadStatus.PAUSED) {
                        if (statusCallback != null) {
                            try {
                                statusCallback!!.invoke(
                                    download.fileId,
                                    DownloadStatus.PAUSED
                                )
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }
                        }

                    } else {
                        if (statusCallback != null) {

                            try {
                                statusCallback!!.invoke(
                                    download.fileId,
                                    DownloadStatus.FAILED
                                )
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }
                        }
                    }

                    downloadList.remove(download)

                } finally {
                    input?.close()
                    output?.close()
                    connection?.disconnect()
                    downloadList.remove(download)
                }
            }

            download.executer!!.shutdown()
        }
    }

    fun stopDownload(fileId: String) {
        downloadList.find {
            it.fileId == fileId
        }?.apply {
            this.status.value = DownloadStatus.PAUSED
        }?.executer?.shutdownNow()
    }

    fun isInDownloadList(fileId: String): Boolean {
        return downloadList.find {
            it.fileId == fileId
        } != null
    }

    fun statusCallback(callBack: (String, DownloadStatus) -> Unit) {
        this.statusCallback = callBack
    }

    fun progressCallback(callBack: (String, Int) -> Unit) {
        this.progressCallback = callBack
    }

    fun finishCallback(callBack: (String, DownloadStatus, String, String, Long) -> Unit) {
        this.finishCallback = callBack
    }

}