package com.app.moviereview

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.stetho.Stetho
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initStetho()
        initTimber()
        initFresco()
    }

    private fun initFresco() {
        Fresco.initialize(applicationContext)
    }

    private fun initTimber() {
        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initStetho() {
        Stetho.initializeWithDefaults(this)
    }
}
