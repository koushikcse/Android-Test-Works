package com.example.myapplication.routeServer

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoHTTPD.IHTTPSession
import fi.iki.elonen.router.RouterNanoHTTPD
import fi.iki.elonen.router.RouterNanoHTTPD.UriResource
import java.io.IOException
import java.io.InputStreamReader

class ResponseManager : RouterNanoHTTPD.DefaultHandler() {

    override fun get(
        uriResource: UriResource?,
        urlParams: MutableMap<String, String>?,
        session: IHTTPSession?
    ): NanoHTTPD.Response {
        val uri = session?.uri

        if (uri.equals("/start")) {
            return ApiHandler.startApi()
        } else if (uri.equals("/stop")) {
            return ApiHandler.stopApi()
        } else if (uri.equals("/isActive")) {
            return ApiHandler.checkRunningApi()
        } else {
            val size: Long = if (session!!.headers.containsKey("content-length")) {
                session.headers["content-length"]!!.toInt().toLong()
            } else {
                0
            }
            val inputStream = session.inputStream

            try {
                val isReader = InputStreamReader(inputStream)
                //Creating a character array
                val charArray = CharArray(size.toInt())
                //Reading the contents of the reader
                isReader.read(charArray)
                //Converting character array to a String
                val contents = String(charArray)
                val jsonObject = Gson().fromJson(contents, TestModel::class.java)
                Log.i("data", contents)
                Log.i("data", jsonObject.name)
                return getJSONResponse(jsonObject)!!
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return NanoHTTPD.newFixedLengthResponse("Error response")
        }
        return NanoHTTPD.newFixedLengthResponse("Error response")
    }


    override fun post(
        uriResource: UriResource?,
        urlParams: Map<String?, String?>?,
        session: IHTTPSession?
    ): NanoHTTPD.Response? {
        return super.post(uriResource, urlParams, session)
    }

    private fun getJSONResponse(testModel: TestModel): NanoHTTPD.Response? {
        val jsonObject = JsonObject()
        jsonObject.addProperty("name", testModel.name)
        return NanoHTTPD.newFixedLengthResponse(
            status,
            "application/json",
            jsonObject.toString()
        )
    }


    override fun getStatus(): NanoHTTPD.Response.IStatus {
        return NanoHTTPD.Response.Status.OK
    }

    override fun getMimeType(): String {
        return "application/json"
    }

    override fun getText(): String {
        return ""
    }
}