package com.kusu.devicewebserver

import android.content.Context

object ContextProvider {
    private var context: Context? = null

    fun setContext(context: Context) {
        ContextProvider.context = context
    }

    fun getContext(): Context? {
        return context
    }
}