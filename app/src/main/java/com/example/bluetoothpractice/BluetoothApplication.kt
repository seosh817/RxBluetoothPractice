package com.example.bluetoothpractice

import android.app.Application
import com.algorigo.algorigoble.BleDeviceEngine
import com.algorigo.algorigoble.BleManager

class BluetoothApplication: Application() {


    override fun onCreate() {
        super.onCreate()
        BleManager.init(applicationContext, BleManager.BleManagerEngine.ALGORIGO_BLE)

    }
}