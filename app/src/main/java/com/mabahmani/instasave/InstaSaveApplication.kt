package com.mabahmani.instasave

import android.app.Application
import android.content.Context
import com.mabahmani.instasave.util.FileHelper
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.Forest.plant


@HiltAndroidApp
class InstaSaveApplication: Application(){

    companion object {
        lateinit  var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext

        if (BuildConfig.DEBUG){
            plant(Timber.DebugTree())
        }

        FileHelper.scanAppMedias()
    }
}