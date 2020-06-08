package com.example.myapplication.server

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import fi.iki.elonen.NanoHTTPD
import org.json.JSONException
import org.json.JSONObject


object ApiHandler {
    var isWevServerStarted = false
    private var context: Context? = null

    fun startApi(context: Context?): NanoHTTPD.Response {
        this.context = context
        val obj = JSONObject()

        if (!isWevServerStarted) {
            isWevServerStarted = true
            try {
                obj.put("success", true)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            context?.let {
                val intent = Intent("START_WEB_SERVICE")
                LocalBroadcastManager.getInstance(it).sendBroadcast(intent)
            }
//            val intent = Intent("START_WEB_SERVICE")
//            context?.sendBroadcast(intent)
        } else {
            try {
                obj.put("success", true)
                obj.put("message", "sync is already running")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        return NanoHTTPD.newFixedLengthResponse(
            NanoHTTPD.Response.Status.OK,
            "application/json",
            obj.toString()
        )
    }

    fun stopApi(context: Context?): NanoHTTPD.Response {
        this.context = context
        val obj = JSONObject()

        if (isWevServerStarted) {
            isWevServerStarted = false
            try {
                obj.put("success", true)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            context?.let {
                val intent = Intent("STOP_WEB_SERVICE")
                LocalBroadcastManager.getInstance(it).sendBroadcast(intent)
            }

//            val intent = Intent("STOP_WEB_SERVICE")
//            context?.sendBroadcast(intent)
        } else {
            try {
                obj.put("success", false)
                obj.put("message", "sync is not started")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        return NanoHTTPD.newFixedLengthResponse(
            NanoHTTPD.Response.Status.OK,
            "application/json",
            obj.toString()
        )
    }

    fun checkRunningApi(context: Context?): NanoHTTPD.Response {
        this.context = context
        val obj = JSONObject()
        try {
            obj.put("success", true)
            obj.put("message", "server is active")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return NanoHTTPD.newFixedLengthResponse(
            NanoHTTPD.Response.Status.OK,
            "application/json",
            obj.toString()
        )

    }
}