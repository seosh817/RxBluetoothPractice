package com.example.bluetoothpractice.ble

import android.bluetooth.BluetoothDevice
import com.algorigo.algorigoble.BleDevice
import com.algorigo.algorigoble.BleManager
import com.algorigo.algorigoble.BleScanFilter
import com.algorigo.algorigoble.BleScanSettings

class SC01DeviceDelegate: BleManager.BleDeviceDelegate() {

    override fun createBleDevice(bluetoothDevice: BluetoothDevice): BleDevice? {
        return when(bluetoothDevice.name) {
            SC01Device.BLE_NAME -> SC01Device()
            else -> null
        }
    }

    override fun getBleScanFilters(): Array<BleScanFilter> {
        return arrayOf(BleScanFilter.Builder().setDeviceName(SC01Device.BLE_NAME).build())
    }

    override fun getBleScanSettings(): BleScanSettings {
        return BleScanSettings.Builder().build()
    }
}