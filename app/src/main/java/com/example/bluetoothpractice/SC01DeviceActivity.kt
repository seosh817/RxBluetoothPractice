package com.example.bluetoothpractice

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.algorigo.algorigoble.BleManager
import com.example.bluetoothpractice.ble.SC01Device
import com.example.bluetoothpractice.databinding.ActivitySc01Binding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class SC01DeviceActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySc01Binding
    private lateinit var sc01Device: SC01Device
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sc01)

        intent.getStringExtra(KEY_MAC_ADDRESS)?.let {
            sc01Device = BleManager.getInstance().getDevice(it) as SC01Device
        }

        binding.periodEdit.setText(sc01Device.getDataPeriod().toString())
        binding.setPeriodBtn.setOnClickListener {
            val value = binding.periodEdit.text.toString()
            sc01Device.setDataPeriodSingle(value.toInt())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    binding.periodEdit.setText(it)
                }, {
                    Log.d("seunghwan", it.toString())
                })
        }
        binding.getPeriodBtn.setOnClickListener {
            binding.periodEdit.setText(sc01Device.getDataPeriod().toString())
        }

        binding.etAmp.setText(sc01Device.getAmplification().toString())
        binding.setAmpBtn.setOnClickListener {
            val value = binding.etAmp.text.toString()
            Log.d("seunghwan", binding.etAmp.toString())
            sc01Device.setAmplificationSingle(value.toInt())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    binding.etAmp.setText(it)
                }, {
                    Log.d("seunghwan", it.toString())
                })
        }
        binding.getAmpBtn.setOnClickListener {
            binding.etAmp.setText(sc01Device.getAmplification().toString())
        }


        binding.etSens.setText(sc01Device.getAmplification().toString())
        binding.btnSetSens.setOnClickListener {
            val value = binding.etAmp.text.toString()
            sc01Device.setAmplificationSingle(value.toInt())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    binding.etAmp.setText(it)
                }, {
                    Log.d("seunghwan", it.toString())
                })
        }
        binding.btnGetSens.setOnClickListener {
            binding.etSens.setText(sc01Device.getPotentiometer().toString())
        }




        binding.sendOffBtn.setOnClickListener {
            disposable?.let {
                it.dispose()
                disposable = null
            }
        }
    }


    companion object {
        const val KEY_MAC_ADDRESS = "key_mac_address"
    }
}