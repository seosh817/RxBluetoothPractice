package com.algorigo.algorigoble

import android.bluetooth.BluetoothDevice

open class BleScanFilter { // deviceName과 DeviceAddress를 비교해서 Boolean값을 리턴해주는 클래스 (Builder 패턴)
    // question 근데 왜 deviceName과 다르면 true를 주지???

    internal var deviceName: String? = null
    internal var deviceAddress: String? = null

    open fun isOk(device: BluetoothDevice?, rssi: Int, scanRecord: ByteArray?): Boolean {
        return (if (deviceName != null) deviceName.equals(device?.name) else true) &&
                (if (deviceAddress != null) deviceAddress.equals(device?.address) else true)
    }

    class Builder {

        private var deviceName: String? = null
        private var deviceAddress: String? = null

        fun setDeviceName(deviceName: String?): Builder {
            this.deviceName = deviceName
            return this
        }

        fun setDeviceAddress(deviceAddress: String): Builder {
            this.deviceAddress = deviceAddress
            return this
        }

        fun build(): BleScanFilter {
            return BleScanFilter().also {
                it.deviceName = deviceName
                it.deviceAddress = deviceAddress
            }
        }
    }
}