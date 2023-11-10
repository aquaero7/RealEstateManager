package com.aquaero.realestatemanager

import android.app.Application
import android.content.Context

class ApplicationRoot: Application() {
    companion object {
        private lateinit var INSTANCE: Application

        fun getContext(): Context = INSTANCE.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}