package com.example.bluetoothpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.algorigo.algorigoble.BleManager
import com.algorigo.algorigoble.impl.BleManagerImpl
import com.example.bluetoothpractice.ble.BlueToothAdapter
import com.example.bluetoothpractice.databinding.ActivityMainBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val blueToothAdapter by lazy {
        BlueToothAdapter()
    }
    private val bleManager: BleManager by lazy {
        BleManager.getInstance()
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.activity = this
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.rvBleScan.apply {
            adapter = blueToothAdapter
        }
    }

    fun scanBtn() {
        compositeDisposable.add(bleManager.scanObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                blueToothAdapter.setItems(it)
            }, {

            })
        )
    }
}