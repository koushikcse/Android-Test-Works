package com.example.myapplication.routeServer

import android.content.Context

object ContextProvider {
    private var context: Context? = null

    fun setContext(context: Context) {
        this.context = context
    }

    fun getContext(): Context? {
        return context
    }
}