package com.xbf.blockcanary

import android.app.Application
import android.content.Context
import com.github.moduth.blockcanary.BlockCanary
import com.github.moduth.blockcanary.BlockCanaryContext

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initLeakCanary(this)
    }

    private fun initLeakCanary(context: Context) {
        BlockCanary.install(this, BlockCanaryContext(1000)).start()
    }

}