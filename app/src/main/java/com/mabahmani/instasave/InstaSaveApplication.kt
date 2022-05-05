package com.mabahmani.instasave

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.Forest.plant


@HiltAndroidApp
class InstaSaveApplication: Application(){
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG){
            plant(Timber.DebugTree())
        }
    }
}