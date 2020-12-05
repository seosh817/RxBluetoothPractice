package com.algorigo.algorigoble

import android.bluetooth.BluetoothDevice
import android.content.Context
import com.algorigo.algorigoble.impl.BleDeviceEngineImpl
import com.algorigo.algorigoble.impl.BleManagerImpl
import com.algorigo.algorigoble.rxandroidble.BleDeviceEngineRxAndroidBle
import com.algorigo.algorigoble.rxandroidble.BleManagerRxAndroidBle
import io.reactivex.Observable

abstract class BleManager {

    enum class BleManagerEngine {
        RX_ANDROID_BLE,
        ALGORIGO_BLE,
    }

    //Exception Class
    class BleNotAvailableException(message: String?, cause: Throwable?) : RuntimeException(message, cause) {
        constructor(): this(null, null)
        constructor(message: String?): this(message, null)
        constructor(cause: Throwable?): this(null, cause)
    }

    abstract class BleDeviceDelegate { // Delegate (커스터마이징 가능)
        fun createBleDeviceOuter(bluetoothDevice: BluetoothDevice): BleDevice? {
            return createBleDevice(bluetoothDevice)
        }

        abstract fun createBleDevice(bluetoothDevice: BluetoothDevice): BleDevice?
        abstract fun getBleScanSettings(): BleScanSettings
        abstract fun getBleScanFilters(): Array<BleScanFilter>
    }

    private val defaultBleDeviceDelegate = object : BleDeviceDelegate() {
        override fun createBleDevice(bluetoothDevice: BluetoothDevice): BleDevice {
            return BleDevice()
        }

        override fun getBleScanSettings(): BleScanSettings {
            return BleScanSettings.Builder().build()
        }

        override fun getBleScanFilters(): Array<BleScanFilter> {
            return arrayOf()
        }
    } // 여기까지

    data class ConnectionStateData(val bleDevice: BleDevice, val connectionState: BleDevice.ConnectionState)

    private val deviceMap: MutableMap<BluetoothDevice, BleDevice> = mutableMapOf()
    var bleDeviceDelegate = defaultBleDeviceDelegate

    internal abstract fun initialize(context: Context)
    abstract fun scanObservable(scanDuration: Long, scanSettings: BleScanSettings, vararg scanFilters: BleScanFilter): Observable<List<BleDevice>>
    abstract fun scanObservable(scanSettings: BleScanSettings, vararg scanFilters: BleScanFilter): Observable<List<BleDevice>>
    abstract fun scanObservable(scanDuration: Long): Observable<List<BleDevice>>
    abstract fun scanObservable(): Observable<List<BleDevice>>
    abstract fun getDevice(macAddress: String): BleDevice?
    abstract fun getConnectionStateObservable(): Observable<ConnectionStateData>

    protected fun onDeviceFound(bluetoothDevice: BluetoothDevice): BleDevice? {
        return deviceMap[bluetoothDevice] ?: createBleDevice(bluetoothDevice)
    }

    private fun createBleDevice(bluetoothDevice: BluetoothDevice): BleDevice? {
        return bleDeviceDelegate.createBleDeviceOuter(bluetoothDevice)?.also {
            deviceMap[bluetoothDevice] = it
        }
    }

    fun getConnectedDevices(): List<BleDevice> {
        return deviceMap.values.toList().filter {
            it.connectionState == BleDevice.ConnectionState.CONNECTED
        }
    }

    fun getDevice(bluetoothDevice: BluetoothDevice): BleDevice? {
        return deviceMap[bluetoothDevice]
    }

    companion object {
        private val TAG = BleManager::class.java.simpleName

        private lateinit var bleManager: BleManager
        private lateinit var bleManagerEngine: BleManagerEngine

        fun init(context: Context, engine: BleManagerEngine = BleManagerEngine.ALGORIGO_BLE) {
            bleManagerEngine = engine
            when (engine) {
                BleManagerEngine.RX_ANDROID_BLE -> {
                    bleManager = BleManagerRxAndroidBle().apply { initialize(context.applicationContext) }
                }
                BleManagerEngine.ALGORIGO_BLE -> {
                    bleManager = BleManagerImpl().apply { initialize(context.applicationContext) }
                }
            }
        }

        fun getInstance(): BleManager {
            return bleManager
        }

        fun generateDeviceEngine(): BleDeviceEngine {
            return when (bleManagerEngine) {
                BleManagerEngine.RX_ANDROID_BLE -> {
                    BleDeviceEngineRxAndroidBle()
                }
                BleManagerEngine.ALGORIGO_BLE -> {
                    BleDeviceEngineImpl()
                }
            }
        }
    }
}