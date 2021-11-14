package com.gadget.modulebase.application

import android.app.Application

class GadgetApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ApplicationApi.instance.setApplication(this)
    }
}