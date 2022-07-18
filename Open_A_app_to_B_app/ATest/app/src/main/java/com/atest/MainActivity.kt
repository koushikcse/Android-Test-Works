package com.atest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.atest.databinding.ActivityMainBinding
import android.widget.Toast

import android.content.Intent
import android.content.ComponentName

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.done.setOnClickListener {
            val intent = Intent()
            intent.putExtra("ATEST","Koushik Mondal")
            intent.component = ComponentName("com.btest", "com.btest.MainActivity")
            startActivity(intent)
            }
        }

}