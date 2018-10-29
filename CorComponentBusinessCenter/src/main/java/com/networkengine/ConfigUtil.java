package com.networkengine;

import android.text.TextUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;

public class ConfigUtil {

    private static HashMap<String, String> configMap = new HashMap<>();

    public static void init(XmlPullParser parser) {

        HashMap<String, String> configMap = new HashMap<>();

        StringBuffer buffer = new StringBuffer();
        try {
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    buffer.append(parser.getName());
                    buffer.append('\n');
                    // buffer.append(parser.getAttributeCount());
                    int ac = parser.getAttributeCount();
                    if (ac > 0) {
                        initConfigMap(parser);
                    }
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        parser.close();
    }

    public static void initConfigMap(XmlPullParser parser) {

        int ac = parser.getAttributeCount();

        for (int i = 0; i < ac; i++) {
            configMap.put(parser.getAttributeName(i), parser.getAttributeValue(i));
        }
    }

    public static String getProt(String url) {

        if (TextUtils.isEmpty(url)) {

            return "";
        }

        String sp = "://";

        int PortIndex = url.indexOf(sp);

        if (PortIndex <= 0) {
            return "http";
        }

        return url.split(sp)[0];

    }

    public static String getHost(String url) {

        if (TextUtils.isEmpty(url)) {

            return "";
        }
//        http://172.16.23.119:8888/mchl/jsse

        String sp = "://";
        String end = "/";
        String port = ":";

        int hostBegin = url.indexOf(sp);

        String hostTmp = url;
        if (hostBegin > 0) {
            hostTmp = hostTmp.substring(hostBegin + sp.length());
        }

        int portBegin = hostTmp.indexOf(port);
        if (portBegin > 0) {

            return hostTmp.substring(0, portBegin);
        } else {
            int hostEnd = hostTmp.indexOf(end);
            if (hostEnd > 0) {
                return hostTmp.substring(0, hostEnd);
            }
            return hostTmp;
        }
    }

    /**
     * 解析出端口
     *
     * @param url
     * @return
     */
    public static String getPort(String url) {
        if (TextUtils.isEmpty(url)) {

            return "";
        }

        String sp = "://";
        String end = "/";
        String port = ":";

        int hostBegin = url.indexOf(sp);

        String hostTmp = url;
        if (hostBegin > 0) {
            hostTmp = hostTmp.substring(hostBegin + sp.length());
        }

        int portBegin = hostTmp.indexOf(port);
        if (portBegin < 0) {

            return "";
        } else {
            hostTmp = hostTmp.substring(portBegin + port.length());
            int hostEnd = hostTmp.indexOf(end);
            if (hostEnd > 0) {
                return hostTmp.substring(0, hostEnd);
            }
            return hostTmp;
        }

    }

    public static String getBaseUrl(String url) {
        if (TextUtils.isEmpty(url)) {

            return "";
        }

        //        http://172.16.23.119:8888/mchl/jsse

        String sp = "://";
        String end = "/";

        int hostBegin = url.indexOf(sp);

        String baseUrl = url;
        if (hostBegin > 0) {
            baseUrl = baseUrl.substring(hostBegin + sp.length());
        }

        int hostEnd = baseUrl.indexOf(end);
        if (hostEnd > 0) {
            baseUrl = baseUrl.substring(hostEnd);
            if (baseUrl.lastIndexOf("/") != baseUrl.length() - 1) {
                baseUrl = baseUrl + "/";
            }
            return baseUrl;
        }
        return "";

    }

    public static String getBaseUrlWithoutEnd(String url) {
        if (TextUtils.isEmpty(url)) {

            return "";
        }

        //        http://172.16.23.119:8888/mchl/jsse

        String sp = "://";
        String end = "/";

        int hostBegin = url.indexOf(sp);

        String baseUrl = url;
        if (hostBegin > 0) {
            baseUrl = baseUrl.substring(hostBegin + sp.length());
        }

        int hostEnd = baseUrl.indexOf(end);
        if (hostEnd > 0) {
            baseUrl = baseUrl.substring(hostEnd);
            return baseUrl;
        }
        return "";

    }

    public static String getMxmHost() {
        if (configMap == null) {
            return "";
        }
        if (!configMap.containsKey("mxm_host")) {
            if (configMap.containsKey("gw_host")) {
                String gw_host = configMap.get("gw_host");
                if (!TextUtils.isEmpty(gw_host)) {
                    return gw_host + "/mxm/";
                }
            }
        }
        return configMap.get("mxm_host");
    }

    public static void putConfigMap(String ley, String value) {
        if (configMap == null) {
            return;
        }
        if (TextUtils.isEmpty(ley)) {
            return;
        }
        if (TextUtils.isEmpty(value)) {
            return;
        }
        configMap.put(ley, value);
    }

    public static String getMchlHost() {
        if (configMap == null) {
            return "";
        }
        if (!configMap.containsKey("mchl_host")) {
            if (configMap.containsKey("gw_host")) {
                String gw_host = configMap.get("gw_host");
                if (!TextUtils.isEmpty(gw_host)) {
                    return gw_host + "/mchl/jsse/";
                }
            }
        }
        return configMap.get("mchl_host");
    }

    public static String getMqttServer() {
        if (configMap == null || !configMap.containsKey("mqtt_server")) {
            return "";
        }
        return configMap.get("mqtt_server");
    }

    public static String getPreviewHost() {
        if (configMap == null || !configMap.containsKey("file_preview_host")) {
            return "";
        }
        return configMap.get("file_preview_host");
    }

    public static String getRazorHost() {
        if (configMap == null || !configMap.containsKey("razor_host")) {
            return "";
        }
        return configMap.get("razor_host");
    }

    public static String getAppKey() {
        if (configMap == null || !configMap.containsKey("app_key")) {
            return "";
        }
        return configMap.get("app_key");
    }

    public static String getSingleWeb() {
        if (configMap == null || !configMap.containsKey("single_web")) {
            return "";
        }
        return configMap.get("single_web");
    }

    public static String getGwHost() {
        if (configMap == null || !configMap.containsKey("gw_host")) {
            return "";
        }
        String gw_host = configMap.get("gw_host");
        if ("http://".equals(gw_host) || "https://".equals(gw_host)) {
            return "";
        }

        return configMap.get("gw_host");
    }

    public static String getGWAccessToken() {
        if (configMap == null || !configMap.containsKey("gw_access_token")) {
            return "";
        }
        return configMap.get("gw_access_token");
    }

    public static String getGWDebug() {
        if (configMap == null || !configMap.containsKey("gw_debug")) {
            return "true";
        }
        return configMap.get("gw_debug");
    }

    public static String getEncrypt() {
        if (configMap == null || !configMap.containsKey("encrypt")) {
            return "false";
        }
        return configMap.get("encrypt");
    }


    public static String getMpmHost() {
        if (configMap == null || !configMap.containsKey("mpm_host")) {
            return "";
        }
        return configMap.get("mpm_host");
    }

    public static String getOAHost() {
        if (configMap == null && !configMap.containsKey("oa_host")) {
            return "";
        }
        return configMap.get("oa_host");
    }

    public static String getBackgroundColor() {
        if (configMap == null && !configMap.containsKey("background_color")) {
            return "";
        }
        return configMap.get("background_color");
    }

    public static String getForegroundColor() {
        if (configMap == null && !configMap.containsKey("foreground_color")) {
            return "";
        }
        return configMap.get("foreground_color");
    }

}
