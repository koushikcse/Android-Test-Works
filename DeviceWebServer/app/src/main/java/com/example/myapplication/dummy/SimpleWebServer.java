package com.example.myapplication.dummy;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;

import fi.iki.elonen.NanoHTTPD;

public class SimpleWebServer extends NanoHTTPD {
  /**
   * Hashtable mapping (String)FILENAME_EXTENSION -> (String)MIME_TYPE
   */
  private static final Map<String, String> MIME_TYPES = new HashMap<String, String>() {{
      put("css", "text/css");
      put("htm", "text/html");
      put("html", "text/html");
      put("xml", "text/xml");
      put("java", "text/x-java-source, text/java");
      put("txt", "text/plain");
      put("asc", "text/plain");
      put("gif", "image/gif");
      put("jpg", "image/jpeg");
      put("jpeg", "image/jpeg");
      put("png", "image/png");
      put("mp3", "audio/mpeg");
      put("m3u", "audio/mpeg-url");
      put("mp4", "video/mp4");
      put("ogv", "video/ogg");
      put("flv", "video/x-flv");
      put("mov", "video/quicktime");
      put("swf", "application/x-shockwave-flash");
      put("js", "application/javascript");
      put("pdf", "application/pdf");
      put("doc", "application/msword");
      put("ogg", "application/x-ogg");
      put("zip", "application/octet-stream");
      put("exe", "application/octet-stream");
      put("class", "application/octet-stream");
  }};

  /**
   * The distribution licence
   */
  private static final String LICENCE =
          "Copyright (c) 2012-2013 by Paul S. Hawke, 2001,2005-2013 by Jarno Elonen, 2010 by Konstantinos Togias\n"
                  + "\n"
                  + "Redistribution and use in source and binary forms, with or without\n"
                  + "modification, are permitted provided that the following conditions\n"
                  + "are met:\n"
                  + "\n"
                  + "Redistributions of source code must retain the above copyright notice,\n"
                  + "this list of conditions and the following disclaimer. Redistributions in\n"
                  + "binary form must reproduce the above copyright notice, this list of\n"
                  + "conditions and the following disclaimer in the documentation and/or other\n"
                  + "materials provided with the distribution. The name of the author may not\n"
                  + "be used to endorse or promote products derived from this software without\n"
                  + "specific prior written permission. \n"
                  + " \n"
                  + "THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR\n"
                  + "IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES\n"
                  + "OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.\n"
                  + "IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,\n"
                  + "INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT\n"
                  + "NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,\n"
                  + "DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY\n"
                  + "THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT\n"
                  + "(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE\n"
                  + "OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.";

  private final File rootDir;
  private final boolean quiet;
  MessageDisplayer msgDsplyr;

  public SimpleWebServer(MessageDisplayer md, String host, int port, File wwwroot, boolean quiet) {
      super(host, port);
      this.rootDir = wwwroot;
      this.quiet = quiet;
      msgDsplyr = md; // save reference to where we can show messages
      System.out.println("SWS host="+host +", port="+port + ", root="+wwwroot); //<<<<<<<
  }

  File getRootDir() {
      return rootDir;
  }

  /**
   * URL-encodes everything between "/"-characters. Encodes spaces as '%20' instead of '+'.
   */
  private String encodeUri(String uri) {
      String newUri = "";
      StringTokenizer st = new StringTokenizer(uri, "/ ", true);
      while (st.hasMoreTokens()) {
          String tok = st.nextToken();
          if (tok.equals("/"))
              newUri += "/";
          else if (tok.equals(" "))
              newUri += "%20";
          else {
              try {
                  newUri += URLEncoder.encode(tok, "UTF-8");
              } catch (UnsupportedEncodingException ignored) {
              }
          }
      }
      return newUri;
  }

