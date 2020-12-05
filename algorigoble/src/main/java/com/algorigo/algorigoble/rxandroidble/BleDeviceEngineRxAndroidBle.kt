package com.algorigo.algorigoble.rxandroidble

import android.bluetooth.BluetoothDevice
import android.util.Log
import com.algorigo.algorigoble.BleDevice
import com.algorigo.algorigoble.BleDeviceEngine
import com.polidea.rxandroidble2.RxBleConnection
import com.polidea.rxandroidble2.RxBleDevice
import com.polidea.rxandroidble2.Timeout
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit

class BleDeviceEngineRxAndroidBle: BleDeviceEngine {

    private inner class ConnectCompletable(val autoConnect: Boolean, val milliSec: Long? = null) : Completable() {
        override fun subscribeActual(observer: CompletableObserver?) {
            connectDisposable = (
                    if (milliSec == null)
                        rxBleDevice.establishConnection(autoConnect)
                    else
                        rxBleDevice.establishConnection(autoConnect, Timeout(milliSec, TimeUnit.MILLISECONDS))
                    )
                .doOnDispose {
                    onDisconnected()
                }
                .subscribe({
                    rxBleConnection = it
                    observer?.onComplete()
                }, {
                    Log.e(TAG, "", it)
                    observer?.onError(it)
                    onDisconnected()
                })
        }
    }

    internal lateinit var rxBleDevice: RxBleDevice
    private var connectDisposable: Disposable? = null
    private var rxBleConnection: RxBleConnection? = null

    override val name: String?
        get() = rxBleDevice.name

    override val macAddress: String
        get() = rxBleDevice.macAddress

    override val bluetoothDevice: BluetoothDevice
        get() = rxBleDevice.bluetoothDevice

    override val connectionState: BleDevice.ConnectionState
        get() {
            return when (rxBleDevice.connectionState) {
                RxBleConnection.RxBleConnectionState.CONNECTING -> BleDevice.ConnectionState.CONNECTING
                RxBleConnection.RxBleConnectionState.CONNECTED -> BleDevice.ConnectionState.CONNECTED
                RxBleConnection.RxBleConnectionState.DISCONNECTED -> BleDevice.ConnectionState.DISCONNECTED
                RxBleConnection.RxBleConnectionState.DISCONNECTING -> BleDevice.ConnectionState.DISCONNECTING
                else -> throw IllegalStateException("rxBleDevice's connectionState is ${rxBleDevice.connectionState}")
            }
        }

    override var bleDeviceCallback: BleDeviceEngine.BleDeviceCallback? = null

    override fun connectCompletableImpl(autoConnect: Boolean): Completable {
        return ConnectCompletable(autoConnect)
    }

    override fun connectCompletableImpl(autoConnect: Boolean, milliSec: Long): Completable {
        return ConnectCompletable(autoConnect, milliSec)
    }

    override fun disconnect() {
        connectDisposable?.dispose()
    }

    override fun onReconnected() {

    }

    override fun onDisconnected() {
        connectDisposable = null
        rxBleConnection = null
    }

    override fun getConnectionStateObservable(): Observable<BleDevice.ConnectionState> {
        return rxBleDevice.observeConnectionStateChanges()
            .map {
                when (it) {
                    RxBleConnection.RxBleConnectionState.CONNECTING -> BleDevice.ConnectionState.CONNECTING
                    RxBleConnection.RxBleConnectionState.CONNECTED -> BleDevice.ConnectionState.CONNECTED
                    RxBleConnection.RxBleConnectionState.DISCONNECTED -> BleDevice.ConnectionState.DISCONNECTED
                    RxBleConnection.RxBleConnectionState.DISCONNECTING -> BleDevice.ConnectionState.DISCONNECTING
                }
            }
    }

    override fun readCharacteristic(characteristicUuid: UUID): Single<ByteArray>? {
        return rxBleConnection?.readCharacteristic(characteristicUuid)
    }

    override fun writeCharacteristic(characteristicUuid: UUID, value: ByteArray): Single<ByteArray>? {
        return rxBleConnection?.writeCharacteristic(characteristicUuid, value)
    }

    override fun writeLongValue(characteristicUuid: UUID, value: ByteArray): Observable<ByteArray>? {
        return rxBleConnection?.createNewLongWriteBuilder()
            ?.setCharacteristicUuid(characteristicUuid)
            ?.setBytes(value)
            ?.build()
    }

    override fun setupNotification(characteristicUuid: UUID): Observable<Observable<ByteArray>>? {
        return rxBleConnection?.setupNotification(characteristicUuid)
    }

    override fun setupIndication(characteristicUuid: UUID): Observable<Observable<ByteArray>>? {
        return rxBleConnection?.setupIndication(characteristicUuid)
    }

    companion object {
        private val TAG = BleDeviceEngineRxAndroidBle::class.java.simpleName
    }

}