package com.example.bluetoothpractice.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.algorigo.algorigoble.BleDevice
import com.example.bluetoothpractice.R
import com.example.bluetoothpractice.databinding.ItemBleBinding
import kotlinx.android.synthetic.main.item_ble.view.*

class BlueToothRecyclerAdapter : RecyclerView.Adapter<BlueToothRecyclerAdapter.BlueToothViewHolder>() {

    private val items = mutableListOf<BleDevice>()
    var onBlueToothItemClickListener: OnBlueToothItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlueToothViewHolder {
        val binding = DataBindingUtil.inflate<ItemBleBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_ble, parent, false
        )
        val holder = BlueToothViewHolder(binding)
        holder.itemView.setOnClickListener {
            onBlueToothItemClickListener?.onSelect(items[holder.adapterPosition])
        }

        holder.itemView.btn_connect.setOnClickListener {
            onBlueToothItemClickListener?.onButtonClick(items[holder.adapterPosition])
        }
        return holder
    }


    fun setItems(list: List<BleDevice>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BlueToothViewHolder, position: Int) {
        holder.bind(items[holder.adapterPosition])
    }


    interface OnBlueToothItemClickListener {
        fun onSelect(bleDevice: BleDevice)
        fun onButtonClick(bleDevice: BleDevice)
    }

    class BlueToothViewHolder(private val binding: ItemBleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BleDevice) {
            binding.tvBle.text = item.toString()
            when(item.connectionState) {
                BleDevice.ConnectionState.CONNECTING -> {
                    binding.btnConnect.isEnabled = false
                    binding.btnConnect.text = itemView.context.getString(R.string.connecting, item.connectionState.status)
                }
                BleDevice.ConnectionState.CONNECTED -> {
                    binding.btnConnect.isEnabled = true
                    binding.btnConnect.text ="Disconnect"
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