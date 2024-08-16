package com.aquaero.realestatemanager.utils

import android.util.Log

interface AndroidLogger {
    fun w(tag: String, message: String)
}

class Logger: AndroidLogger {
    override fun w(tag: String, message: String) {
        Log.w(tag, message)
    }
}