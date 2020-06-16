package com.kusu.devicewebserver;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public   class FileHelper {
    private static final String LOG_TAG = "FileUtils";
    private static final String _DATA = "_data";

    FileHelper(){
        _mappings.put(".323", "text/h323");
        _mappings.put(".3g2", "video/3gpp2");
        _mappings.put(".3gp", "video/3gpp");
        _mappings.put(".3gp2", "video/3gpp2");
        _mappings.put(".3gpp", "video/3gpp");
        _mappings.put(".7z", "application/x-7z-compressed");
        _mappings.put(".aa", "audio/audible");
        _mappings.put(".AAC", "audio/aac");
        _mappings.put(".aaf", "application/octet-stream");
        _mappings.put(".aax", "audio/vnd.audible.aax");
        _mappings.put(".ac3", "audio/ac3");
        _mappings.put(".aca", "application/octet-stream");
        _mappings.put(".accda", "application/msaccess.addin");
        _mappings.put(".accdb", "application/msaccess");
        _mappings.put(".accdc", "application/msaccess.cab");
        _mappings.put(".accde", "application/msaccess");
        _mappings.put(".accdr", "application/msaccess.runtime");
        _mappings.put(".accdt", "application/msaccess");
        _mappings.put(".accdw", "application/msaccess.webapplication");
        _mappings.put(".accft", "application/msaccess.ftemplate");
        _mappings.put(".acx", "application/internet-property-stream");
        _mappings.put(".AddIn", "text/xml");
        _mappings.put(".ade", "application/msaccess");
        _mappings.put(".adobebridge", "application/x-bridge-url");
        _mappings.put(".adp", "application/msaccess");
        _mappings.put(".ADT", "audio/vnd.dlna.adts");
        _mappings.put(".ADTS", "audio/aac");
        _mappings.put(".afm", "application/octet-stream");
        _mappings.put(".ai", "application/postscript");
        _mappings.put(".aif", "audio/x-aiff");
        _mappings.put(".aifc", "audio/aiff");
        _mappings.put(".aiff", "audio/aiff");
        _mappings.put(".air", "application/vnd.adobe.air-application-installer-package+zip");
        _mappings.put(".amc", "application/x-mpeg");
        _mappings.put(".application", "application/x-ms-application");
        _mappings.put(".art", "image/x-jg");
        _mappings.put(".asa", "application/xml");
        _mappings.put(".asax", "application/xml");
        _mappings.put(".ascx", "application/xml");
        _mappings.put(".asd", "application/octet-stream");
        _mappings.put(".asf", "video/x-ms-asf");
        _mappings.put(".ashx", "application/xml");
        _mappings.put(".asi", "application/octet-stream");
        _mappings.put(".asm", "text/plain");
        _mappings.put(".asmx", "application/xml");
        _mappings.put(".aspx", "application/xml");
        _mappings.put(".asr", "video/x-ms-asf");
        _mappings.put(".asx", "video/x-ms-asf");
        _mappings.put(".atom", "application/atom+xml");
        _mappings.put(".au", "audio/basic");
        _mappings.put(".avi", "video/x-msvideo");
        _mappings.put(".axs", "application/olescript");
        _mappings.put(".bas", "text/plain");
        _mappings.put(".bcpio", "application/x-bcpio");
        _mappings.put(".bin", "application/octet-stream");
        _mappings.put(".bmp", "image/bmp");
        _mappings.put(".c", "text/plain");
        _mappings.put(".cab", "application/octet-stream");
        _mappings.put(".caf", "audio/x-caf");
        _mappings.put(".calx", "application/vnd.ms-office.calx");
        _mappings.put(".cat", "application/vnd.ms-pki.seccat");
        _mappings.put(".cc", "text/plain");
        _mappings.put(".cd", "text/plain");
        _mappings.put(".cdda", "audio/aiff");
        _mappings.put(".cdf", "application/x-cdf");
        _mappings.put(".cer", "application/x-x509-ca-cert");
        _mappings.put(".chm", "application/octet-stream");
        _mappings.put(".class", "application/x-java-applet");
        _mappings.put(".clp", "application/x-msclip");
        _mappings.put(".cmx", "image/x-cmx");
        _mappings.put(".cnf", "text/plain");
        _mappings.put(".cod", "image/cis-cod");
        _mappings.put(".config", "application/xml");
        _mappings.put(".contact", "text/x-ms-contact");
        _mappings.put(".coverage", "application/xml");
        _mappings.put(".cpio", "application/x-cpio");
        _mappings.put(".cpp", "text/plain");
        _mappings.put(".crd", "application/x-mscardfile");
        _mappings.put(".crl", "application/pkix-crl");
        _mappings.put(".crt", "application/x-x509-ca-cert");
        _mappings.put(".cs", "text/plain");
        _mappings.put(".csdproj", "text/plain");
        _mappings.put(".csh", "application/x-csh");
        _mappings.put(".csproj", "text/plain");
        _mappings.put(".css", "text/css");
        _mappings.put(".csv", "text/csv");
        _mappings.put(".cur", "application/octet-stream");
        _mappings.put(".cxx", "text/plain");
        _mappings.put(".dat", "application/octet-stream");
        _mappings.put(".datasource", "application/xml");
        _mappings.put(".dbproj", "text/plain");
        _mappings.put(".dcr", "application/x-director");
        _mappings.put(".def", "text/plain");
        _mappings.put(".deploy", "application/octet-stream");
        _mappings.put(".der", "application/x-x509-ca-cert");
        _mappings.put(".dgml", "application/xml");
        _mappings.put(".dib", "image/bmp");
        _mappings.put(".dif", "video/x-dv");
        _mappings.put(".dir", "application/x-director");
        _mappings.put(".disco", "text/xml");
        _mappings.put(".dll", "application/x-msdownload");
        _mappings.put(".dll.config", "text/xml");
        _mappings.put(".dlm", "text/dlm");
        _mappings.put(".doc", "application/msword");
        _mappings.put(".docm", "application/vnd.ms-word.document.macroEnabled.12");
        _mappings.put(".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        _mappings.put(".dot", "application/msword");
        _mappings.put(".dotm", "application/vnd.ms-word.template.macroEnabled.12");
        _mappings.put(".dotx", "application/vnd.openxmlformats-officedocument.wordprocessingml.template");
        _mappings.put(".dsp", "application/octet-stream");
        _mappings.put(".dsw", "text/plain");
        _mappings.put(".dtd", "text/xml");
        _mappings.put(".dtsConfig", "text/xml");
        _mappings.put(".dv", "video/x-dv");
        _mappings.put(".dvi", "application/x-dvi");
        _mappings.put(".dwf", "drawing/x-dwf");
        _mappings.put(".dwp", "application/octet-stream");
        _mappings.put(".dxr", "application/x-director");
        _mappings.put(".eml", "message/rfc822");
        _mappings.put(".emz", "application/octet-stream");
        _mappings.put(".eot", "application/octet-stream");
        _mappings.put(".eps", "application/postscript");
        _mappings.put(".etl", "application/etl");
        _mappings.put(".etx", "text/x-setext");
        _mappings.put(".evy", "application/envoy");
        _mappings.put(".exe", "application/octet-stream");
        _mappings.put(".exe.config", "text/xml");
        _mappings.put(".fdf", "application/vnd.fdf");
        _mappings.put(".fif", "application/fractals");
        _mappings.put(".filters", "Application/xml");
        _mappings.put(".fla", "application/octet-stream");
        _mappings.put(".flr", "x-world/x-vrml");
        _mappings.put(".flv", "video/x-flv");
        _mappings.put(".fsscript", "application/fsharp-script");
        _mappings.put(".fsx", "application/fsharp-script");
        _mappings.put(".generictest", "application/xml");
        _mappings.put(".gif", "image/gif");
        _mappings.put(".group", "text/x-ms-group");
        _mappings.put(".gsm", "audio/x-gsm");
        _mappings.put(".gtar", "application/x-gtar");
        _mappings.put(".gz", "application/x-gzip");
        _mappings.put(".h", "text/plain");
        _mappings.put(".hdf", "application/x-hdf");
        _mappings.put(".hdml", "text/x-hdml");
        _mappings.put(".hhc", "application/x-oleobject");
        _mappings.put(".hhk", "application/octet-stream");
        _mappings.put(".hhp", "application/octet-stream");
        _mappings.put(".hlp", "application/winhlp");
        _mappings.put(".hpp", "text/plain");
        _mappings.put(".hqx", "application/mac-binhex40");
        _mappings.put(".hta", "application/hta");
        _mappings.put(".htc", "text/x-component");
        _mappings.put(".htm", "text/html");
        _mappings.put(".html", "text/html");
        _mappings.put(".htt", "text/webviewhtml");
        _mappings.put(".hxa", "application/xml");
        _mappings.put(".hxc", "application/xml");
        _mappings.put(".hxd", "application/octet-stream");
        _mappings.put(".hxe", "application/xml");
        _mappings.put(".hxf", "application/xml");
        _mappings.put(".hxh", "application/octet-stream");
        _mappings.put(".hxi", "application/octet-stream");
        _mappings.put(".hxk", "application/xml");
        _mappings.put(".hxq", "application/octet-stream");
        _mappings.put(".hxr", "application/octet-stream");
        _mappings.put(".hxs", "application/octet-stream");
        _mappings.put(".hxt", "text/html");
        _mappings.put(".hxv", "application/xml");
        _mappings.put(".hxw", "application/octet-stream");
        _mappings.put(".hxx", "text/plain");
        _mappings.put(".i", "text/plain");
        _mappings.put(".ico", "image/x-icon");
        _mappings.put(".ics", "application/octet-stream");
        _mappings.put(".idl", "text/plain");
        _mappings.put(".ief", "image/ief");
        _mappings.put(".iii", "application/x-iphone");
        _mappings.put(".inc", "text/plain");
        _mappings.put(".inf", "application/octet-stream");
        _mappings.put(".inl", "text/plain");
        _mappings.put(".ins", "application/x-internet-signup");
        _mappings.put(".ipa", "application/x-itunes-ipa");
        _mappings.put(".ipg", "application/x-itunes-ipg");
        _mappings.put(".ipproj", "text/plain");
        _mappings.put(".ipsw", "application/x-itunes-ipsw");
        _mappings.put(".iqy", "text/x-ms-iqy");
        _mappings.put(".isp", "application/x-internet-signup");
        _mappings.put(".ite", "application/x-itunes-ite");
        _mappings.put(".itlp", "application/x-itunes-itlp");
        _mappings.put(".itms", "application/x-itunes-itms");
        _mappings.put(".itpc", "application/x-itunes-itpc");
        _mappings.put(".IVF", "video/x-ivf");
        _mappings.put(".jar", "application/java-archive");
        _mappings.put(".java", "application/octet-stream");
        _mappings.put(".jck", "application/liquidmotion");
        _mappings.put(".jcz", "application/liquidmotion");
        _mappings.put(".jfif", "image/pjpeg");
        _mappings.put(".jnlp", "application/x-java-jnlp-file");
        _mappings.put(".jpb", "application/octet-stream");
        _mappings.put(".jpe", "image/jpeg");
        _mappings.put(".jpeg", "image/jpeg");
        _mappings.put(".jpg", "image/jpeg");
        _mappings.put(".js", "application/x-javascript");
        _mappings.put(".json", "application/json");
        _mappings.put(".jsx", "text/jscript");
        _mappings.put(".jsxbin", "text/plain");
        _mappings.put(".latex", "application/x-latex");
        _mappings.put(".library-ms", "application/windows-library+xml");
        _mappings.put(".lit", "application/x-ms-reader");
        _mappings.put(".loadtest", "application/xml");
        _mappings.put(".lpk", "application/octet-stream");
        _mappings.put(".lsf", "video/x-la-asf");
        _mappings.put(".lst", "text/plain");
        _mappings.put(".lsx", "video/x-la-asf");
        _mappings.put(".lzh", "application/octet-stream");
        _mappings.put(".m13", "application/x-msmediaview");
        _mappings.put(".m14", "application/x-msmediaview");
        _mappings.put(".m1v", "video/mpeg");
        _mappings.put(".m2t", "video/vnd.dlna.mpeg-tts");
        _mappings.put(".m2ts", "video/vnd.dlna.mpeg-tts");
        _mappings.put(".m2v", "video/mpeg");
        _mappings.put(".m3u", "audio/x-mpegurl");
        _mappings.put(".m3u8", "audio/x-mpegurl");
        _mappings.put(".m4a", "audio/m4a");
        _mappings.put(".m4b", "audio/m4b");
        _mappings.put(".m4p", "audio/m4p");
        _mappings.put(".m4r", "audio/x-m4r");
        _mappings.put(".m4v", "video/x-m4v");
        _mappings.put(".mac", "image/x-macpaint");
        _mappings.put(".mak", "text/plain");
        _mappings.put(".man", "application/x-troff-man");
        _mappings.put(".manifest", "application/x-ms-manifest");
        _mappings.put(".map", "text/plain");
        _mappings.put(".master", "application/xml");
        _mappings.put(".mda", "application/msaccess");
        _mappings.put(".mdb", "application/x-msaccess");
        _mappings.put(".mde", "application/msaccess");
        _mappings.put(".mdp", "application/octet-stream");
        _mappings.put(".me", "application/x-troff-me");
        _mappings.put(".mfp", "application/x-shockwave-flash");
        _mappings.put(".mht", "message/rfc822");
        _mappings.put(".mhtml", "message/rfc822");
        _mappings.put(".mid", "audio/mid");
        _mappings.put(".midi", "audio/mid");
        _mappings.put(".mix", "application/octet-stream");
        _mappings.put(".mk", "text/plain");
        _mappings.put(".mmf", "application/x-smaf");
        _mappings.put(".mno", "text/xml");
        _mappings.put(".mny", "application/x-msmoney");
        _mappings.put(".mod", "video/mpeg");
        _mappings.put(".mov", "video/quicktime");
        _mappings.put(".movie", "video/x-sgi-movie");
        _mappings.put(".mp2", "video/mpeg");
        _mappings.put(".mp2v", "video/mpeg");
        _mappings.put(".mp3", "audio/mpeg");
        _mappings.put(".mp4", "video/mp4");
        _mappings.put(".mp4v", "video/mp4");
        _mappings.put(".mpa", "video/mpeg");
        _mappings.put(".mpe", "video/mpeg");
        _mappings.put(".mpeg", "video/mpeg");
        _mappings.put(".mpf", "application/vnd.ms-mediapackage");
        _mappings.put(".mpg", "video/mpeg");
        _mappings.put(".mpp", "application/vnd.ms-project");
        _mappings.put(".mpv2", "video/mpeg");
        _mappings.put(".mqv", "video/quicktime");
        _mappings.put(".ms", "application/x-troff-ms");
        _mappings.put(".msi", "application/octet-stream");
        _mappings.put(".mso", "application/octet-stream");
        _mappings.put(".mts", "video/vnd.dlna.mpeg-tts");
        _mappings.put(".mtx", "application/xml");
        _mappings.put(".mvb", "application/x-msmediaview");
        _mappings.put(".mvc", "application/x-miva-compiled");
        _mappings.put(".mxp", "application/x-mmxp");
        _mappings.put(".nc", "application/x-netcdf");
        _mappings.put(".nsc", "video/x-ms-asf");
        _mappings.put(".nws", "message/rfc822");
        _mappings.put(".ocx", "application/octet-stream");
        _mappings.put(".oda", "application/oda");
        _mappings.put(".odc", "text/x-ms-odc");
        _mappings.put(".odh", "text/plain");
        _mappings.put(".odl", "text/plain");
        _mappings.put(".odp", "application/vnd.oasis.opendocument.presentation");
        _mappings.put(".ods", "application/oleobject");
        _mappings.put(".odt", "application/vnd.oasis.opendocument.text");
        _mappings.put(".one", "application/onenote");
        _mappings.put(".onea", "application/onenote");
        _mappings.put(".onepkg", "application/onenote");
        _mappings.put(".onetmp", "application/onenote");
        _mappings.put(".onetoc", "application/onenote");
        _mappings.put(".onetoc2", "application/onenote");
        _mappings.put(".orderedtest", "application/xml");
        _mappings.put(".osdx", "application/opensearchdescription+xml");
        _mappings.put(".p10", "application/pkcs10");
        _mappings.put(".p12", "application/x-pkcs12");
        _mappings.put(".p7b", "application/x-pkcs7-certificates");
        _mappings.put(".p7c", "application/pkcs7-mime");
        _mappings.put(".p7m", "application/pkcs7-mime");
        _mappings.put(".p7r", "application/x-pkcs7-certreqresp");
        _mappings.put(".p7s", "application/pkcs7-signature");
        _mappings.put(".pbm", "image/x-portable-bitmap");
        _mappings.put(".pcast", "application/x-podcast");
        _mappings.put(".pct", "image/pict");
        _mappings.put(".pcx", "application/octet-stream");
        _mappings.put(".pcz", "application/octet-stream");
        _mappings.put(".pdf", "application/pdf");
        _mappings.put(".pfb", "application/octet-stream");
        _mappings.put(".pfm", "application/octet-stream");
        _mappings.put(".pfx", "application/x-pkcs12");
        _mappings.put(".pgm", "image/x-portable-graymap");
        _mappings.put(".pic", "image/pict");
        _mappings.put(".pict", "image/pict");
        _mappings.put(".pkgdef", "text/plain");
        _mappings.put(".pkgundef", "text/plain");
        _mappings.put(".pko", "application/vnd.ms-pki.pko");
        _mappings.put(".pls", "audio/scpls");
        _mappings.put(".pma", "application/x-perfmon");
        _mappings.put(".pmc", "application/x-perfmon");
        _mappings.put(".pml", "application/x-perfmon");
        _mappings.put(".pmr", "application/x-perfmon");
        _mappings.put(".pmw", "application/x-perfmon");
        _mappings.put(".png", "image/png");
        _mappings.put(".pnm", "image/x-portable-anymap");
        _mappings.put(".pnt", "image/x-macpaint");
        _mappings.put(".pntg", "image/x-macpaint");
        _mappings.put(".pnz", "image/png");
        _mappings.put(".pot", "application/vnd.ms-powerpoint");
        _mappings.put(".potm", "application/vnd.ms-powerpoint.template.macroEnabled.12");
        _mappings.put(".potx", "application/vnd.openxmlformats-officedocument.presentationml.template");
        _mappings.put(".ppa", "application/vnd.ms-powerpoint");
        _mappings.put(".ppam", "application/vnd.ms-powerpoint.addin.macroEnabled.12");
        _mappings.put(".ppm", "image/x-portable-pixmap");
        _mappings.put(".pps", "application/vnd.ms-powerpoint");
        _mappings.put(".ppsm", "application/vnd.ms-powerpoint.slideshow.macroEnabled.12");
        _mappings.put(".ppsx", "application/vnd.openxmlformats-officedocument.presentationml.slideshow");
        _mappings.put(".ppt", "application/vnd.ms-powerpoint");
        _mappings.put(".pptm", "application/vnd.ms-powerpoint.presentation.macroEnabled.12");
        _mappings.put(".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        _mappings.put(".prf", "application/pics-rules");
        _mappings.put(".prm", "application/octet-stream");
        _mappings.put(".prx", "application/octet-stream");
        _mappings.put(".ps", "application/postscript");
        _mappings.put(".psc1", "application/PowerShell");
        _mappings.put(".psd", "application/octet-stream");
        _mappings.put(".psess", "application/xml");
        _mappings.put(".psm", "application/octet-stream");
        _mappings.put(".psp", "application/octet-stream");
        _mappings.put(".pub", "application/x-mspublisher");
        _mappings.put(".pwz", "application/vnd.ms-powerpoint");
        _mappings.put(".qht", "text/x-html-insertion");
        _mappings.put(".qhtm", "text/x-html-insertion");
        _mappings.put(".qt", "video/quicktime");
        _mappings.put(".qti", "image/x-quicktime");
        _mappings.put(".qtif", "image/x-quicktime");
        _mappings.put(".qtl", "application/x-quicktimeplayer");
        _mappings.put(".qxd", "application/octet-stream");
        _mappings.put(".ra", "audio/x-pn-realaudio");
        _mappings.put(".ram", "audio/x-pn-realaudio");
        _mappings.put(".rar", "application/octet-stream");
        _mappings.put(".ras", "image/x-cmu-raster");
        _mappings.put(".rat", "application/rat-file");
        _mappings.put(".rc", "text/plain");
        _mappings.put(".rc2", "text/plain");
        _mappings.put(".rct", "text/plain");
        _mappings.put(".rdlc", "application/xml");
        _mappings.put(".resx", "application/xml");
        _mappings.put(".rf", "image/vnd.rn-realflash");
        _mappings.put(".rgb", "image/x-rgb");
        _mappings.put(".rgs", "text/plain");
        _mappings.put(".rm", "application/vnd.rn-realmedia");
        _mappings.put(".rmi", "audio/mid");
        _mappings.put(".rmp", "application/vnd.rn-rn_music_package");
        _mappings.put(".roff", "application/x-troff");
        _mappings.put(".rpm", "audio/x-pn-realaudio-plugin");
        _mappings.put(".rqy", "text/x-ms-rqy");
        _mappings.put(".rtf", "application/rtf");
        _mappings.put(".rtx", "text/richtext");
        _mappings.put(".ruleset", "application/xml");
        _mappings.put(".s", "text/plain");
        _mappings.put(".safariextz", "application/x-safari-safariextz");
        _mappings.put(".scd", "application/x-msschedule");
        _mappings.put(".sct", "text/scriptlet");
        _mappings.put(".sd2", "audio/x-sd2");
        _mappings.put(".sdp", "application/sdp");
        _mappings.put(".sea", "application/octet-stream");
        _mappings.put(".searchConnector-ms", "application/windows-search-connector+xml");
        _mappings.put(".setpay", "application/set-payment-initiation");
        _mappings.put(".setreg", "application/set-registration-initiation");
        _mappings.put(".settings", "application/xml");
        _mappings.put(".sgimb", "application/x-sgimb");
        _mappings.put(".sgml", "text/sgml");
        _mappings.put(".sh", "application/x-sh");
        _mappings.put(".shar", "application/x-shar");
        _mappings.put(".shtml", "text/html");
        _mappings.put(".sit", "application/x-stuffit");
        _mappings.put(".sitemap", "application/xml");
        _mappings.put(".skin", "application/xml");
        _mappings.put(".sldm", "application/vnd.ms-powerpoint.slide.macroEnabled.12");
        _mappings.put(".sldx", "application/vnd.openxmlformats-officedocument.presentationml.slide");
        _mappings.put(".slk", "application/vnd.ms-excel");
        _mappings.put(".sln", "text/plain");
        _mappings.put(".slupkg-ms", "application/x-ms-license");
        _mappings.put(".smd", "audio/x-smd");
        _mappings.put(".smi", "application/octet-stream");
        _mappings.put(".smx", "audio/x-smd");
        _mappings.put(".smz", "audio/x-smd");
        _mappings.put(".snd", "audio/basic");
        _mappings.put(".snippet", "application/xml");
        _mappings.put(".snp", "application/octet-stream");
        _mappings.put(".sol", "text/plain");
        _mappings.put(".sor", "text/plain");
        _mappings.put(".spc", "application/x-pkcs7-certificates");
        _mappings.put(".spl", "application/futuresplash");
        _mappings.put(".src", "application/x-wais-source");
        _mappings.put(".srf", "text/plain");
        _mappings.put(".SSISDeploymentManifest", "text/xml");
        _mappings.put(".ssm", "application/streamingmedia");
        _mappings.put(".sst", "application/vnd.ms-pki.certstore");
        _mappings.put(".stl", "application/vnd.ms-pki.stl");
        _mappings.put(".sv4cpio", "application/x-sv4cpio");
        _mappings.put(".sv4crc", "application/x-sv4crc");
        _mappings.put(".svc", "application/xml");
        _mappings.put(".swf", "application/x-shockwave-flash");
        _mappings.put(".t", "application/x-troff");
        _mappings.put(".tar", "application/x-tar");
        _mappings.put(".tcl", "application/x-tcl");
        _mappings.put(".testrunconfig", "application/xml");
        _mappings.put(".testsettings", "application/xml");
        _mappings.put(".tex", "application/x-tex");
        _mappings.put(".texi", "application/x-texinfo");
        _mappings.put(".texinfo", "application/x-texinfo");
        _mappings.put(".tgz", "application/x-compressed");
        _mappings.put(".thmx", "application/vnd.ms-officetheme");
        _mappings.put(".thn", "application/octet-stream");
        _mappings.put(".tif", "image/tiff");
        _mappings.put(".tiff", "image/tiff");
        _mappings.put(".tlh", "text/plain");
        _mappings.put(".tli", "text/plain");
        _mappings.put(".toc", "application/octet-stream");
        _mappings.put(".tr", "application/x-troff");
        _mappings.put(".trm", "application/x-msterminal");
        _mappings.put(".trx", "application/xml");
        _mappings.put(".ts", "video/vnd.dlna.mpeg-tts");
        _mappings.put(".tsv", "text/tab-separated-values");
        _mappings.put(".ttf", "application/octet-stream");
        _mappings.put(".tts", "video/vnd.dlna.mpeg-tts");
        _mappings.put(".txt", "text/plain");
        _mappings.put(".u32", "application/octet-stream");
        _mappings.put(".uls", "text/iuls");
        _mappings.put(".user", "text/plain");
        _mappings.put(".ustar", "application/x-ustar");
        _mappings.put(".vb", "text/plain");
        _mappings.put(".vbdproj", "text/plain");
        _mappings.put(".vbk", "video/mpeg");
        _mappings.put(".vbproj", "text/plain");
        _mappings.put(".vbs", "text/vbscript");
        _mappings.put(".vcf", "text/x-vcard");
        _mappings.put(".vcproj", "Application/xml");
        _mappings.put(".vcs", "text/plain");
        _mappings.put(".vcxproj", "Application/xml");
        _mappings.put(".vddproj", "text/plain");
        _mappings.put(".vdp", "text/plain");
        _mappings.put(".vdproj", "text/plain");
        _mappings.put(".vdx", "application/vnd.ms-visio.viewer");
        _mappings.put(".vml", "text/xml");
        _mappings.put(".vscontent", "application/xml");
        _mappings.put(".vsct", "text/xml");
        _mappings.put(".vsd", "application/vnd.visio");
        _mappings.put(".vsi", "application/ms-vsi");
        _mappings.put(".vsix", "application/vsix");
        _mappings.put(".vsixlangpack", "text/xml");
        _mappings.put(".vsixmanifest", "text/xml");
        _mappings.put(".vsmdi", "application/xml");
        _mappings.put(".vspscc", "text/plain");
        _mappings.put(".vss", "application/vnd.visio");
        _mappings.put(".vsscc", "text/plain");
        _mappings.put(".vssettings", "text/xml");
        _mappings.put(".vssscc", "text/plain");
        _mappings.put(".vst", "application/vnd.visio");
        _mappings.put(".vstemplate", "text/xml");
        _mappings.put(".vsto", "application/x-ms-vsto");
        _mappings.put(".vsw", "application/vnd.visio");
        _mappings.put(".vsx", "application/vnd.visio");
        _mappings.put(".vtx", "application/vnd.visio");
        _mappings.put(".wav", "audio/wav");
        _mappings.put(".wave", "audio/wav");
        _mappings.put(".wax", "audio/x-ms-wax");
        _mappings.put(".wbk", "application/msword");
        _mappings.put(".wbmp", "image/vnd.wap.wbmp");
        _mappings.put(".wcm", "application/vnd.ms-works");
        _mappings.put(".wdb", "application/vnd.ms-works");
        _mappings.put(".wdp", "image/vnd.ms-photo");
        _mappings.put(".webarchive", "application/x-safari-webarchive");
        _mappings.put(".webtest", "application/xml");
        _mappings.put(".wiq", "application/xml");
        _mappings.put(".wiz", "application/msword");
        _mappings.put(".wks", "application/vnd.ms-works");
        _mappings.put(".WLMP", "application/wlmoviemaker");
        _mappings.put(".wlpginstall", "application/x-wlpg-detect");
        _mappings.put(".wlpginstall3", "application/x-wlpg3-detect");
        _mappings.put(".wm", "video/x-ms-wm");
        _mappings.put(".wma", "audio/x-ms-wma");
        _mappings.put(".wmd", "application/x-ms-wmd");
        _mappings.put(".wmf", "application/x-msmetafile");
        _mappings.put(".wml", "text/vnd.wap.wml");
        _mappings.put(".wmlc", "application/vnd.wap.wmlc");
        _mappings.put(".wmls", "text/vnd.wap.wmlscript");
        _mappings.put(".wmlsc", "application/vnd.wap.wmlscriptc");
        _mappings.put(".wmp", "video/x-ms-wmp");
        _mappings.put(".wmv", "video/x-ms-wmv");
        _mappings.put(".wmx", "video/x-ms-wmx");
        _mappings.put(".wmz", "application/x-ms-wmz");
        _mappings.put(".wpl", "application/vnd.ms-wpl");
        _mappings.put(".wps", "application/vnd.ms-works");
        _mappings.put(".wri", "application/x-mswrite");
        _mappings.put(".wrl", "x-world/x-vrml");
        _mappings.put(".wrz", "x-world/x-vrml");
        _mappings.put(".wsc", "text/scriptlet");
        _mappings.put(".wsdl", "text/xml");
        _mappings.put(".wvx", "video/x-ms-wvx");
        _mappings.put(".x", "application/directx");
        _mappings.put(".xaf", "x-world/x-vrml");
        _mappings.put(".xaml", "application/xaml+xml");
        _mappings.put(".xap", "application/x-silverlight-app");
        _mappings.put(".xbap", "application/x-ms-xbap");
        _mappings.put(".xbm", "image/x-xbitmap");
        _mappings.put(".xdr", "text/plain");
        _mappings.put(".xht", "application/xhtml+xml");
        _mappings.put(".xhtml", "application/xhtml+xml");
        _mappings.put(".xla", "application/vnd.ms-excel");
        _mappings.put(".xlam", "application/vnd.ms-excel.addin.macroEnabled.12");
        _mappings.put(".xlc", "application/vnd.ms-excel");
        _mappings.put(".xld", "application/vnd.ms-excel");
        _mappings.put(".xlk", "application/vnd.ms-excel");
        _mappings.put(".xll", "application/vnd.ms-excel");
        _mappings.put(".xlm", "application/vnd.ms-excel");
        _mappings.put(".xls", "application/vnd.ms-excel");
        _mappings.put(".xlsb", "application/vnd.ms-excel.sheet.binary.macroEnabled.12");
        _mappings.put(".xlsm", "application/vnd.ms-excel.sheet.macroEnabled.12");
        _mappings.put(".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        _mappings.put(".xlt", "application/vnd.ms-excel");
        _mappings.put(".xltm", "application/vnd.ms-excel.template.macroEnabled.12");
        _mappings.put(".xltx", "application/vnd.openxmlformats-officedocument.spreadsheetml.template");
        _mappings.put(".xlw", "application/vnd.ms-excel");
        _mappings.put(".xml", "text/xml");
        _mappings.put(".xmta", "application/xml");
        _mappings.put(".xof", "x-world/x-vrml");
        _mappings.put(".XOML", "text/plain");
        _mappings.put(".xpm", "image/x-xpixmap");
        _mappings.put(".xps", "application/vnd.ms-xpsdocument");
        _mappings.put(".xrm-ms", "text/xml");
        _mappings.put(".xsc", "application/xml");
        _mappings.put(".xsd", "text/xml");
        _mappings.put(".xsf", "text/xml");
        _mappings.put(".xsl", "text/xml");
        _mappings.put(".xslt", "text/xml");
        _mappings.put(".xsn", "application/octet-stream");
        _mappings.put(".xss", "application/xml");
        _mappings.put(".xtp", "application/octet-stream");
        _mappings.put(".xwd", "image/x-xwindowdump");
        _mappings.put(".z", "application/x-compress");
        _mappings.put(".zip", "application/x-zip-compressed");
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API11_And_Above(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        String result = null;

        try {
            Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);

        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    /**
     * Returns an input stream based on given URI string.
     *
     * @param uriString the URI string from which to obtain the input stream
     * @param cordova the current application context
     * @return an input stream into the data at the given URI or null if given an invalid URI string
     * @throws IOException
     */
    public static InputStream getInputStreamFromUriString(String uriString, Context cordova)
            throws IOException {
        InputStream returnValue = null;
        if (uriString.startsWith("content")) {
            Uri uri = Uri.parse(uriString);
            returnValue = cordova.getContentResolver().openInputStream(uri);
        } else if (uriString.startsWith("file://")) {
            int question = uriString.indexOf("?");
            if (question > -1) {
                uriString = uriString.substring(0, question);
            }
            if (uriString.startsWith("file:///android_asset/")) {
                Uri uri = Uri.parse(uriString);
                String relativePath = uri.getPath().substring(15);
                returnValue = cordova.getAssets().open(relativePath);
            } else {
                // might still be content so try that first
                try {
                    returnValue = cordova.getContentResolver().openInputStream(Uri.parse(uriString));
                } catch (Exception e) {
                    returnValue = null;
                }

            }
        } else {
            returnValue = new FileInputStream(uriString);
        }
        return returnValue;
    }

    /**
     * Removes the "file://" prefix from the given URI string, if applicable.
     * If the given URI string doesn't have a "file://" prefix, it is returned unchanged.
     *
     * @param uriString the URI string to operate on
     * @return a path without the "file://" prefix
     */
    public static String stripFileProtocol(String uriString) {
        if (uriString.startsWith("file://")) {
            uriString = uriString.substring(7);
        }
        return uriString;
    }

    public static String getMimeTypeForExtension(String path) {
        String extension = path;
        int lastDot = extension.lastIndexOf('.');
        if (lastDot != -1) {
            extension = extension.substring(lastDot + 1);
        }
        // Convert the URI string to lower case to ensure compatibility with MimeTypeMap (see CB-2185).
        extension = extension.toLowerCase(Locale.getDefault());
        if (extension.equals("3ga")) {
            return "audio/3gpp";
        }
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    /**
     * Returns the mime type of the data specified by the given URI string.
     *
     * @param uriString the URI string of the data
     * @return the mime type of the specified data
     */
    public static String getMimeType(String uriString, Context cordova) {
        String mimeType = null;

        Uri uri = Uri.parse(uriString);
        if (uriString.startsWith("content://")) {
            mimeType = cordova.getContentResolver().getType(uri);
        } else {
            mimeType = getMimeTypeForExtension(uri.getPath());
        }

        return mimeType;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     * @author paulburke
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {

                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     * @author paulburke
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     * @author paulburke
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     * @author paulburke
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    private static Map<String, String> _mappings = new HashMap<String,String>(){{
        put(".323", "text/h323");
        put(".3g2", "video/3gpp2");
        put(".3gp", "video/3gpp");
        put(".3gp2", "video/3gpp2");
        put(".3gpp", "video/3gpp");
        put(".7z", "application/x-7z-compressed");
        put(".aa", "audio/audible");
        put(".AAC", "audio/aac");
        put(".aaf", "application/octet-stream");
        put(".aax", "audio/vnd.audible.aax");
        put(".ac3", "audio/ac3");
        put(".aca", "application/octet-stream");
        put(".accda", "application/msaccess.addin");
        put(".accdb", "application/msaccess");
        put(".accdc", "application/msaccess.cab");
        put(".accde", "application/msaccess");
        put(".accdr", "application/msaccess.runtime");
        put(".accdt", "application/msaccess");
        put(".accdw", "application/msaccess.webapplication");
        put(".accft", "application/msaccess.ftemplate");
        put(".acx", "application/internet-property-stream");
        put(".AddIn", "text/xml");
        put(".ade", "application/msaccess");
        put(".adobebridge", "application/x-bridge-url");
        put(".adp", "application/msaccess");
        put(".ADT", "audio/vnd.dlna.adts");
        put(".ADTS", "audio/aac");
        put(".afm", "application/octet-stream");
        put(".ai", "application/postscript");
        put(".aif", "audio/x-aiff");
        put(".aifc", "audio/aiff");
        put(".aiff", "audio/aiff");
        put(".air", "application/vnd.adobe.air-application-installer-package+zip");
        put(".amc", "application/x-mpeg");
        put(".application", "application/x-ms-application");
        put(".art", "image/x-jg");
        put(".asa", "application/xml");
        put(".asax", "application/xml");
        put(".ascx", "application/xml");
        put(".asd", "application/octet-stream");
        put(".asf", "video/x-ms-asf");
        put(".ashx", "application/xml");
        put(".asi", "application/octet-stream");
        put(".asm", "text/plain");
        put(".asmx", "application/xml");
        put(".aspx", "application/xml");
        put(".asr", "video/x-ms-asf");
        put(".asx", "video/x-ms-asf");
        put(".atom", "application/atom+xml");
        put(".au", "audio/basic");
        put(".avi", "video/x-msvideo");
        put(".axs", "application/olescript");
        put(".bas", "text/plain");
        put(".bcpio", "application/x-bcpio");
        put(".bin", "application/octet-stream");
        put(".bmp", "image/bmp");
        put(".c", "text/plain");
        put(".cab", "application/octet-stream");
        put(".caf", "audio/x-caf");
        put(".calx", "application/vnd.ms-office.calx");
        put(".cat", "application/vnd.ms-pki.seccat");
        put(".cc", "text/plain");
        put(".cd", "text/plain");
        put(".cdda", "audio/aiff");
        put(".cdf", "application/x-cdf");
        put(".cer", "application/x-x509-ca-cert");
        put(".chm", "application/octet-stream");
        put(".class", "application/x-java-applet");
        put(".clp", "application/x-msclip");
        put(".cmx", "image/x-cmx");
        put(".cnf", "text/plain");
        put(".cod", "image/cis-cod");
        put(".config", "application/xml");
        put(".contact", "text/x-ms-contact");
        put(".coverage", "application/xml");
        put(".cpio", "application/x-cpio");
        put(".cpp", "text/plain");
        put(".crd", "application/x-mscardfile");
        put(".crl", "application/pkix-crl");
        put(".crt", "application/x-x509-ca-cert");
        put(".cs", "text/plain");
        put(".csdproj", "text/plain");
        put(".csh", "application/x-csh");
        put(".csproj", "text/plain");
        put(".css", "text/css");
        put(".csv", "text/csv");
        put(".cur", "application/octet-stream");
        put(".cxx", "text/plain");
        put(".dat", "application/octet-stream");
        put(".datasource", "application/xml");
        put(".dbproj", "text/plain");
        put(".dcr", "application/x-director");
        put(".def", "text/plain");
        put(".deploy", "application/octet-stream");
        put(".der", "application/x-x509-ca-cert");
        put(".dgml", "application/xml");
        put(".dib", "image/bmp");
        put(".dif", "video/x-dv");
        put(".dir", "application/x-director");
        put(".disco", "text/xml");
        put(".dll", "application/x-msdownload");
        put(".dll.config", "text/xml");
        put(".dlm", "text/dlm");
        put(".doc", "application/msword");
        put(".docm", "application/vnd.ms-word.document.macroEnabled.12");
        put(".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        put(".dot", "application/msword");
        put(".dotm", "application/vnd.ms-word.template.macroEnabled.12");
        put(".dotx", "application/vnd.openxmlformats-officedocument.wordprocessingml.template");
        put(".dsp", "application/octet-stream");
        put(".dsw", "text/plain");
        put(".dtd", "text/xml");
        put(".dtsConfig", "text/xml");
        put(".dv", "video/x-dv");
        put(".dvi", "application/x-dvi");
        put(".dwf", "drawing/x-dwf");
        put(".dwp", "application/octet-stream");
        put(".dxr", "application/x-director");
        put(".eml", "message/rfc822");
        put(".emz", "application/octet-stream");
        put(".eot", "application/octet-stream");
        put(".eps", "application/postscript");
        put(".etl", "application/etl");
        put(".etx", "text/x-setext");
        put(".evy", "application/envoy");
        put(".exe", "application/octet-stream");
        put(".exe.config", "text/xml");
        put(".fdf", "application/vnd.fdf");
        put(".fif", "application/fractals");
        put(".filters", "Application/xml");
        put(".fla", "application/octet-stream");
        put(".flr", "x-world/x-vrml");
        put(".flv", "video/x-flv");
        put(".fsscript", "application/fsharp-script");
        put(".fsx", "application/fsharp-script");
        put(".generictest", "application/xml");
        put(".gif", "image/gif");
        put(".group", "text/x-ms-group");
        put(".gsm", "audio/x-gsm");
        put(".gtar", "application/x-gtar");
        put(".gz", "application/x-gzip");
        put(".h", "text/plain");
        put(".hdf", "application/x-hdf");
        put(".hdml", "text/x-hdml");
        put(".hhc", "application/x-oleobject");
        put(".hhk", "application/octet-stream");
        put(".hhp", "application/octet-stream");
        put(".hlp", "application/winhlp");
        put(".hpp", "text/plain");
        put(".hqx", "application/mac-binhex40");
        put(".hta", "application/hta");
        put(".htc", "text/x-component");
        put(".htm", "text/html");
        put(".html", "text/html");
        put(".htt", "text/webviewhtml");
        put(".hxa", "application/xml");
        put(".hxc", "application/xml");
        put(".hxd", "application/octet-stream");
        put(".hxe", "application/xml");
        put(".hxf", "application/xml");
        put(".hxh", "application/octet-stream");
        put(".hxi", "application/octet-stream");
        put(".hxk", "application/xml");
        put(".hxq", "application/octet-stream");
        put(".hxr", "application/octet-stream");
        put(".hxs", "application/octet-stream");
        put(".hxt", "text/html");
        put(".hxv", "application/xml");
        put(".hxw", "application/octet-stream");
        put(".hxx", "text/plain");
        put(".i", "text/plain");
        put(".ico", "image/x-icon");
        put(".ics", "application/octet-stream");
        put(".idl", "text/plain");
        put(".ief", "image/ief");
        put(".iii", "application/x-iphone");
        put(".inc", "text/plain");
        put(".inf", "application/octet-stream");
        put(".inl", "text/plain");
        put(".ins", "application/x-internet-signup");
        put(".ipa", "application/x-itunes-ipa");
        put(".ipg", "application/x-itunes-ipg");
        put(".ipproj", "text/plain");
        put(".ipsw", "application/x-itunes-ipsw");
        put(".iqy", "text/x-ms-iqy");
        put(".isp", "application/x-internet-signup");
        put(".ite", "application/x-itunes-ite");
        put(".itlp", "application/x-itunes-itlp");
        put(".itms", "application/x-itunes-itms");
        put(".itpc", "application/x-itunes-itpc");
        put(".IVF", "video/x-ivf");
        put(".jar", "application/java-archive");
        put(".java", "application/octet-stream");
        put(".jck", "application/liquidmotion");
        put(".jcz", "application/liquidmotion");
        put(".jfif", "image/pjpeg");
        put(".jnlp", "application/x-java-jnlp-file");
        put(".jpb", "application/octet-stream");
        put(".jpe", "image/jpeg");
        put(".jpeg", "image/jpeg");
        put(".jpg", "image/jpeg");
        put(".js", "application/x-javascript");
        put(".json", "application/json");
        put(".jsx", "text/jscript");
        put(".jsxbin", "text/plain");
        put(".latex", "application/x-latex");
        put(".library-ms", "application/windows-library+xml");
        put(".lit", "application/x-ms-reader");
        put(".loadtest", "application/xml");
        put(".lpk", "application/octet-stream");
        put(".lsf", "video/x-la-asf");
        put(".lst", "text/plain");
        put(".lsx", "video/x-la-asf");
        put(".lzh", "application/octet-stream");
        put(".m13", "application/x-msmediaview");
        put(".m14", "application/x-msmediaview");
        put(".m1v", "video/mpeg");
        put(".m2t", "video/vnd.dlna.mpeg-tts");
        put(".m2ts", "video/vnd.dlna.mpeg-tts");
        put(".m2v", "video/mpeg");
        put(".m3u", "audio/x-mpegurl");
        put(".m3u8", "audio/x-mpegurl");
        put(".m4a", "audio/m4a");
        put(".m4b", "audio/m4b");
        put(".m4p", "audio/m4p");
        put(".m4r", "audio/x-m4r");
        put(".m4v", "video/x-m4v");
        put(".mac", "image/x-macpaint");
        put(".mak", "text/plain");
        put(".man", "application/x-troff-man");
        put(".manifest", "application/x-ms-manifest");
        put(".map", "text/plain");
        put(".master", "application/xml");
        put(".mda", "application/msaccess");
        put(".mdb", "application/x-msaccess");
        put(".mde", "application/msaccess");
        put(".mdp", "application/octet-stream");
        put(".me", "application/x-troff-me");
        put(".mfp", "application/x-shockwave-flash");
        put(".mht", "message/rfc822");
        put(".mhtml", "message/rfc822");
        put(".mid", "audio/mid");
        put(".midi", "audio/mid");
        put(".mix", "application/octet-stream");
        put(".mk", "text/plain");
        put(".mmf", "application/x-smaf");
        put(".mno", "text/xml");
        put(".mny", "application/x-msmoney");
        put(".mod", "video/mpeg");
        put(".mov", "video/quicktime");
        put(".movie", "video/x-sgi-movie");
        put(".mp2", "video/mpeg");
        put(".mp2v", "video/mpeg");
        put(".mp3", "audio/mpeg");
        put(".mp4", "video/mp4");
        put(".mp4v", "video/mp4");
        put(".mpa", "video/mpeg");
        put(".mpe", "video/mpeg");
        put(".mpeg", "video/mpeg");
        put(".mpf", "application/vnd.ms-mediapackage");
        put(".mpg", "video/mpeg");
        put(".mpp", "application/vnd.ms-project");
        put(".mpv2", "video/mpeg");
        put(".mqv", "video/quicktime");
        put(".ms", "application/x-troff-ms");
        put(".msi", "application/octet-stream");
        put(".mso", "application/octet-stream");
        put(".mts", "video/vnd.dlna.mpeg-tts");
        put(".mtx", "application/xml");
        put(".mvb", "application/x-msmediaview");
        put(".mvc", "application/x-miva-compiled");
        put(".mxp", "application/x-mmxp");
        put(".nc", "application/x-netcdf");
        put(".nsc", "video/x-ms-asf");
        put(".nws", "message/rfc822");
        put(".ocx", "application/octet-stream");
        put(".oda", "application/oda");
        put(".odc", "text/x-ms-odc");
        put(".odh", "text/plain");
        put(".odl", "text/plain");
        put(".odp", "application/vnd.oasis.opendocument.presentation");
        put(".ods", "application/oleobject");
        put(".odt", "application/vnd.oasis.opendocument.text");
        put(".one", "application/onenote");
        put(".onea", "application/onenote");
        put(".onepkg", "application/onenote");
        put(".onetmp", "application/onenote");
        put(".onetoc", "application/onenote");
        put(".onetoc2", "application/onenote");
        put(".orderedtest", "application/xml");
        put(".osdx", "application/opensearchdescription+xml");
        put(".p10", "application/pkcs10");
        put(".p12", "application/x-pkcs12");
        put(".p7b", "application/x-pkcs7-certificates");
        put(".p7c", "application/pkcs7-mime");
        put(".p7m", "application/pkcs7-mime");
        put(".p7r", "application/x-pkcs7-certreqresp");
        put(".p7s", "application/pkcs7-signature");
        put(".pbm", "image/x-portable-bitmap");
        put(".pcast", "application/x-podcast");
        put(".pct", "image/pict");
        put(".pcx", "application/octet-stream");
        put(".pcz", "application/octet-stream");
        put(".pdf", "application/pdf");
        put(".pfb", "application/octet-stream");
        put(".pfm", "application/octet-stream");
        put(".pfx", "application/x-pkcs12");
        put(".pgm", "image/x-portable-graymap");
        put(".pic", "image/pict");
        put(".pict", "image/pict");
        put(".pkgdef", "text/plain");
        put(".pkgundef", "text/plain");
        put(".pko", "application/vnd.ms-pki.pko");
        put(".pls", "audio/scpls");
        put(".pma", "application/x-perfmon");
        put(".pmc", "application/x-perfmon");
        put(".pml", "application/x-perfmon");
        put(".pmr", "application/x-perfmon");
        put(".pmw", "application/x-perfmon");
        put(".png", "image/png");
        put(".pnm", "image/x-portable-anymap");
        put(".pnt", "image/x-macpaint");
        put(".pntg", "image/x-macpaint");
        put(".pnz", "image/png");
        put(".pot", "application/vnd.ms-powerpoint");
        put(".potm", "application/vnd.ms-powerpoint.template.macroEnabled.12");
        put(".potx", "application/vnd.openxmlformats-officedocument.presentationml.template");
        put(".ppa", "application/vnd.ms-powerpoint");
        put(".ppam", "application/vnd.ms-powerpoint.addin.macroEnabled.12");
        put(".ppm", "image/x-portable-pixmap");
        put(".pps", "application/vnd.ms-powerpoint");
        put(".ppsm", "application/vnd.ms-powerpoint.slideshow.macroEnabled.12");
        put(".ppsx", "application/vnd.openxmlformats-officedocument.presentationml.slideshow");
        put(".ppt", "application/vnd.ms-powerpoint");
        put(".pptm", "application/vnd.ms-powerpoint.presentation.macroEnabled.12");
        put(".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        put(".prf", "application/pics-rules");
        put(".prm", "application/octet-stream");
        put(".prx", "application/octet-stream");
        put(".ps", "application/postscript");
        put(".psc1", "application/PowerShell");
        put(".psd", "application/octet-stream");
        put(".psess", "application/xml");
        put(".psm", "application/octet-stream");
        put(".psp", "application/octet-stream");
        put(".pub", "application/x-mspublisher");
        put(".pwz", "application/vnd.ms-powerpoint");
        put(".qht", "text/x-html-insertion");
        put(".qhtm", "text/x-html-insertion");
        put(".qt", "video/quicktime");
        put(".qti", "image/x-quicktime");
        put(".qtif", "image/x-quicktime");
        put(".qtl", "application/x-quicktimeplayer");
        put(".qxd", "application/octet-stream");
        put(".ra", "audio/x-pn-realaudio");
        put(".ram", "audio/x-pn-realaudio");
        put(".rar", "application/octet-stream");
        put(".ras", "image/x-cmu-raster");
        put(".rat", "application/rat-file");
        put(".rc", "text/plain");
        put(".rc2", "text/plain");
        put(".rct", "text/plain");
        put(".rdlc", "application/xml");
        put(".resx", "application/xml");
        put(".rf", "image/vnd.rn-realflash");
        put(".rgb", "image/x-rgb");
        put(".rgs", "text/plain");
        put(".rm", "application/vnd.rn-realmedia");
        put(".rmi", "audio/mid");
        put(".rmp", "application/vnd.rn-rn_music_package");
        put(".roff", "application/x-troff");
        put(".rpm", "audio/x-pn-realaudio-plugin");
        put(".rqy", "text/x-ms-rqy");
        put(".rtf", "application/rtf");
        put(".rtx", "text/richtext");
        put(".ruleset", "application/xml");
        put(".s", "text/plain");
        put(".safariextz", "application/x-safari-safariextz");
        put(".scd", "application/x-msschedule");
        put(".sct", "text/scriptlet");
        put(".sd2", "audio/x-sd2");
        put(".sdp", "application/sdp");
        put(".sea", "application/octet-stream");
        put(".searchConnector-ms", "application/windows-search-connector+xml");
        put(".setpay", "application/set-payment-initiation");
        put(".setreg", "application/set-registration-initiation");
        put(".settings", "application/xml");
        put(".sgimb", "application/x-sgimb");
        put(".sgml", "text/sgml");
        put(".sh", "application/x-sh");
        put(".shar", "application/x-shar");
        put(".shtml", "text/html");
        put(".sit", "application/x-stuffit");
        put(".sitemap", "application/xml");
        put(".skin", "application/xml");
        put(".sldm", "application/vnd.ms-powerpoint.slide.macroEnabled.12");
        put(".sldx", "application/vnd.openxmlformats-officedocument.presentationml.slide");
        put(".slk", "application/vnd.ms-excel");
        put(".sln", "text/plain");
        put(".slupkg-ms", "application/x-ms-license");
        put(".smd", "audio/x-smd");
        put(".smi", "application/octet-stream");
        put(".smx", "audio/x-smd");
        put(".smz", "audio/x-smd");
        put(".snd", "audio/basic");
        put(".snippet", "application/xml");
        put(".snp", "application/octet-stream");
        put(".sol", "text/plain");
        put(".sor", "text/plain");
        put(".spc", "application/x-pkcs7-certificates");
        put(".spl", "application/futuresplash");
        put(".src", "application/x-wais-source");
        put(".srf", "text/plain");
        put(".SSISDeploymentManifest", "text/xml");
        put(".ssm", "application/streamingmedia");
        put(".sst", "application/vnd.ms-pki.certstore");
        put(".stl", "application/vnd.ms-pki.stl");
        put(".sv4cpio", "application/x-sv4cpio");
        put(".sv4crc", "application/x-sv4crc");
        put(".svc", "application/xml");
        put(".swf", "application/x-shockwave-flash");
        put(".t", "application/x-troff");
        put(".tar", "application/x-tar");
        put(".tcl", "application/x-tcl");
        put(".testrunconfig", "application/xml");
        put(".testsettings", "application/xml");
        put(".tex", "application/x-tex");
        put(".texi", "application/x-texinfo");
        put(".texinfo", "application/x-texinfo");
        put(".tgz", "application/x-compressed");
        put(".thmx", "application/vnd.ms-officetheme");
        put(".thn", "application/octet-stream");
        put(".tif", "image/tiff");
        put(".tiff", "image/tiff");
        put(".tlh", "text/plain");
        put(".tli", "text/plain");
        put(".toc", "application/octet-stream");
        put(".tr", "application/x-troff");
        put(".trm", "application/x-msterminal");
        put(".trx", "application/xml");
        put(".ts", "video/vnd.dlna.mpeg-tts");
        put(".tsv", "text/tab-separated-values");
        put(".ttf", "application/octet-stream");
        put(".tts", "video/vnd.dlna.mpeg-tts");
        put(".txt", "text/plain");
        put(".u32", "application/octet-stream");
        put(".uls", "text/iuls");
        put(".user", "text/plain");
        put(".ustar", "application/x-ustar");
        put(".vb", "text/plain");
        put(".vbdproj", "text/plain");
        put(".vbk", "video/mpeg");
        put(".vbproj", "text/plain");
        put(".vbs", "text/vbscript");
        put(".vcf", "text/x-vcard");
        put(".vcproj", "Application/xml");
        put(".vcs", "text/plain");
        put(".vcxproj", "Application/xml");
        put(".vddproj", "text/plain");
        put(".vdp", "text/plain");
        put(".vdproj", "text/plain");
        put(".vdx", "application/vnd.ms-visio.viewer");
        put(".vml", "text/xml");
        put(".vscontent", "application/xml");
        put(".vsct", "text/xml");
        put(".vsd", "application/vnd.visio");
        put(".vsi", "application/ms-vsi");
        put(".vsix", "application/vsix");
        put(".vsixlangpack", "text/xml");
        put(".vsixmanifest", "text/xml");
        put(".vsmdi", "application/xml");
        put(".vspscc", "text/plain");
        put(".vss", "application/vnd.visio");
        put(".vsscc", "text/plain");
        put(".vssettings", "text/xml");
        put(".vssscc", "text/plain");
        put(".vst", "application/vnd.visio");
        put(".vstemplate", "text/xml");
        put(".vsto", "application/x-ms-vsto");
        put(".vsw", "application/vnd.visio");
        put(".vsx", "application/vnd.visio");
        put(".vtx", "application/vnd.visio");
        put(".wav", "audio/wav");
        put(".wave", "audio/wav");
        put(".wax", "audio/x-ms-wax");
        put(".wbk", "application/msword");
        put(".wbmp", "image/vnd.wap.wbmp");
        put(".wcm", "application/vnd.ms-works");
        put(".wdb", "application/vnd.ms-works");
        put(".wdp", "image/vnd.ms-photo");
        put(".webarchive", "application/x-safari-webarchive");
        put(".webtest", "application/xml");
        put(".wiq", "application/xml");
        put(".wiz", "application/msword");
        put(".wks", "application/vnd.ms-works");
        put(".WLMP", "application/wlmoviemaker");
        put(".wlpginstall", "application/x-wlpg-detect");
        put(".wlpginstall3", "application/x-wlpg3-detect");
        put(".wm", "video/x-ms-wm");
        put(".wma", "audio/x-ms-wma");
        put(".wmd", "application/x-ms-wmd");
        put(".wmf", "application/x-msmetafile");
        put(".wml", "text/vnd.wap.wml");
        put(".wmlc", "application/vnd.wap.wmlc");
        put(".wmls", "text/vnd.wap.wmlscript");
        put(".wmlsc", "application/vnd.wap.wmlscriptc");
        put(".wmp", "video/x-ms-wmp");
        put(".wmv", "video/x-ms-wmv");
        put(".wmx", "video/x-ms-wmx");
        put(".wmz", "application/x-ms-wmz");
        put(".wpl", "application/vnd.ms-wpl");
        put(".wps", "application/vnd.ms-works");
        put(".wri", "application/x-mswrite");
        put(".wrl", "x-world/x-vrml");
        put(".wrz", "x-world/x-vrml");
        put(".wsc", "text/scriptlet");
        put(".wsdl", "text/xml");
        put(".wvx", "video/x-ms-wvx");
        put(".x", "application/directx");
        put(".xaf", "x-world/x-vrml");
        put(".xaml", "application/xaml+xml");
        put(".xap", "application/x-silverlight-app");
        put(".xbap", "application/x-ms-xbap");
        put(".xbm", "image/x-xbitmap");
        put(".xdr", "text/plain");
        put(".xht", "application/xhtml+xml");
        put(".xhtml", "application/xhtml+xml");
        put(".xla", "application/vnd.ms-excel");
        put(".xlam", "application/vnd.ms-excel.addin.macroEnabled.12");
        put(".xlc", "application/vnd.ms-excel");
        put(".xld", "application/vnd.ms-excel");
        put(".xlk", "application/vnd.ms-excel");
        put(".xll", "application/vnd.ms-excel");
        put(".xlm", "application/vnd.ms-excel");
        put(".xls", "application/vnd.ms-excel");
        put(".xlsb", "application/vnd.ms-excel.sheet.binary.macroEnabled.12");
        put(".xlsm", "application/vnd.ms-excel.sheet.macroEnabled.12");
        put(".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        put(".xlt", "application/vnd.ms-excel");
        put(".xltm", "application/vnd.ms-excel.template.macroEnabled.12");
        put(".xltx", "application/vnd.openxmlformats-officedocument.spreadsheetml.template");
        put(".xlw", "application/vnd.ms-excel");
        put(".xml", "text/xml");
        put(".xmta", "application/xml");
        put(".xof", "x-world/x-vrml");
        put(".XOML", "text/plain");
        put(".xpm", "image/x-xpixmap");
        put(".xps", "application/vnd.ms-xpsdocument");
        put(".xrm-ms", "text/xml");
        put(".xsc", "application/xml");
        put(".xsd", "text/xml");
        put(".xsf", "text/xml");
        put(".xsl", "text/xml");
        put(".xslt", "text/xml");
        put(".xsn", "application/octet-stream");
        put(".xss", "application/xml");
        put(".xtp", "application/octet-stream");
        put(".xwd", "image/x-xwindowdump");
        put(".z", "application/x-compress");
        put(".zip", "application/x-zip-compressed");
    }};

    public static String GetMimeType(String extension)
    {
        if (extension == null)
        {
            throw new NullPointerException("extension");
        }

        if (!extension.startsWith("."))
        {
            extension = "." + extension;
        }

        String mime=_mappings.get(extension.toLowerCase());

        return  mime!= null? mime : "application/octet-stream";
    }
}
