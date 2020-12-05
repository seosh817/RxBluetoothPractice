package com.example.bluetoothpractice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.bluetoothpractice.databinding.ActivityNotRxBinding

class NotRxActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotRxBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_not_rx)
    }
}