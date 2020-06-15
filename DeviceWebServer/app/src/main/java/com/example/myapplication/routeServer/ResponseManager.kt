package com.example.myapplication.routeServer

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoHTTPD.IHTTPSession
import fi.iki.elonen.NanoHTTPD.ResponseException
import fi.iki.elonen.router.RouterNanoHTTPD
import fi.iki.elonen.router.RouterNanoHTTPD.UriResource
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

class ResponseManager : RouterNanoHTTPD.DefaultHandler() {

    override fun get(
        uriResource: UriResource?,
        urlParams: MutableMap<String, String>?,
        session: IHTTPSession?
    ): NanoHTTPD.Response {
        val uri = session?.uri
        val method = session?.method

        if (uri.equals(ApiUri.START) && method == NanoHTTPD.Method.GET) {
            return ApiHandler.startApi()
        } else if (uri.equals(ApiUri.STOP) && method == NanoHTTPD.Method.GET) {
            return ApiHandler.stopApi()
        } else if (uri.equals(ApiUri.IS_ACTIVE) && method == NanoHTTPD.Method.GET) {
            return ApiHandler.checkRunningApi()
        } else if (uri.equals(ApiUri.TEST_FILE) && method == NanoHTTPD.Method.GET) {
            val param =
                session.parameters
            if (param.containsKey("path")) {
                val path = param["path"]!![0]
                return getFileResponse(path)
            }
            return TestFileResponse.TestFileApi()
        } else if (uri.equals("/test") && method == NanoHTTPD.Method.POST) {
            if (session.headers.containsValue("application/json")) {
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
                    Log.i("data", contents)
                    val jsonObject = Gson().fromJson(contents, TestModel::class.java)
                    return getJSONResponse(jsonObject)

//                    return NanoHTTPD.newFixedLengthResponse(
//                        status,
//                        "application/json",
//                        contents
//                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                val files: Map<String, String> = HashMap()
                try {
                    session.parseBody(files)
                } catch (ioe: IOException) {
                    try {
                        // return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.d("Exception", e.message)
                    }
                } catch (re: ResponseException) {
                    try {
                        // return newFixedLengthResponse(re.getStatus(), MIME_PLAINTEXT, re.getMessage());
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.d("Exception", re.message)
                    }
                }
                val postBody = session.queryParameterString
                val postParameter = session.parms["name"]
                if (postParameter != null) {
                    val testModel = TestModel(name = postParameter)
                    return getJSONResponse(testModel)
                }
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

    private fun getJSONResponse(testModel: TestModel): NanoHTTPD.Response {
        val jsonObject = JsonObject()
        jsonObject.addProperty("name", testModel.name)
        return NanoHTTPD.newFixedLengthResponse(
            status,
            "application/json",
            jsonObject.toString()
        )
    }


    private fun getExtensionByStringHandling(filename: String): String? {
        return if (filename.contains(".")) {
            filename.substring(filename.lastIndexOf(".") + 1)
        } else ""
    }

    private fun getFileResponse(path: String): NanoHTTPD.Response {
        try {
            val mimeType = FileHelper.getMimeType(
                getExtensionByStringHandling(path),
                ContextProvider.getContext()
            )
            val inputStream = FileHelper.getInputStreamFromUriString(
                path,
                ContextProvider.getContext()
            )
            return NanoHTTPD.newChunkedResponse(status, mimeType, inputStream)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return NanoHTTPD.newFixedLengthResponse(status, "text/html", "File Not Found")
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