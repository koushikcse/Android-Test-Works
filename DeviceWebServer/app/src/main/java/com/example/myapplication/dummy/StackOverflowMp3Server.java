package com.example.myapplication.dummy;

import android.os.Environment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class StackOverflowMp3Server extends NanoHTTPD {

    public StackOverflowMp3Server() {
         super(8089);
    }

    @Override
    public Response serve(String uri, Method method,
                          Map<String, String> header, Map<String, String> parameters,
                          Map<String, String> files) {
    String answer = "";

    FileInputStream fis = null;
    try {
        fis = new FileInputStream(Environment.getExternalStorageDirectory()
                + "/Samsung/Music/Over_the_Horizon.mp3");
    } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return newChunkedResponse(Response.Status.OK, "audio/mpeg", fis);
  }
}