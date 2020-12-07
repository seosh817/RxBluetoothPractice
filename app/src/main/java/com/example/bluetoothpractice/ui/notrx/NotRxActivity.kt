package com.example.bluetoothpractice.ui.notrx

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanFilter
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.bluetoothpractice.R
import com.example.bluetoothpractice.databinding.ActivityNotRxBinding
import com.example.bluetoothpractice.ui.BlueToothRecyclerAdapter

class NotRxActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotRxBinding
    private lateinit var blueToothAdapter: BluetoothAdapter

    private var bleGatt: BluetoothGatt? = null

    private val filters: MutableList<ScanFilter> = ArrayList()


    private val blueToothRecyclerAdapter: BlueToothRecyclerAdapter by lazy {
        BlueToothRecyclerAdapter()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_not_rx)
        initBluetooth()
    }

    private fun initBluetooth() {
        blueToothAdapter = (getSystemService(BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }

    fun searchBtn() {
        checkBlueToothEnableAndScan(scan = { onScan() })
    }

    private fun checkBlueToothEnableAndScan(scan: () -> Unit) {
        if (!blueToothAdapter.isEnabled) {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT)
        } else {
            scan()
        }
    }

    private fun onScan() {
        if(blueToothAdapter.isDiscovering) {
            blueToothAdapter.cancelDiscovery()
        } else {
            if(blueToothAdapter.isEnabled) {
                blueToothAdapter.startDiscovery()
            }
        }
    }

    private fun setFilters() {

    }

    companion object {
        const val REQUEST_ENABLE_BT = 1
    }
}
