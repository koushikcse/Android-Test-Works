package com.example.myapplication

import android.content.*
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    // INSTANCE OF ANDROID WEB SERVER
    private var androidWebServer: AndroidWebServer? = null
    private var broadcastReceiverNetworkState: BroadcastReceiver? = null
    private var isStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        floatingActionButtonOnOff.setOnClickListener {
            if (isConnectedInWifi()) {
                if (!isStarted && startAndroidWebServer()) {
                    isStarted = true;
                    textViewMessage.setVisibility(View.VISIBLE);
                    floatingActionButtonOnOff.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorGreen));
                    editTextPort.setEnabled(false);
                } else if (stopAndroidWebServer()) {
                    isStarted = false;
                    textViewMessage.setVisibility(View.INVISIBLE);
                    floatingActionButtonOnOff.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorRed));
                    editTextPort.setEnabled(true);
                }
            } else {
                Snackbar.make(coordinatorLayout, getString(R.string.wifi_message), Snackbar.LENGTH_LONG).show();
            }
        }
        // INIT BROADCAST RECEIVER TO LISTEN NETWORK STATE CHANGED
        initBroadcastReceiverNetworkStateChanged();
    }

    //region Start And Stop AndroidWebServer
    private fun startAndroidWebServer(): Boolean {
        if (!isStarted) {
            val port: Int = getPortFromEditText()
            try {
                if (port == 0) {
                    throw Exception()
                }
                androidWebServer = AndroidWebServer(port)
                androidWebServer!!.start()
                return true
            } catch (e: Exception) {
                e.printStackTrace()
                Snackbar.make(
                    coordinatorLayout,
                    "The PORT $port doesn't work, please change it between 1000 and 9999.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
        return false
    }

    private fun stopAndroidWebServer(): Boolean {
        if (isStarted && androidWebServer != null) {
            androidWebServer!!.stop()
            return true
        }
        return false
    }
    //endregion

    //region Private utils Method
    private fun setIpAccess() {
        textViewIpAccess.setText(getIpAccess())
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

    private fun getPortFromEditText(): Int {
        val valueEditText = editTextPort.text.toString()
        return if (valueEditText.length > 0) valueEditText.toInt() else DEFAULT_PORT
    }

    fun isConnectedInWifi(): Boolean {
        val wifiManager =
            applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val networkInfo =
            (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        return (networkInfo != null && networkInfo.isAvailable && networkInfo.isConnected
                && wifiManager.isWifiEnabled && networkInfo.typeName == "WIFI")
    }
    //endregion

    override fun onKeyDown(keyCode: Int, evt: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isStarted) {
                AlertDialog.Builder(this)
                    .setTitle(R.string.warning)
                    .setMessage(R.string.dialog_exit_message)
                    .setPositiveButton(
                        resources.getString(android.R.string.ok),
                        DialogInterface.OnClickListener { dialog, id -> finish() })
                    .setNegativeButton(resources.getString(android.R.string.cancel), null)
                    .show()
            } else {
                finish()
            }
            return true
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAndroidWebServer()
        isStarted = false
        if (broadcastReceiverNetworkState != null) {
            unregisterReceiver(broadcastReceiverNetworkState)
        }
    }

    companion object {
        private const val DEFAULT_PORT = 8080
    }
}
