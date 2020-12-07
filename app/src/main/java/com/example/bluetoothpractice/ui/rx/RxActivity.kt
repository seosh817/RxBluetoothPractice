package com.example.bluetoothpractice.ui.rx

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.algorigo.algorigoble.BleDevice
import com.algorigo.algorigoble.BleManager
import com.example.bluetoothpractice.*
import com.example.bluetoothpractice.ble.SC01Device
import com.example.bluetoothpractice.ble.SC01DeviceDelegate
import com.example.bluetoothpractice.databinding.ActivityRxBinding
import com.example.bluetoothpractice.ui.BlueToothRecyclerAdapter
import com.example.bluetoothpractice.ui.DeviceInfoActivity
import com.example.bluetoothpractice.ui.notrx.NotRxActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_rx.*

class RxActivity : AppCompatActivity(), BlueToothRecyclerAdapter.OnBlueToothItemClickListener {

    //https://developer.android.com/guide/topics/connectivity/bluetooth
    // InitializableBleDevice는 BleDevice를 상속받은 구현체이다.
    // BleDeviceEngineImpl, BleManagerImpl 구현체
    // BleScanner는 BleManagerImpl에서 사용되며 블루투스 기계들을 scan하기 위해 만들어진 클래스

    // BleDevice는 Device의 고유 객체라고 생각하면 된다.
    // connect할 때 필요한 정보는 deviceName과 deviceAddress인데 (사실 address만 필요)
    // 그 둘의 값을 가지고 있는 클래스이다
    // rssi값은 int형으로 현재 device가 내 안드로이드 폰에 보낸 패킷이 얼마나 걸렸는지 대략적으로 나타내주는 값이다
    // 하지만 신호이기 때문에 다른 요인에 많은 영향을 끼치므로 너무 신뢰하지 않는편이 좋다.
    // 기본적으로 값은 음수 값이며 값이 높을수록 (0에 가까울수록) 가까이 있다는 뜻이다.

    // BleManager는 블루투스의 Scan하는 기능을 담당함.
    // 실제 scan 메소드들은 BleScanner에 있음

    // BluetoothProfile.ServiceListener

    private val compositeDisposable = CompositeDisposable()

    private lateinit var binding: ActivityRxBinding
    private val blueToothAdapter by lazy {
        BlueToothRecyclerAdapter().apply {
            onBlueToothItemClickListener = this@RxActivity
        }
    }

    private lateinit var bleManager: BleManager

    private var disposable: Disposable? = null
    private var connectionStateDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rx)
        binding.activity = this
        initRecyclerView()
        initBlueTooth()
    }

    private fun initBlueTooth() {
        BleManager.init(this)
        bleManager = BleManager.getInstance().apply {
            bleDeviceDelegate = SC01DeviceDelegate()
        }
    }

    private fun initRecyclerView() {
        binding.rvBleScan.apply {
            adapter = blueToothAdapter
        }
    }

    // question disposable 재정의
    fun startScan() {
        if (PermissionUtil.checkPermissions(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
/*            disposable = bleManager.scanObservable(30000)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    disposable = it
                    binding.btnScan.isEnabled = false
                    blueToothAdapter.setItems(listOf())
                    Log.d("seunghwan", it.toString())
                }
                .doOnDispose {
                    binding.btnScan.isEnabled = true
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    blueToothAdapter.setItems(it)
                    Log.d("seunghwan", it.toString())
                }, {
                    binding.btnScan.isEnabled = true
                    val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(intent, 1)
                    Log.d("seunghwan", it.toString())
                }, {
                    binding.btnScan.isEnabled = true
                })*/

            disposable = bleManager.scanObservable(30000)
                .doFinally {
                    disposable = null
                }
                .doOnSubscribe {
                    disposable = it
                    btn_scan.isEnabled = false
                    blueToothAdapter.setItems(listOf())
                }
                .doOnDispose {
                    btn_scan.isEnabled = true
                }
                .subscribe({
                    blueToothAdapter.setItems(it)
                }, {
                    Log.e(TAG, "", it)
                    btn_scan.isEnabled = true
                    if (true) {
                        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                        startActivityForResult(intent, 1)
                    }
                }, {
                    btn_scan.isEnabled = true
                })

/*            bleManager.scanObservable(30000)
                .doOnSubscribe {
                    binding.btnScan.isEnabled = false
                    blueToothAdapter.items = listOf()
                    Log.d("seunghwan", it.toString())
                }
                .doOnDispose {
                    binding.btnScan.isEnabled = true
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    blueToothAdapter.items = it
                    Log.d("seunghwan", it.toString())
                }, {
                    binding.btnScan.isEnabled = true
                    val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(intent, 1)
                    Log.d("seunghwan", it.toString())
                }, {
                    binding.btnScan.isEnabled = true
                }).addTo(compositeDisposable)*/
        } else {
            PermissionUtil.requestExternalPermissions(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                REQUEST_CODE
            )
        }
    }

    fun stopBtn() {
        disposable?.dispose()
    }


    fun navigateToNotRxActivity() {
        val intent = Intent(this, NotRxActivity::class.java)
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            if (PermissionUtil.verifyPermission(grantResults)) {
                startScan()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }


    override fun onSelect(bleDevice: BleDevice) {
        if (bleDevice.connected) {
/*            when (bleDevice) {
                is MySampleDevice2 -> {
                    Intent(this@RxActivity, DeviceInfoActivity::class.java).apply {
                        putExtra(DeviceInfoActivity.KEY_MAC_ADDRESS, bleDevice.macAddress)
                    }.also { startActivity(it) }
                }
                is SC01Device -> {
                    Intent(this@RxActivity, DeviceInfoActivity::class.java).apply {
                        putExtra(DeviceInfoActivity.KEY_MAC_ADDRESS, bleDevice.macAddress)
                    }.also { startActivity(it) }

                }
            }*/
            Intent(this@RxActivity, SC01DeviceActivity::class.java).apply {
                putExtra(DeviceInfoActivity.KEY_MAC_ADDRESS, bleDevice.macAddress)
            }.also { startActivity(it) }
        }
    }

    override fun onButtonClick(bleDevice: BleDevice) {
        when (bleDevice.connectionState) {
            BleDevice.ConnectionState.CONNECTED -> {
                bleDevice.disconnect()
            }
            BleDevice.ConnectionState.DISCONNECTED -> {
                bleDevice.connect(false)
            }
            else -> {

            }
        }
    }

    // question
    override fun onResume() {
        super.onResume()
        connectionStateDisposable = bleManager.getConnectionStateObservable()
            .doFinally {
                connectionStateDisposable = null
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                blueToothAdapter.notifyDataSetChanged()
            }, {
                connectionStateDisposable = null
            })
/*        bleManager.getConnectionStateObservable()
            .doFinally {
                connectionStateDisposable = null
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                blueToothAdapter.notifyDataSetChanged()
            }, {
                connectionStateDisposable = null
            }).addTo(compositeDisposable)*/
    }

    override fun onPause() {
        super.onPause()
        connectionStateDisposable?.dispose()
        //compositeDisposable.dispose()
    }



    companion object {
        private val TAG = RxActivity::class.java.simpleName

        private const val REQUEST_CODE = 1
    }
}