package com.kitching.common

import android.app.Application

class KitchingApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }
    companion object {
        lateinit var appInstance: KitchingApplication
        fun getAppContext() : KitchingApplication = appInstance
    }
}