package com.example.bluetoothpractice.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.algorigo.algorigoble.BleDevice
import com.example.bluetoothpractice.MySampleDevice2
import com.example.bluetoothpractice.R
import com.example.bluetoothpractice.ble.SC01Device
import com.example.bluetoothpractice.databinding.ItemOtherBinding
import com.example.bluetoothpractice.databinding.ItemSc01Binding
import kotlinx.android.synthetic.main.item_sc01.view.*

class BlueToothRecyclerAdapter : RecyclerView.Adapter<BlueToothRecyclerAdapter.BlueToothViewHolder>() {

    private val items = mutableListOf<BleDevice>()
    var onBlueToothItemClickListener: OnBlueToothItemClickListener? = null

    enum class ViewType {
        SC01,
        Other
    }

    fun setItems(list: List<BleDevice>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlueToothViewHolder {
        return when (ViewType.values()[viewType]) {
            ViewType.SC01 -> {
                val binding = DataBindingUtil.inflate<ItemSc01Binding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_sc01, parent, false
                )
                SC01BluetoothViewHolder(binding).apply {
                    itemView.setOnClickListener {
                        onBlueToothItemClickListener?.onSelect(items[this.adapterPosition])
                    }
                    itemView.btn_connect.setOnClickListener {
                        onBlueToothItemClickListener?.onButtonClick(items[this.adapterPosition])
                    }
                }
            }
            ViewType.Other -> {
                val binding = DataBindingUtil.inflate<ItemOtherBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_other, parent, false
                )
                OtherBluetoothViewHolder(binding).apply {
                    itemView.setOnClickListener {
                        onBlueToothItemClickListener?.onSelect(items[this.adapterPosition])
                    }
                    itemView.btn_connect.setOnClickListener {
                        onBlueToothItemClickListener?.onButtonClick(items[this.adapterPosition])
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(items[position]) {
            is SC01Device -> {
                0
            }
            else -> {
                1
            }
        }
    }


    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BlueToothViewHolder, position: Int) {
        holder.bind(items[holder.adapterPosition])
    }


    interface OnBlueToothItemClickListener {
        fun onSelect(bleDevice: BleDevice)
        fun onButtonClick(bleDevice: BleDevice)
    }



    abstract class BlueToothViewHolder(binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item: BleDevice)
    }

    inner class SC01BluetoothViewHolder(val binding: ItemSc01Binding) : BlueToothViewHolder(binding) {
        override fun bind(item: BleDevice) {
            binding.tvBle.text = item.toString()
            when (item.connectionState) {
                BleDevice.ConnectionState.CONNECTING -> {
                    binding.btnConnect.isEnabled = false
                    binding.btnConnect.text =
                        itemView.context.getString(R.string.connecting, item.connectionState.status)
                }
                BleDevice.ConnectionState.CONNECTED -> {
                    binding.btnConnect.isEnabled = true
                    binding.btnConnect.text = "Disconnect"
                }
                BleDevice.ConnectionState.DISCONNECTING -> {
                    binding.btnConnect.isEnabled = false
                    binding.btnConnect.text = "Disconnecting..."
                }
                BleDevice.ConnectionState.DISCONNECTED -> {
                    binding.btnConnect.isEnabled = true
                    binding.btnConnect.text = "Connect"
                }
            }
        }
    }

    inner class OtherBluetoothViewHolder(val binding: ItemOtherBinding) : BlueToothViewHolder(binding) {
        override fun bind(item: BleDevice) {
            binding.tvBle.text = item.toString()
            when (item.connectionState) {
                BleDevice.ConnectionState.CONNECTING -> {
                    binding.btnConnect.isEnabled = false
                    binding.btnConnect.text =
                        itemView.context.getString(R.string.connecting, item.connectionState.status)
                }
                BleDevice.ConnectionState.CONNECTED -> {
                    binding.btnConnect.isEnabled = true
                    binding.btnConnect.text = "Disconnect"
                }
                BleDevice.ConnectionState.DISCONNECTING -> {
                    binding.btnConnect.isEnabled = false
                    binding.btnConnect.text = "Disconnecting..."
                }
                BleDevice.ConnectionState.DISCONNECTED -> {
                    binding.btnConnect.isEnabled = true
                    binding.btnConnect.text = "Connect"
                }
            }
        }
    }
}