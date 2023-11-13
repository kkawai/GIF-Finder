package com.kk.android.fuzzy_waddle

import android.app.Application
import com.kk.android.fuzzy_waddle.data.AppContainer
import com.kk.android.fuzzy_waddle.data.DefaultAppContainer

class GifFinderApplication : Application() {

    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = DefaultAppContainer()
    }
}