package com.example.myapplication.dummy;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class AndroidWebServer extends NanoHTTPD {

    private Activity activity;
    private File rootpath;

    public AndroidWebServer(int port, Activity mainActivity, File rootfolder) throws IOException {
        super(port);
        activity = mainActivity;
        rootpath = rootfolder;
    }

    public AndroidWebServer(String hostname, int port) {
        super(hostname, port);
    }

//    @Override
//    public Response serve(IHTTPSession session) {
//        String msg = "<html><body><h1>Hello server</h1>\n";
//        Map<String, String> parms = session.getParms();
//        if (parms.get("username") == null) {
//            msg += "<form action='?' method='get'>\n  <p>Your name: <input type='text' name='username'></p>\n" + "</form>\n";
//        } else {
//            msg += "<p>Hello, " + parms.get("username") + "!</p>";
//        }
//        return newFixedLengthResponse(msg + "</body></html>\n");
//    }


    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        String msg = "<html><body><h1>Device Web-Server</h1>\n";

        File[] arrayfile;

        int i = 0;

        try {
            session.parseBody(new HashMap<String, String>());
        } catch (ResponseException | IOException r) {
            r.printStackTrace();
        }

        Map<String, String> parms = session.getParms();
        if (parms.get("username") == null) {
            msg += "<form action='?' method='get'>\n  <p>Your name: <input type='text' name='username'></p>\n" + "</form>\n";
        } else {
            msg += "<p>Hello, " + parms.get("username") + "!</p>";
        }
        msg += "<br><br><a href='/Open_rap'>Open Image</a><br><br>";
        msg += "<br><br><a href='/files'>Browse Files</a><br><br>";
        msg += "<br><br><a href='/getmethod'>GET METHOD OPERATION</a><br><br>";
        msg += "<br><br><a href='/postmethod'>POST METHOD OPERATION</a><br><br>";

        if (uri.equals("/hello")) {
            String response = "Hello World";
            return newFixedLengthResponse(response);
        } else if (uri.equals("/getmethod")) {
            String html = "<html><head><h1>Welcome to the Form</h1><head/><body>";

            if (parms.get("name") == null) {
                html += "<form action='' method='get'> \n " +
                        "<p>Enter Your Name:</p> <input type='text' name='name'>" +
                        "</form>" +
                        "</body>";
            } else {
                html += "<p>Hello Mr. " + parms.get("name") + "</p>" +
                        "</body> ";
            }

            html += "</html>";
            return newFixedLengthResponse(html);

        } else if (uri.equals("/postmethod")) {
            String html = "<html><head><h1>Welcome to the Form</h1><head/><body>";
            Map<String, String> files = new HashMap<String, String>();
            Method method = session.getMethod();
            String postParameter = "";


            html += "<form action='' method='post'> \n " +
                    "<p>Enter Your Name:</p> <input type='text' name='name'>" +
                    "</form>";

            if (Method.POST.equals(method) || Method.PUT.equals(method)) {
                try {
                    session.parseBody(files);
                } catch (IOException ioe) {
                    try {
                        // return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("Exception", e.getMessage());
                    }
                } catch (ResponseException re) {
                    try {
                        // return newFixedLengthResponse(re.getStatus(), MIME_PLAINTEXT, re.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("Exception", re.getMessage());
                    }
                }
            }
            html += "</body></html>";
            String postBody = session.getQueryParameterString();
            postParameter = session.getParms().get("name");
            Log.d("Postbody", postBody + "\n" + postParameter);
            if (postParameter != null) {
                ((MainActivity) activity).setName(postParameter);
                String html1 = "<html><head><h1>" + postParameter + "</h1><head></html>";
                return newFixedLengthResponse(html1);
            }
            return newFixedLengthResponse(Response.Status.OK, "text/html", html);
        } else if (uri.equals("/reqJSON")) {
            /*File root = Environment.getExternalStorageDirectory();
            FileInputStream fis = null;
            File file = new File(root, "a.jpg");
            try {
                if (file.exists()) {
                    fis = new FileInputStream(file);

                } else
                    Log.d("FOF :", "File Not exists:");
                Log.d("FOF path:", root.getAbsolutePath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return newFixedLengthResponse(Response.Status.OK, "image/jpeg", fis, file.length());
*/
            return sendJsonRes(session);
        /*} else if (uri.equals("/files")) {
//            File root = Environment.getExternalStorageDirectory();

            FileInputStream fis = null;
            File file = rootpath;
            try {
                if (file.exists()) {
                    fis = new FileInputStream(file);

                } else
                    Log.d("FOF :", "File Not exists:");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return newFixedLengthResponse(uri, new HashMap<String, String>(), rootpath,0L );*/
        } else if (uri.contains(".")) {

            String[] split = uri.split("/");
            String s = "";
            for (i = 0; i < split.length; i++) {
                Log.d("String", "" + split[i] + "" + i);
                s = s + "/" + split[i];
            }
            String x = s.substring(1, s.length());
            Log.d("String2", s);
            Log.d("String2", x + "  " + x.length());
            String y = NanoHTTPD.getMimeTypeForFile(x);
            Log.d("MIME-TYPE", y);
            File root = Environment.getExternalStorageDirectory();
            FileInputStream fis = null;
            //File file = new File(root.getAbsolutePath() + "/"+split[1]+"/"+split[2]+"/"+split[3]);
            File file = new File(root.getAbsolutePath() + x);
            try {
                if (file.exists()) {
                    fis = new FileInputStream(file);

                } else
                    Log.d("FOF :", "File Not exists:");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return newFixedLengthResponse(Response.Status.OK, y, fis, file.length());

        } else {
            return newFixedLengthResponse(msg + "</body></html>\n");
        }

    }


    private Response showImageFile() {
        File root = Environment.getExternalStorageDirectory();
        FileInputStream fis = null;
        File file = new File(root, "a.jpg");
        try {
            if (file.exists()) {
                fis = new FileInputStream(file);

            } else
                Log.d("FOF :", "File Not exists:");
            Log.d("FOF path:", root.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return newFixedLengthResponse(Response.Status.OK, "image/jpeg", fis, file.length());
    }

    private Response sendJsonRes(IHTTPSession session) {

        Map<String, String> files = new HashMap<String, String>();
        Method method = session.getMethod();
        Map<String, String> headers = session.getHeaders();
        String postParameter = "";

        if (Method.POST.equals(method) || Method.PUT.equals(method)) {
            if ("application/json".equals(headers.get("content-type"))) {
                long size;
                if (session.getHeaders().containsKey("content-length")) {
                    size = Integer.parseInt(session.getHeaders().get("content-length"));
                } else {
                    size = 0;
                }
                InputStream inputStream = session.getInputStream();

                try {
                    InputStreamReader isReader = new InputStreamReader(inputStream);
                    //Creating a character array
                    char charArray[] = new char[(int) size];
                    //Reading the contents of the reader
                    isReader.read(charArray);
                    //Converting character array to a String
                    String contents = new String(charArray);
                    ReqModel jsonObject = new Gson().fromJson(contents, ReqModel.class);

                    postParameter = jsonObject.getName();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    session.parseBody(files);
                } catch (IOException ioe) {
                    try {
                        // return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("Exception", e.getMessage());
                    }
                } catch (ResponseException re) {
                    try {
                        // return newFixedLengthResponse(re.getStatus(), MIME_PLAINTEXT, re.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("Exception", re.getMessage());
                    }
                }
                String postBody = session.getQueryParameterString();
                postParameter = session.getParms().get("name");
                Log.d("Postbody", postBody + "\n" + postParameter);
            }
        }

        if (postParameter != null) {
            ((MainActivity) activity).setName(postParameter);
            JSONObject res = createJson(postParameter);
            return newFixedLengthResponse(Response.Status.OK, "application/json", res.toString());
        }
        JSONObject error = new JSONObject();
        try {
            error.put("response", "not found");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newFixedLengthResponse(Response.Status.OK, "application/json", error.toString());

    }


    private JSONObject createJson(String name) {
        JSONObject obj = new JSONObject();
        JSONObject res = new JSONObject();
        try {
            res.put("id", "1");
            res.put("name", name);
            res.put("mobile", "9876543210");
            res.put("company", "Arc");
            res.put("birthday", "1/1/1990");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            obj.put("response", res);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

}