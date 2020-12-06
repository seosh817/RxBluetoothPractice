package com.example.bluetoothpractice.ui

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.bluetoothpractice.R
import com.example.bluetoothpractice.databinding.ActivityNotRxBinding

class NotRxActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotRxBinding

    private lateinit var blueToothAdapter: BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_not_rx)
        initBluetooth()
    }

    private fun initBluetooth() {
        blueToothAdapter = (getSystemService(BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }

    fun scanBtn() {
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

    fun onScan() {
    }

    companion object {
        const val REQUEST_ENABLE_BT = 1
    }
}
