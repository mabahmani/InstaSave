package com.mabahmani.instasave.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import com.arthenica.ffmpegkit.FFmpegKit
import com.mabahmani.instasave.R
import com.mabahmani.instasave.domain.model.LiveStream
import com.mabahmani.instasave.domain.model.MPD
import com.mabahmani.instasave.util.*
import com.mabahmani.instasave.util.LiveStreamMPDXmlParser.parseMPD
import com.mabahmani.instasave.util.NotificationHelper.LIVE_STREAM_NOTIFICATION_ID
import com.mabahmani.instasave.util.NotificationHelper.STOP_FOREGROUND_SERVICE_ACTION
import com.mabahmani.instasave.util.NotificationHelper.STOP_RECORDING_LIVE
import timber.log.Timber
import java.io.*
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.util.concurrent.Executors

class DownloadLiveStreamsService : Service() {

    companion object {
        private val currentDownloadList: MutableList<LiveStream> = mutableListOf()

        fun isDownloading(liveId: Long): Boolean {
            return currentDownloadList.find { it.id == liveId } != null
        }

        var callBack: ((LiveStream) -> Unit)? = null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when (intent?.action) {
            STOP_FOREGROUND_SERVICE_ACTION -> {
                cancelAllDownloads()
            }
            STOP_RECORDING_LIVE -> {
                cancelDownload(intent.getLongExtra(AppConstants.Args.ID, 0))
            }
            else -> {

                startForeground(
                    LIVE_STREAM_NOTIFICATION_ID,
                    NotificationHelper.notifyDownloadLiveStreamService(this)
                )

                val liveId = intent?.getLongExtra(AppConstants.Args.ID, 0) ?: 0L
                val username = intent?.getStringExtra(AppConstants.Args.USERNAME)
                val playbackUrl = intent?.getStringExtra(AppConstants.Args.URL)


                if (currentDownloadList.find { it.id == liveId } == null) {

                    val liveStreamModel =
                        LiveStream(liveId, "", "", username.orEmpty(), "", playbackUrl.orEmpty())

                    currentDownloadList.add(liveStreamModel)

                    createDownloadThread(liveStreamModel)

                    if (callBack != null){
                        try {
                            callBack!!.invoke(liveStreamModel)
                        }catch (ex: Exception){
                            ex.printStackTrace()
                        }
                    }
                }


            }
        }

        return START_STICKY
    }

    private fun cancelDownload(liveId: Long) {
        Timber.d("cancelDownload %s", liveId)
        val foundedItem = currentDownloadList.find { it.id == liveId }?.apply {
            downloadState.value = LiveStream.DownloadState.MERGING
        }

        if (callBack != null){
            try {
                callBack!!.invoke(foundedItem!!)
            }catch (ex: Exception){
                ex.printStackTrace()
            }
        }

        NotificationHelper.notifyDownloadLiveStreamFinishing(
            this,
            getString(R.string.live_stream_download_finishing_format).format(
                foundedItem?.videoSegmentsUrl?.size.toString(),
                foundedItem?.username.orEmpty()
            ),
            foundedItem?.id.orZero()
        )
    }

    private fun createDownloadThread(model: LiveStream) {

        model.downloadState.value = LiveStream.DownloadState.DOWNLOADING

        val executor = Executors.newSingleThreadExecutor()

        executor.execute {

            val mainHandler = Handler(Looper.getMainLooper())

            mainHandler.post(object : Runnable {
                override fun run() {
                    if (model.refreshDuplicateCount < 30 && model.downloadState.value == LiveStream.DownloadState.DOWNLOADING) {
                        downloadMPD(model)
                        mainHandler.postDelayed(this, 2000)
                    } else {
                        model.downloadState.value = LiveStream.DownloadState.MERGING
                    }
                }
            })
        }

        executor.shutdown()

    }

