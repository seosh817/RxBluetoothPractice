package com.example.bluetoothpractice

import android.bluetooth.BluetoothDevice
import com.algorigo.algorigoble.BleDevice
import com.algorigo.algorigoble.BleManager
import com.algorigo.algorigoble.BleScanFilter
import com.algorigo.algorigoble.BleScanSettings

class MyDeviceDelegate: BleManager.BleDeviceDelegate() {
    override fun createBleDevice(bluetoothDevice: BluetoothDevice): BleDevice? {
        return when(bluetoothDevice.name) {
            MySampleDevice.BLE_NAME -> MySampleDevice()
            else -> null
        }
    }

    override fun getBleScanFilters(): Array<BleScanFilter> {
        return arrayOf(BleScanFilter.Builder().setDeviceName(MySampleDevice.BLE_NAME).build())
    }

    override fun getBleScanSettings(): BleScanSettings {
        return BleScanSettings.Builder().build()
    }
}