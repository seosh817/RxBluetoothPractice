package com.example.bluetoothpractice

import android.app.Application
import android.content.Context
import com.algorigo.algorigoble.BleDeviceEngine
import com.algorigo.algorigoble.BleManager

class BluetoothApplication: Application() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        BleManager.init(applicationContext, BleManager.BleManagerEngine.ALGORIGO_BLE)
    }

    companion object {
        private lateinit var instance: BluetoothApplication
        fun getApplicationContext() : Context = instance.applicationContext
    }
}