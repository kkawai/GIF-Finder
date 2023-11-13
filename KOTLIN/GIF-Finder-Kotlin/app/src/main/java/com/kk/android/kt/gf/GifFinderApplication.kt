package com.kk.android.kt.gf

import android.app.Application
import com.kk.android.kt.gf.data.AppContainer
import com.kk.android.kt.gf.data.DefaultAppContainer

class GifFinderApplication : Application() {

    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = DefaultAppContainer()
    }
}