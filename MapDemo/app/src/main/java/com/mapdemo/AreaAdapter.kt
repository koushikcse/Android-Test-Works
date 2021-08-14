package com.mapdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.mapdemo.databinding.ItemListLayoutBinding

class AreaAdapter( private val datalist: ArrayList<Area>,
                   private val dataListener: ListCallback):
    DataBoundListAdapter<Area, ItemListLayoutBinding>(datalist) {

    override fun createBinding(parent: ViewGroup): ItemListLayoutBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_list_layout,
            parent,
            false
        ) as ItemListLayoutBinding
    }

    override fun bind(binding: ItemListLayoutBinding, position: Int) {
        binding.position = position
        binding.item = datalist[position]
        binding.callback = dataListener
    }

}