  /**  =================================================================================================================
   * Serves file from homeDir and its' subdirectories (only). Uses only URI, ignores all headers and HTTP parameters.
   */
  Response serveFile(String uri, Map<String, String> header, File homeDir) {
      Response res = null;

      // Make sure we won't die of an exception later
      if (!homeDir.isDirectory()) {
          res = newFixedLengthResponse(Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, "INTERNAL ERRROR: serveFile(): given homeDir is not a directory: "
                               + homeDir);                        //<<<<
      }
      System.out.println("SWS serveFile() homeDir="+homeDir +", uri="+uri); //<<<<<<<<<<

      if (res == null) {
          // Remove URL arguments
          uri = uri.trim().replace(File.separatorChar, '/');
          if (uri.indexOf('?') >= 0)
              uri = uri.substring(0, uri.indexOf('?'));
          System.out.println("SWS new uri="+uri); //<<<<<<<<<<<<<<

          // Prohibit getting out of current directory
          if (uri.startsWith("src/main") || uri.endsWith("src/main") || uri.contains("../"))
              res = newFixedLengthResponse(Response.Status.FORBIDDEN, NanoHTTPD.MIME_PLAINTEXT, "FORBIDDEN: Won't serve ../ for security reasons.");
      }

      File f = new File(homeDir, uri);
//      System.out.println("homeDir="+homeDir +", uri="+uri); //<<<<<<<<<<
      // f=D:\www\NanoServerTesting\servlet\SaveFile
      if (res == null && !f.exists()) {
          if(uri.contains("StopServer")) {
            res = newFixedLengthResponse(Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "Error 999, Server stopping.");
            stop();
            synchronized(waitMonitor) {
               waitMonitor.notify(); //<<<< let main exit
            }
            return res; 
          }else {
        	 System.out.println(">>SWS File not found f="+f+"<"); //<<<<<<<<<<<
             System.out.println(">>SWS homeDir="+homeDir +", uri="+uri); //<<<<<<<<<<
             res = newFixedLengthResponse(Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "Error 404, file not found.");
          }
      }

      // List the directory, if necessary
      if (res == null && f.isDirectory()) {
          // Browsers get confused without '/' after the
          // directory, send a redirect.
          if (!uri.endsWith("/")) {
              uri += "/";
              res = newFixedLengthResponse(Response.Status.REDIRECT, NanoHTTPD.MIME_HTML,
            		             "<html><body>Redirected: <a href=\"" + uri + "\">" + uri
                                 + "</a></body></html>");
              res.addHeader("Location", uri);
          }

          if (res == null) {
              // First try index.html and index.htm
              if (new File(f, "index.html").exists()) {
                  f = new File(homeDir, uri + "/index.html");
              } else if (new File(f, "index.htm").exists()) {
                  f = new File(homeDir, uri + "/index.htm");
              } else if (f.canRead()) {
                  // No index file, list the directory if it is readable
                  res = newFixedLengthResponse(listDirectory(uri, f));
              } else {
                  res = newFixedLengthResponse(Response.Status.FORBIDDEN, NanoHTTPD.MIME_PLAINTEXT, "FORBIDDEN: No directory listing.");
              }
          }
      }

      try {
          if (res == null) {
              // Get MIME type from file name extension, if possible
              String mime = null;
              int dot = f.getCanonicalPath().lastIndexOf('.');
              if (dot >= 0) {
                  mime = MIME_TYPES.get(f.getCanonicalPath().substring(dot + 1).toLowerCase());
              }
              if (mime == null) {
                  mime = NanoHTTPD.MIME_PLAINTEXT;
              }

              // Calculate etag
              String etag = Integer.toHexString((f.getAbsolutePath() + f.lastModified() + "" + f.length()).hashCode());

              // Support (simple) skipping:
              long startFrom = 0;
              long endAt = -1;
              String range = header.get("range");
              if (range != null) {
                  if (range.startsWith("bytes=")) {
                      range = range.substring("bytes=".length());
                      int minus = range.indexOf('-');
                      try {
                          if (minus > 0) {
                              startFrom = Long.parseLong(range.substring(0, minus));
                              endAt = Long.parseLong(range.substring(minus + 1));
                          }
                      } catch (NumberFormatException ignored) {
                      }
                  }
              }

              // Change return code and add Content-Range header when skipping is requested
              long fileLen = f.length();
              if (range != null && startFrom >= 0) {
                  if (startFrom >= fileLen) {
                      res = newFixedLengthResponse(Response.Status.RANGE_NOT_SATISFIABLE, NanoHTTPD.MIME_PLAINTEXT, "");
                      res.addHeader("Content-Range", "bytes 0-0/" + fileLen);
                      res.addHeader("ETag", etag);
                  } else {
                      if (endAt < 0) {
                          endAt = fileLen - 1;
                      }
                      long newLen = endAt - startFrom + 1;
                      if (newLen < 0) {
                          newLen = 0;
                      }

                      final long dataLen = newLen;
                      FileInputStream fis = new FileInputStream(f) {
                          @Override
                          public int available() throws IOException {
                              return (int) dataLen;
                          }
                      };
                      fis.skip(startFrom);

                      res = newFixedLengthResponse(Response.Status.PARTIAL_CONTENT, mime, fis,f.length());
                      res.addHeader("Content-Length", "" + dataLen);
                      res.addHeader("Content-Range", "bytes " + startFrom + "-" + endAt + "/" + fileLen);
                      res.addHeader("ETag", etag);
                  }
              } else {
                  if (etag.equals(header.get("if-none-match")))
                      res = newFixedLengthResponse(Response.Status.NOT_MODIFIED, mime, "");
                  else {
                      res = newFixedLengthResponse(Response.Status.OK, mime, new FileInputStream(f),f.length());
                      res.addHeader("Content-Length", "" + fileLen);
                      res.addHeader("ETag", etag);
                  }
              }
          }
      } catch (IOException ioe) {
          res = newFixedLengthResponse(Response.Status.FORBIDDEN, NanoHTTPD.MIME_PLAINTEXT, "FORBIDDEN: Reading file failed.");
      }

      res.addHeader("Accept-Ranges", "bytes"); // Announce that the file server accepts partial content requestes
      return res;
  }

