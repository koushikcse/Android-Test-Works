package com.mapdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mapdemo.databinding.FragmentAreaListBottomSheetBinding

 open class AreaListBottomSheetFragment(private val listCallback: ListCallback) : BottomSheetDialogFragment() {

    private var binding: FragmentAreaListBottomSheetBinding? = null
     private lateinit var linearLayoutManager: LinearLayoutManager
      lateinit var areaAdapter: AreaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_area_list_bottom_sheet,
            container,
            false
        )
        areaAdapter = AreaAdapter(Utils.dataList,listCallback)

        linearLayoutManager = LinearLayoutManager(context)
        binding?.rvList?.layoutManager = linearLayoutManager
        binding?.rvList?.adapter=areaAdapter
        return  binding?.root
    }
 }