package com.kusu.networktest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * Created by Koushik on 16/10/19.
 */
class NetworkManager : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val conn = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = conn.activeNetworkInfo

    }
}