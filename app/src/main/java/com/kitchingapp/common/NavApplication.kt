package com.kitchingapp.common

import android.app.Application

class NavApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }
    companion object {
        lateinit var appInstance: NavApplication
        fun getAppContext() : NavApplication = appInstance
    }
}