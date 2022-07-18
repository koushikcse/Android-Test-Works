package com.arc.azuredemo

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.arc.azuredemo.databinding.ActivityMainBinding
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.model.MediaFile
import java.io.File


class MainActivity : AppCompatActivity(), Callback {

    private lateinit var binding: ActivityMainBinding
    private val datalist = ArrayList<String>()
    private var adapter: FileAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        binding.btnSelect.setOnClickListener {
            val intent = Intent(this, FilePickerActivity::class.java)
            startActivityForResult(intent, 100)
        }

        adapter = FileAdapter(datalist,this)
        binding.rvFiles.adapter = adapter


        binding.btnConnect.setOnClickListener {
            val account = AzureUtils.init()
            account?.let { it1 ->
                binding.btnConnect.visibility = View.GONE
                binding.tvConnected.visibility = View.VISIBLE
                binding.btnSelect.visibility = View.VISIBLE
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val files = data?.getParcelableArrayListExtra<MediaFile>(FilePickerActivity.MEDIA_FILES)
//            BlobGettingStartedTask(this, files?.get(0)).execute()
            files?.get(0)?.let {
                AzureUtils.upload(it)
                datalist.add(it.name)
                adapter?.notifyDataSetChanged()
            }
        }
    }

    override fun itemClickListener(fileName: String) {
        val file = File(
            Environment.getExternalStorageDirectory().absolutePath+
                    "AzureDemo",
            fileName
        )
        AzureUtils.download(file)
    }
}