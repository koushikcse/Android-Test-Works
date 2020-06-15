package com.example.myapplication.routeServer

import android.os.Environment
import com.google.gson.Gson
import fi.iki.elonen.NanoHTTPD
import java.io.File

object TestFileResponse {

    fun TestFileApi(): NanoHTTPD.Response {
        var list: ArrayList<TestModel> = ArrayList()

        val directory =
            File(
                Environment.getExternalStorageDirectory().absolutePath + File.separator + "compliance" + File.separator + "historical_documents" + File.separator + "3193"
        )

        if (directory.isDirectory) {
            val files = directory.listFiles()
            for (item in files) {
                val testModel = TestModel(item.hashCode(), item.name, item.absolutePath)
                list.add(testModel)
            }
        }

        val gson = Gson()
        val res = gson.toJson(list)

        return NanoHTTPD.newFixedLengthResponse(
            NanoHTTPD.Response.Status.OK,
            "application/json",
            res.toString()
        )

    }
}