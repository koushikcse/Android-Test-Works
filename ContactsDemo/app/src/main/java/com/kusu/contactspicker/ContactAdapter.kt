package com.kusu.contactspicker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kusu.contactspicker.databinding.ItemContactBinding

/**
 * Created by Koushik on 2/14/22.
 */
class ContactAdapter(private val datalist: MutableList<Contact>, private val listener: Listener) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int, contact: Contact, listener: Listener) {
            binding.contact = contact
            binding.listener = listener
            binding.pos = position
            Glide.with(binding.root.context)
                .load(contact.photo)
                .placeholder(R.drawable.ic_user_circle)
                .error(R.drawable.ic_user_circle)
                .fallback(R.drawable.ic_user_circle)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivUser);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_contact,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, datalist[position], listener)
    }

    override fun getItemCount(): Int {
        return datalist.size
    }
}