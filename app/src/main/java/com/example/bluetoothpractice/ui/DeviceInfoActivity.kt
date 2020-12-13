package com.example.bluetoothpractice.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.algorigo.algorigoble.BleDevice
import com.algorigo.algorigoble.BleManager
import com.example.bluetoothpractice.MySampleDevice
import com.example.bluetoothpractice.MySampleDevice2
import com.example.bluetoothpractice.R
import com.example.bluetoothpractice.databinding.ActivityDeviceInfoBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.activity_device_info.*

class DeviceInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeviceInfoBinding
    private lateinit var mySampleDevice: MySampleDevice2
    private var disposable: Disposable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_device_info)

        intent.getStringExtra(KEY_MAC_ADDRESS)?.let {
            mySampleDevice = BleManager.getInstance().getDevice(it) as MySampleDevice2
        }

        //binding.periodEdit.setText(mySampleDevice.getVersion())
        binding.setPeriodBtn.setOnClickListener {
            val value = binding.periodEdit.text.toString()
/*            mySampleDevice.setVersionSingle(value)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    periodEdit.setText(it)
                }, {
                    Log.d("seunghwan", it.toString())
                })*/
        }
        binding.getPeriodBtn.setOnClickListener {
            //binding.periodEdit.setText(mySampleDevice.getVersion())
        }

        binding.sendOffBtn.setOnClickListener {
            disposable?.let {
                it.dispose()
                disposable = null
            }
        }
    }



    sealed class BleData(val type: BleType) {
        data class SC01Data(val keyMacAddress: String) : BleData(BleType.SC01)
        data class OtherData(val keyMacAddress: String) : BleData(BleType.OTHER)
    }

    enum class BleType {
        SC01,
        OTHER
    }

    companion object {
        const val KEY_MAC_ADDRESS = "key_mac_address"
        const val KEY_BLE_TYPE = "ble_type"

    }
}