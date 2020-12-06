package com.example.bluetoothpractice.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DeviceInfoActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        const val KEY_MAC_ADDRESS = "key_mac_address"
    }
}