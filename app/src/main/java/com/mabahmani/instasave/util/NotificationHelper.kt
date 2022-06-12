package com.mabahmani.instasave.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mabahmani.instasave.R
import com.mabahmani.instasave.service.DownloadLiveStreamsService

object NotificationHelper {

    const val LIVE_STREAM_NOTIFICATION_ID = 1001
    var LIVE_STREAM_COMPLETED_NOTIFICATION_ID = 2001
    const val STOP_FOREGROUND_SERVICE_ACTION = "STOP_FOREGROUND_SERVICE_ACTION"
    const val STOP_RECORDING_LIVE = "STOP_RECORDING_LIVE"

    fun notifyDownloadLiveStreamService(context: Context): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(
                context,
                context.getString(R.string.live_stream_download_notification_channel_id),
                context.getString(R.string.live_stream_download_notification_channel_name),
                context.getString(R.string.live_stream_download_notification_channel_description),
                NotificationManager.IMPORTANCE_LOW
            )
        }

        val notification = NotificationCompat.Builder(
            context,
            context.getString(R.string.live_stream_download_notification_channel_id)
        )
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(context.getString(R.string.downloading_live_streams))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(false)
            .addAction(
                NotificationCompat.Action(
                    R.drawable.ic_stop,
                    context.getString(R.string.stop_all),
                    PendingIntent.getService(
                        context, 0,
                        Intent(context, DownloadLiveStreamsService::class.java).apply {
                            action = STOP_FOREGROUND_SERVICE_ACTION
                        },
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT } else { PendingIntent.FLAG_UPDATE_CURRENT }
                    )
                )
            )
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(LIVE_STREAM_NOTIFICATION_ID, notification)
        }

        return notification
    }

    fun notifyDownloadLiveStreamCompleted(context: Context, message: String): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(
                context,
                context.getString(R.string.live_stream_download_completed_notification_channel_id),
                context.getString(R.string.live_stream_download_completed_notification_channel_name),
                context.getString(R.string.live_stream_download_completed_notification_channel_description),
                NotificationManager.IMPORTANCE_HIGH

            )
        }

        val notification = NotificationCompat.Builder(
            context,
            context.getString(R.string.live_stream_download_notification_channel_id)
        )
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setDefaults(Notification.DEFAULT_SOUND)
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(LIVE_STREAM_COMPLETED_NOTIFICATION_ID ++, notification)
        }

        return notification
    }

    private fun createNotificationChannel(
        context: Context,
        channelId: String,
        channelName: String,
        channelDescription: String,
        importance: Int,
    ) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}