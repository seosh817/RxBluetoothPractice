package com.example.bluetoothpractice

import android.bluetooth.BluetoothDevice
import com.algorigo.algorigoble.BleDevice
import com.algorigo.algorigoble.BleManager
import com.algorigo.algorigoble.BleScanFilter
import com.algorigo.algorigoble.BleScanSettings
import com.example.bluetoothpractice.ble.SC01Device

class MyDeviceDelegate : BleManager.BleDeviceDelegate() {
    override fun createBleDevice(bluetoothDevice: BluetoothDevice): BleDevice? {
        return when (bluetoothDevice.name) {
            SC01Device.BLE_NAME -> SC01Device()
            else -> MySampleDevice2()
        }
    }

    override fun getBleScanSettings(): BleScanSettings {
        return BleScanSettings.Builder().build()
    }

    override fun getBleScanFilters(): Array<BleScanFilter> {
        //return arrayOf(BleScanFilter.Builder().setDeviceName(MySampleDevice.BLE_NAME).build())
        return arrayOf()
    }

}