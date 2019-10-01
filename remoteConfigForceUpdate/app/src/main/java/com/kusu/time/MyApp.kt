package com.kusu.time

import android.app.Application
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import java.util.*
import kotlin.collections.HashMap

class MyApp : Application(){

    private val TAG = MyApp::class.java!!.getSimpleName()

    override fun onCreate() {
        super.onCreate()
        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        // set in-app defaults
        val remoteConfigDefaults = HashMap<String,Any>()
        remoteConfigDefaults.put(Constant.KEY_UPDATE_REQUIRED, false)
        remoteConfigDefaults.put(Constant.KEY_CURRENT_VERSION, "1.0.0")
        remoteConfigDefaults.put(
            Constant.KEY_UPDATE_URL,
            "https://play.google.com/store/apps/details?id=com.kusu.time"
        )

        firebaseRemoteConfig.setDefaults(remoteConfigDefaults)
        firebaseRemoteConfig.fetch(60) // fetch every 10 sec
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "remote config is fetched SUCCESS.")
                    firebaseRemoteConfig.activateFetched()
                }else{
                    Log.d(TAG, "remote config is fetched FAILED.")
                }
            }

    }
}