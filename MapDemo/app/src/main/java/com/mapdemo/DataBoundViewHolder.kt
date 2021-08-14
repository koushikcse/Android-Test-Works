package com.arc.facilities.compliance.ui.adapters.holders

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class DataBoundViewHolder<T : ViewDataBinding> internal constructor(val binding: T) :
    RecyclerView.ViewHolder(binding.root)