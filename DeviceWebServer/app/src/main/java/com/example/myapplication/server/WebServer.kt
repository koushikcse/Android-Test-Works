package com.example.myapplication.server

import android.content.Context
import fi.iki.elonen.NanoHTTPD

class WebServer : NanoHTTPD {
    private var context: Context? = null

    constructor(port: Int, context: Context?) : super(port) {
        this.context = context
    }

    constructor(hostname: String, port: Int, context: Context?) : super(hostname, port) {
        this.context = context
    }

    override fun serve(session: IHTTPSession?): Response {
        val uri = session?.uri

        if (uri == "/start") {
            return ApiHandler.startApi(context)
        } else if (uri == "/stop") {
            return ApiHandler.stopApi(context)
        } else if (uri == "/isActive") {
            return ApiHandler.checkRunningApi(context)
        }

        val msg = "<html><body><h1>Hello, Device web-server running...</h1></body></html>";
        return newFixedLengthResponse(msg);
    }

    val res = context?.filesDir
}