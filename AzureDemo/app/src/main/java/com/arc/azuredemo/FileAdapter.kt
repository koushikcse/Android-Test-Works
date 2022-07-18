package com.arc.azuredemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arc.azuredemo.databinding.ItemFileNameBinding

/**
 * Created by Koushik on 4/7/22.
 */
class FileAdapter(private val dataList: MutableList<String>, private val callback: Callback) :
    RecyclerView.Adapter<FileAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemFileNameBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(filename: String) {
            binding.tvFileName.text = filename
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_file_name,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
        holder.itemView.setOnClickListener { callback.itemClickListener(dataList[position]) }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}