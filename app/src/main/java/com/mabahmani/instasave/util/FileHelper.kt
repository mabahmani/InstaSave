package com.mabahmani.instasave.util

import android.os.Environment
import com.mabahmani.instasave.InstaSaveApplication
import com.mabahmani.instasave.R
import com.mabahmani.instasave.domain.model.enums.MediaType
import java.io.File

object FileHelper {
    private fun getDownloadLiveStreamsDirectory(): File {

        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            InstaSaveApplication.appContext.getString(R.string.app_name) + File.separator + InstaSaveApplication.appContext.getString(R.string.live_stream_path)
        )

        if (file.exists())
            return file
        else
            file.mkdirs()

        return file
    }

    private fun getDownloadDirectory(): File {

        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            InstaSaveApplication.appContext.getString(R.string.app_name) + File.separator + InstaSaveApplication.appContext.getString(R.string.media_path)
        )


        if (file.exists())
            return file
        else
            file.mkdirs()

        return file
    }

    private fun getDownloadLiveStreamsUserDirectory(
        username: String,
        liveId: String
    ): File {
        val file = File(
            getDownloadLiveStreamsDirectory(),
            username + "_" + liveId
        )

        if (file.exists())
            return file
        else
            file.mkdirs()

        return file
    }

    private fun getDownloadUserDirectory(
        username: String
    ): File {
        val file = File(
            getDownloadDirectory(),
            username
        )

        if (file.exists())
            return file
        else
            file.mkdirs()

        return file
    }

    fun getDownloadLiveStreamsMPDManifestFile(
        username: String,
        liveId: String
    ): File {
        return File(getDownloadLiveStreamsUserDirectory( username, liveId), "manifest.mpd")
    }

    fun getDownloadLiveStreamsVideoFile(username: String, liveId: String): File {
        return File(getDownloadLiveStreamsUserDirectory(username, liveId), "video.m4v")
    }

    fun getDownloadLiveStreamsAudioFile(username: String, liveId: String): File {
        return File(getDownloadLiveStreamsUserDirectory(username, liveId), "audio.m4a")
    }

    fun getDownloadLiveStreamsOutputFile(username: String, liveId: String): File {
        val file = File(
            getDownloadLiveStreamsUserDirectory(username, liveId),
            "%s_%s_%s.mp4".format(username, liveId, System.currentTimeMillis())
        )

        file.createNewFile()
        return file
    }

    fun getDownloadOutputFile(
        username: String,
        fileId: String,
        mediaType: MediaType
    ): File {
        when (mediaType) {
            MediaType.IMAGE -> {
                val file = File(
                    getDownloadUserDirectory(username),
                    "%s_%s.jpg".format(username, fileId)
                )
                file.createNewFile()
                return file
            }

            MediaType.VIDEO -> {
                val file = File(
                    getDownloadUserDirectory(username),
                    "%s_%s.mp4".format(username, fileId)
                )
                file.createNewFile()
                return file
            }
        }
    }

    fun isDownloadFileExists(
        username: String,
        fileId: String,
        mediaType: MediaType
    ): Boolean {
        when (mediaType) {
            MediaType.IMAGE -> {
                val file = File(
                    getDownloadUserDirectory(username),
                    "%s_%s.jpg".format(username, fileId)
                )
                return file.exists()
            }

            MediaType.VIDEO -> {
                val file = File(
                    getDownloadUserDirectory(username),
                    "%s_%s.mp4".format(username, fileId)
                )
                return file.exists()
            }
        }
    }

    fun deleteMediaFile(
        filePath: String,
    ){
        try {
            File(filePath).delete()
        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }
}