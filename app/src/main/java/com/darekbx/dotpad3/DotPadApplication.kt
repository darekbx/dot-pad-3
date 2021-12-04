package com.darekbx.dotpad3

import android.app.Application
import com.darekbx.dotpad3.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class DotPadApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@DotPadApplication)
            modules(appModule)
        }
    }
}