package com.example.bluetoothpractice

import com.algorigo.algorigoble.BleDevice

class MySampleDevice2: BleDevice() {

    private var version = ""


    override fun onDisconnected() {
        super.onDisconnected()
        version = ""
    }

    companion object {
        private val TAG = MySampleDevice2::class.java.simpleName

    }
}