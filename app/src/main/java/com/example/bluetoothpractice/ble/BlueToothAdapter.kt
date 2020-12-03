package com.example.bluetoothpractice.ble

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.algorigo.algorigoble.BleDevice
import com.algorigo.algorigoble.BleManager
import com.example.bluetoothpractice.R
import com.example.bluetoothpractice.databinding.ItemBleBinding

class BlueToothAdapter : RecyclerView.Adapter<BlueToothAdapter.BlueToothViewHolder>() {

    private val items = mutableListOf<BleDevice>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlueToothViewHolder {
        val binding = DataBindingUtil.inflate<ItemBleBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_ble, parent, false
        )
        val holder =
            BlueToothViewHolder(binding)
        return holder
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BlueToothViewHolder, position: Int) {
        holder.bind(items[holder.adapterPosition])
    }

    fun setItems(list: List<BleDevice>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class BlueToothViewHolder(private val binding: ItemBleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BleDevice) {
            binding.tvBle.text = item.name
        }

    }
}