    private fun downloadMPD(
        model: LiveStream
    ) {

        val executor = Executors.newSingleThreadExecutor()

        executor.execute {

            try {

                download(
                    URL(model.playbackUrl),
                    FileHelper.getDownloadLiveStreamsMPDManifestFile(
                        model.username,
                        model.id.toString()
                    ).outputStream()
                )

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {

                    val parsedData = parseMPD(
                        FileHelper.getDownloadLiveStreamsMPDManifestFile(
                            model.username,
                            model.id.toString()
                        ).inputStream()
                    )

                    extractVideoSegments(parsedData, model)

                    extractAudioSegments(parsedData, model)

                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }

        executor.shutdown()
    }

    private fun extractAudioSegments(parsedData: MPD, model: LiveStream) {

        if (parsedData.periods[0].adaptationSet.last().representation[0].segmentTemplate[0].segmentTimeline[0].s[0].t!!.toLong() > model.lastAudioTimeStamp) {
            model.refreshDuplicateCount = 0
            model.audioSegmentsUrl.addAll(parsedData.periods[0].adaptationSet.last().representation[0].segmentTemplate[0].segmentTimeline[0].s.map {
                val mediaUrl = resolveMediaUrl(
                    model.playbackUrl,
                    parsedData.periods[0].adaptationSet.last().representation[0].segmentTemplate[0].media,
                    it.t
                )

                model.lastAudioTimeStamp = it.t!!.toLong()

                return@map mediaUrl
            })
        } else {
            model.refreshDuplicateCount++
        }


        if (!model.audioInitDownloaded) {

            model.audioInitDownloaded = true

            val initAudioUrl = resolveMediaInitUrl(
                model.playbackUrl,
                parsedData.periods[0].adaptationSet.last().representation[0].segmentTemplate[0].initialization
            )

            model.audioSegmentsUrl.add(
                0,
                initAudioUrl
            )

            downloadAudioSegments(model)

        }
    }

    private fun extractVideoSegments(parsedData: MPD, model: LiveStream) {
        if (parsedData.periods[0].adaptationSet[0].representation[0].segmentTemplate[0].segmentTimeline[0].s[0].t!!.toLong() > model.lastVideoTimeStamp) {
            model.refreshDuplicateCount = 0
            model.videoSegmentsUrl.addAll(
                parsedData.periods[0].adaptationSet[0].representation[0].segmentTemplate[0].segmentTimeline[0].s.map {

                    val mediaUrl = resolveMediaUrl(
                        model.playbackUrl,
                        parsedData.periods[0].adaptationSet[0].representation[0].segmentTemplate[0].media,
                        it.t
                    )

                    model.lastVideoTimeStamp = it.t!!.toLong()

                    return@map mediaUrl
                }
            )
        } else {
            model.refreshDuplicateCount++
        }

        if (!model.videoInitDownloaded) {
            model.videoInitDownloaded = true

            val initVideoUrl = resolveMediaInitUrl(
                model.playbackUrl,
                parsedData.periods[0].adaptationSet[0].representation[0].segmentTemplate[0].initialization
            )

            model.videoSegmentsUrl.add(
                0,
                initVideoUrl
            )

            downloadVideoSegments(model)

        }
    }

    private fun resolveMediaUrl(playbackUrl: String, media: String?, t: String?): String {
        var mediaUrl = media?.replace(
            "../",
            URI(playbackUrl).resolve("..").toURL().toString()
        )
        mediaUrl = mediaUrl?.replace("\$Time$", t.toString())

        return mediaUrl.orEmpty()
    }

    private fun resolveMediaInitUrl(playbackUrl: String, init: String?): String {
        val initVideoUrl = init?.replace(
            "../",
            URI(playbackUrl).resolve("..").toURL().toString()
        )

        return initVideoUrl.orEmpty()
    }

    private fun downloadAudioSegments(model: LiveStream) {

        val file =
            FileHelper.getDownloadLiveStreamsAudioFile(model.username, model.id.toString())

        val executor = Executors.newSingleThreadExecutor()

        executor.execute {

            while (model.downloadState.value == LiveStream.DownloadState.DOWNLOADING || model.audioSegmentsUrl.isNotEmpty()) {

                if (model.audioSegmentsUrl.isNotEmpty()) {

                    val it = model.audioSegmentsUrl[0]

                    if (it?.isNotEmpty() == true) {
                        try {
                            download(URL(it), FileOutputStream(file, true))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        } finally {
                            model.audioSegmentsUrl.removeAt(0)
                        }
                    }

                }
            }

        }

        executor.shutdown()
    }

    private fun downloadVideoSegments(model: LiveStream) {

        val file =
            FileHelper.getDownloadLiveStreamsVideoFile(model.username, model.id.toString())

        val executor = Executors.newSingleThreadExecutor()

        executor.execute {

            while (model.downloadState.value == LiveStream.DownloadState.DOWNLOADING || model.videoSegmentsUrl.isNotEmpty()) {

                if (model.downloadState.value == LiveStream.DownloadState.MERGING){

                    if (callBack != null){
                        try {
                            callBack!!.invoke(model)
                        }catch (ex: Exception){
                            ex.printStackTrace()
                        }
                    }

                    NotificationHelper.notifyDownloadLiveStreamFinishing(
                        this,
                        getString(R.string.live_stream_download_finishing_format).format(
                            model.videoSegmentsUrl.size.toString(),
                            model.username
                        ),
                        model.id
                    )
                }
                if (model.videoSegmentsUrl.isNotEmpty()) {

                    val it = model.videoSegmentsUrl[0]

                    if (it?.isNotEmpty() == true) {
                        try {
                            download(URL(it), FileOutputStream(file, true))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        } finally {
                            model.videoSegmentsUrl.removeAt(0)
                        }
                    }

                }

            }

            mergeVideoAndAudio(model)
        }

        executor.shutdown()
    }

    private fun mergeVideoAndAudio(model: LiveStream) {
        val videoFile =
            FileHelper.getDownloadLiveStreamsVideoFile(model.username, model.id.toString())
        val audioFile =
            FileHelper.getDownloadLiveStreamsAudioFile(model.username, model.id.toString())
        val outputFile =
            FileHelper.getDownloadLiveStreamsOutputFile(model.username, model.id.toString())

        FFmpegKit.executeAsync(
            "-i ${audioFile.path} -i ${videoFile.path} -y -c copy ${outputFile.path}",
            {
                videoFile.delete()
                audioFile.delete()
                completeDownload(model)
            },
            { Timber.d("FFmpegKit %s", it) },
            { Timber.d("FFmpegKit %s", it) }
        )
    }

    private fun completeDownload(model: LiveStream) {

        model.downloadState.value = LiveStream.DownloadState.COMPLETED

        if (callBack != null){
            try {
                callBack!!.invoke(model)
            }catch (ex: Exception){
                ex.printStackTrace()
            }
        }


        NotificationHelper.notifyDownloadLiveStreamCompleted(
            this,
            getString(R.string.live_stream_download_completed_format).format(model.username),
            model.id
        )
        currentDownloadList.remove(model)

        checkDownloadList()

    }

    private fun checkDownloadList() {
        if (currentDownloadList.isEmpty()) {
            cancelAllDownloads()
        }
    }

    private fun download(
        url: URL,
        output: OutputStream
    ) {

        var input: InputStream? = null
        var connection: HttpURLConnection? = null

        try {

            connection = url.openConnection() as HttpURLConnection
            connection.connect()

            input = connection.inputStream

            val data = ByteArray(4096)
            var count: Int
            while (input!!.read(data).also { count = it } != -1) {
                output.write(data, 0, count)
            }

        } catch (ex: Exception) {

        } finally {
            input?.close()
            output.close()
            connection?.disconnect()
        }

    }


    private fun cancelAllDownloads() {
        currentDownloadList.forEach {
            it.downloadState.value = LiveStream.DownloadState.MERGING

            if (callBack != null){
                try {
                    callBack!!.invoke(it)
                }catch (ex: Exception){
                    ex.printStackTrace()
                }
            }

            NotificationHelper.notifyDownloadLiveStreamFinishing(
                this,
                getString(R.string.live_stream_download_finishing_format).format(
                    it.videoSegmentsUrl.size.toString(),
                    it.username
                ),
                it.id
            )
        }

        stopForeground(true)
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()

        cancelAllDownloads()
    }
}