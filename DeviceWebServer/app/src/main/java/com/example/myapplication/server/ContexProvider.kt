package com.example.myapplication.server

import android.app.Activity
import android.content.Context

object ContexProvider {
//    private var context: Context? = null
    private var context: Activity? = null

    fun setContext(context: Activity) {
        this.context = context
    }

    fun getContext(): Activity? {
        return context
    }
}