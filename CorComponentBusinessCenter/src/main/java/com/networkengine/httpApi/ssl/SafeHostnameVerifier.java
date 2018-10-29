package com.networkengine.httpApi.ssl;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class SafeHostnameVerifier implements HostnameVerifier {

    @Override
    public boolean verify(String hostname, SSLSession session) {
        // TODO 对域名进行校验
        return true;
    }

}
