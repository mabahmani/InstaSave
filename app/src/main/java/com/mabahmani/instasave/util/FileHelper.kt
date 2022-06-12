package com.mabahmani.instasave.util

import android.content.Context
import android.os.Environment
import com.mabahmani.instasave.R
import java.io.File

object FileHelper {
    private fun getDownloadLiveStreamsDirectory(context: Context): File {

        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            context.getString(R.string.app_name) + File.separator + context.getString(R.string.live_stream_path)
        )

        if (file.exists())
            return file

        else
            file.mkdirs()

        return file
    }

    private fun getDownloadLiveStreamsUserDirectory(context: Context, username: String, liveId: String): File {
        val file = File(
            getDownloadLiveStreamsDirectory(context),
            username + "_" + liveId
        )

        if (file.exists())
            return file
        else
            file.mkdirs()

        return file
    }

    fun getDownloadLiveStreamsMPDManifestFile(context: Context, username: String, liveId: String): File {
        return File(getDownloadLiveStreamsUserDirectory(context, username, liveId), "manifest.mpd")
    }

    fun getDownloadLiveStreamsVideoFile(context: Context, username: String, liveId: String): File {
        return File(getDownloadLiveStreamsUserDirectory(context, username, liveId), "video.m4v")
    }

    fun getDownloadLiveStreamsAudioFile(context: Context, username: String, liveId: String): File {
        return File(getDownloadLiveStreamsUserDirectory(context, username, liveId), "audio.m4a")
    }

    fun getDownloadLiveStreamsOutputFile(context: Context, username: String, liveId: String): File {
        val file = File(getDownloadLiveStreamsUserDirectory(context, username, liveId),
            "%s_%s_%s.mp4".format(username,liveId,System.currentTimeMillis())
        )

        file.createNewFile()
        return file
    }
}