  //=====================================================================================================
  private String listDirectory(String uri, File f) {
	  System.out.println("SWS listDir uri="+uri + ", f="+f); //<<<<<<<<<<<<<<
      String heading = "Directory " + uri;
      String msg = "<html><head><title>" + heading + "</title><style><!--\n"+
              "span.dirname { font-weight: bold; }\n"+
              "span.filesize { font-size: 75%; }\n"+
              "// -->\n"+
              "</style>"+
              "</head><body><h1>" + heading + "</h1>";

      String up = null;
      if (uri.length() > 1) {
          String u = uri.substring(0, uri.length() - 1);
          int slash = u.lastIndexOf('/');
          if (slash >= 0 && slash < u.length()) {
              up = uri.substring(0, slash + 1);
          }
      }

      List<String> files = Arrays.asList(f.list(new FilenameFilter() {
          @Override
          public boolean accept(File dir, String name) {
              return new File(dir, name).isFile();
          }
      }));
      Collections.sort(files);
      List<String> directories = Arrays.asList(f.list(new FilenameFilter() {
          @Override
          public boolean accept(File dir, String name) {
              return new File (dir, name).isDirectory();
          }
      }));
      Collections.sort(directories);
      if (up != null || directories.size() + files.size() > 0) {
          msg += "<ul>";
          if (up != null || directories.size() > 0) {
              msg += "<section class=\"directories\">";
              if (up != null) {
                  msg += "<li><a rel=\"directory\" href=\"" + up + "\"><span class=\"dirname\">..</span></a></b></li>";
              }
              for (int i = 0; i < directories.size(); i++) {
                  String dir = directories.get(i) + "/";
                  msg += "<li><a rel=\"directory\" href=\"" + encodeUri(uri + dir) + "\"><span class=\"dirname\">" + dir + "</span></a></b></li>";
              }
              msg += "</section>";
          }
          if (files.size() > 0) {
              msg += "<section class=\"files\">";
              for (int i = 0; i < files.size(); i++) {
                  String file = files.get(i);

                  msg += "<li><a href=\"" + encodeUri(uri + file) + "\"><span class=\"filename\">" + file + "</span></a>";
                  File curFile = new File(f, file);
                  long len = curFile.length();
                  msg += "&nbsp;<span class=\"filesize\">(";
                  if (len < 1024)
                      msg += len + " bytes";
                  else if (len < 1024 * 1024)
                      msg += len / 1024 + "." + (len % 1024 / 10 % 100) + " KB";
                  else
                      msg += len / (1024 * 1024) + "." + len % (1024 * 1024) / 10 % 100 + " MB";
                  msg += ")</span></li>";
              }
              msg += "</section>";
          }
          msg += "</ul>";
      }
      msg += "</body></html>";
      return msg;
  }

