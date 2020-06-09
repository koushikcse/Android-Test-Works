package com.example.myapplication.routeServer

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.myapplication.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_server.*

class ServerActivity : AppCompatActivity() {
    private var broadcastReceiverNetworkState: BroadcastReceiver? = null
    private var host: String? = null
    private var isStarted = false
    private val port = 8080

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server)

        initBroadcastReceiverNetworkStateChanged()
//        ApiHandler.init(this)

        val filter = IntentFilter()
        filter.addAction("START_WEB_SERVICE")
        filter.addAction("STOP_WEB_SERVICE")
        LocalBroadcastManager.getInstance(this).registerReceiver(apiReceiver, filter)

        server_on_off_btn.setOnClickListener {
            if (isConnectedInWifi()) {
                if (!isStarted) {
                    startServer()
                } else {
                    stopServer()
                }
            } else {
                Snackbar.make(
                    server_layout,
                    getString(R.string.wifi_message),
                    Snackbar.LENGTH_LONG
                ).show();
            }
        }
    }

    private fun startService() {
        if (!isMyServiceRunning(MyService::class.java)) {
            val intent = Intent(this, MyService::class.java)
            intent.putExtra("port", port)
            startService(intent)
        }
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager =
            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    private fun setIpAccess() {
        host = getIpAccess()
    }

    private fun initBroadcastReceiverNetworkStateChanged() {
        val filters = IntentFilter()
        filters.addAction("android.net.wifi.WIFI_STATE_CHANGED")
        filters.addAction("android.net.wifi.STATE_CHANGE")
        broadcastReceiverNetworkState = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                setIpAccess()
            }
        }
        super.registerReceiver(broadcastReceiverNetworkState, filters)
    }

    private fun getIpAccess(): String? {
        val wifiManager =
            applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val ipAddress = wifiManager.connectionInfo.ipAddress
        val formatedIpAddress = String.format(
            "%d.%d.%d.%d",
            ipAddress and 0xff,
            ipAddress shr 8 and 0xff,
            ipAddress shr 16 and 0xff,
            ipAddress shr 24 and 0xff
        )
        return "http://$formatedIpAddress:"
    }

    private fun isConnectedInWifi(): Boolean {
        val wifiManager =
            applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val networkInfo =
            (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        return (networkInfo != null && networkInfo.isAvailable && networkInfo.isConnected
                && wifiManager.isWifiEnabled && networkInfo.typeName == "WIFI")
    }

    private fun startServer() {
        isStarted = true
        startService()
        server_on_off_btn.isChecked = true
        server_url_txt.text = "$host$port"
    }

    private fun stopServer() {
        isStarted = false
        ApiHandler.isWevServerStarted = false
        stopService(Intent(this, MyService::class.java))
        server_on_off_btn.isChecked = false
        server_url_txt.text = "NONE"
    }


    private val apiReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "START_WEB_SERVICE") {
                Toast.makeText(context, "START_WEB_SERVICE", Toast.LENGTH_LONG).show()
            } else if (intent.action == "STOP_WEB_SERVICE") {
                stopServer()
            }
        }
    }

}