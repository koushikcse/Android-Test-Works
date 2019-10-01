package com.kusu.time

import android.content.pm.PackageManager
import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.kusu.time.Constant.KEY_CURRENT_VERSION
import com.kusu.time.Constant.KEY_UPDATE_REQUIRED
import com.kusu.time.Constant.KEY_UPDATE_URL

class ForceUpdateChecker {
    private val TAG = ForceUpdateChecker::class.java.simpleName

    private var onUpdateNeededListener: OnUpdateNeededListener? = null
    private lateinit var context: Context

    interface OnUpdateNeededListener {
        fun onUpdateNeeded(updateUrl: String)
    }

    constructor(
        context: Context,
        onUpdateNeededListener: OnUpdateNeededListener
    ) {
        this.context = context
        this.onUpdateNeededListener = onUpdateNeededListener
    }

    fun check() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()

        if (remoteConfig.getBoolean(KEY_UPDATE_REQUIRED)) {
            val currentVersion = remoteConfig.getString(KEY_CURRENT_VERSION)
            val appVersion = getAppVersion(context)
            val updateUrl = remoteConfig.getString(KEY_UPDATE_URL)


            if (!TextUtils.equals(currentVersion, appVersion) && onUpdateNeededListener != null) {
                onUpdateNeededListener?.onUpdateNeeded(updateUrl)
            }
        }
    }

    private fun getAppVersion(context: Context): String {
        var result = ""

        try {
            result = context.getPackageManager()
                .getPackageInfo(context.getPackageName(), 0)
                .versionName
            result = result.replace("[a-zA-Z]|-".toRegex(), "")
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, e.message)
        }

        return result
    }

}