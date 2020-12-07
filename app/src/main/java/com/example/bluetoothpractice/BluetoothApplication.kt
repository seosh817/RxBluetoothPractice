package com.example.bluetoothpractice

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import com.algorigo.algorigoble.BleDeviceEngine
import com.algorigo.algorigoble.BleManager
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import java.io.IOException
import java.net.SocketException

class BluetoothApplication: Application() {

    init {
        instance = this
    }

    @SuppressLint("LongLogTag")
    override fun onCreate() {
        super.onCreate()
        BleManager.init(applicationContext, BleManager.BleManagerEngine.ALGORIGO_BLE)
        RxJavaPlugins.setErrorHandler { e ->
            var error = e
            if (error is UndeliverableException) {
                error = e.cause
            }
            if (error is IOException || error is SocketException) {
                // fine, irrelevant network problem or API that throws on cancellation
                return@setErrorHandler
            }
            if (error is InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                return@setErrorHandler
            }
            if (error is NullPointerException || error is IllegalArgumentException) {
                // that's likely a bug in the application
                Thread.currentThread().uncaughtExceptionHandler
                    .uncaughtException(Thread.currentThread(), error)
                return@setErrorHandler
            }
            if (error is IllegalStateException) {
                // that's a bug in RxJava or in a custom operator
                Thread.currentThread().uncaughtExceptionHandler
                    .uncaughtException(Thread.currentThread(), error)
                return@setErrorHandler
            }
            Log.w("Undeliverable exception received, not sure what to do", error)
        }
    }

    companion object {
        private lateinit var instance: BluetoothApplication
        fun getApplicationContext() : Context = instance.applicationContext
    }
}