  //====================================================================================================================================
/*
  @Override
  public Response serve(String uri, Method method, Map<String, String> header, Map<String, String> parms, Map<String, String> files) {
      if (!quiet) {
    	  String msg = method + " '" + uri + "'\n";
          System.out.print(msg);

          Iterator<String> e = header.keySet().iterator();
          while (e.hasNext()) {
              String value = e.next();
              System.out.println("  HDR: '" + value + "' = '" + header.get(value) + "'");
          }
          e = parms.keySet().iterator();
          while (e.hasNext()) {
              String value = e.next();
              String msg1 = "  PRM: '" + value + "' = '" + parms.get(value) + "'\n";
              msg += msg1;              // build message
              System.out.print(msg1);
          }
          e = files.keySet().iterator();
          while (e.hasNext()) {
              String value = e.next();
              String msg2 = "  UPLOADED: '" + value + "' = '" + files.get(value) + "'\n";
              msg += msg2;                // build message
              System.out.print(msg2);
          }
          msgDsplyr.showMessage(msg);   //  Send message to be shown to user
      }
      return serveFile(uri, header, getRootDir());
  }
*/

  static Object waitMonitor = new Object();


    @Override
    public Response serve(IHTTPSession session) {
        String msg = "<html><body><h1>Hello server</h1>\n";
        Map<String, String> parms = session.getParms();
        if (parms.get("username") == null) {
            msg += "<form action='?' method='get'>\n  <p>Your name: <input type='text' name='username'></p>\n" + "</form>\n";
        } else {
            msg += "<p>Hello, " + parms.get("username") + "!</p>";
        }
        return newFixedLengthResponse(msg + "</body></html>\n");
    }

  /** ====================================================================================
   * Starts as a standalone file server and waits for Enter.
   */
/*  
  public static void main(String[] args) {
      // Defaults
      int port = 8080;
      String root = "D:/www/NanoServerTesting/"; //".";        //<<<<<<<<<<<<<<<<<<<<
      System.setProperty("java.io.tmpdir", "D:\\www\\NanoServerTesting\\SavedFiles");  //<<<<<<<<< where to write uploads
      String host = "127.0.0.1";
      File wwwroot = new File(root).getAbsoluteFile();
      boolean quiet = false;
      // Parse command-line, with short and long versions of the options.
      for (int i = 0; i < args.length; ++i) {
          if (args[i].equalsIgnoreCase("-h") || args[i].equalsIgnoreCase("--host")) {
              host = args[i + 1];
          } else if (args[i].equalsIgnoreCase("-p") || args[i].equalsIgnoreCase("--port")) {
              port = Integer.parseInt(args[i + 1]);
          } else if (args[i].equalsIgnoreCase("-q") || args[i].equalsIgnoreCase("--quiet")) {
              quiet = true;
          } else if (args[i].equalsIgnoreCase("-d") || args[i].equalsIgnoreCase("--dir")) {
              wwwroot = new File(args[i + 1]).getAbsoluteFile();
          } else if (args[i].equalsIgnoreCase("--licence")) {
              System.out.println(LICENCE + "\n");
              break;
          }
      }
//      ServerRunner.executeInstance(new SimpleWebServer(host, port, wwwroot, quiet));   //  Hangs on read of System.in
      SimpleWebServer server =  new SimpleWebServer(null, host, port, wwwroot, quiet);
      try {
          server.start();
      } catch (IOException ioe) {
          System.err.println("Couldn't start server:\n" + ioe);
          System.exit(-1);
      }
      synchronized(waitMonitor) {
         try {waitMonitor.wait();}catch(Exception x) {x.printStackTrace();}
      }
      try {Thread.sleep(100);}catch(Exception x) {}
      System.out.println("Server stopped.");
  }
*/  
}