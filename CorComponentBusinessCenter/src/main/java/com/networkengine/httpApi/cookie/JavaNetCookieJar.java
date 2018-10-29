/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.networkengine.httpApi.cookie;

import com.networkengine.httpApi.Api;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.internal.platform.Platform;

import static okhttp3.internal.Util.delimiterOffset;
import static okhttp3.internal.Util.trimSubstring;
import static okhttp3.internal.platform.Platform.WARN;

/**
 * A cookie jar that delegates to a {@link java.net.CookieHandler}.
 */
public final class JavaNetCookieJar implements CookieJar {

    private static final CookieHandler cookieHandler
            = new CookieManager(Api.getInMemoryCookieStore()
            , CookiePolicy.ACCEPT_ORIGINAL_SERVER);

//    public JavaNetCookieJar(CookieHandler cookieHandler) {
//        this.cookieHandler = cookieHandler;
//    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookieHandler != null) {
            List<String> cookieStrings = new ArrayList<>();
            for (Cookie cookie : cookies) {
                cookieStrings.add(cookie.toString());
            }
            Map<String, List<String>> multimap = Collections.singletonMap("Set-Cookie", cookieStrings);
            try {
                cookieHandler.put(url.uri(), multimap);
            } catch (IOException e) {
                Platform.get().log(WARN, "Saving cookies failed for " + url.resolve("/..."), e);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        // The RI passes all headers. We don't have 'em, so we don't pass 'em!
        Map<String, List<String>> headers = Collections.emptyMap();
        Map<String, List<String>> cookieHeaders;
        try {
            List<HttpCookie> cookies = ((CookieManager) cookieHandler)
                    .getCookieStore().getCookies();

            cookieHeaders = getAllCookie(cookies);
            // cookieHeaders = cookieHandler.get(url.uri(), headers);
        } catch (Exception e) {
            Platform.get().log(WARN, "Loading cookies failed for " + url.resolve("/..."), e);
            return Collections.emptyList();
        }

        List<Cookie> cookies = null;
        for (Map.Entry<String, List<String>> entry : cookieHeaders.entrySet()) {
            String key = entry.getKey();
            if (("Cookie".equalsIgnoreCase(key) || "Cookie2".equalsIgnoreCase(key))
                    && !entry.getValue().isEmpty()) {
                for (String header : entry.getValue()) {
                    if (cookies == null) cookies = new ArrayList<>();
                    cookies.addAll(decodeHeaderAsJavaNetCookies(url, header));
                }
            }
        }

        return cookies != null
                ? Collections.unmodifiableList(cookies)
                : Collections.<Cookie>emptyList();
    }

    /**
     * Convert a request header to OkHttp's cookies via {@link HttpCookie}. That extra step handles
     * multiple cookies in a single request header, which {@link Cookie#parse} doesn't support.
     */
    private List<Cookie> decodeHeaderAsJavaNetCookies(HttpUrl url, String header) {
        List<Cookie> result = new ArrayList<>();
        for (int pos = 0, limit = header.length(), pairEnd; pos < limit; pos = pairEnd + 1) {
            pairEnd = delimiterOffset(header, pos, limit, ";,");
            int equalsSign = delimiterOffset(header, pos, pairEnd, '=');
            String name = trimSubstring(header, pos, equalsSign);
            if (name.startsWith("$")) continue;

            // We have either name=value or just a name.
            String value = equalsSign < pairEnd
                    ? trimSubstring(header, equalsSign + 1, pairEnd)
                    : "";

            // If the value is "quoted", drop the quotes.
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }

            result.add(new Cookie.Builder()
                    .name(name)
                    .value(value)
                    .domain(url.host())
                    .build());
        }
        return result;
    }

    private Map<String, List<String>> getAllCookie(List<HttpCookie> cookies) {
        Map<String, List<String>> cookieMap = new HashMap<String, List<String>>();

        if (cookies.isEmpty()) {
            return Collections.emptyMap();
        }

        // apply sort rule (RFC 2965 sec. 3.3.4)
        final StringBuilder result = new StringBuilder();

        // Netscape cookie spec and RFC 2965 have different format of Cookie
        // header; RFC 2965 requires a leading $Version="1" string while Netscape
        // does not.
        // The workaround here is to add a $Version="1" string in advance
        int minVersion = 1;
        for (HttpCookie cookie : cookies) {
            if (cookie.getVersion() < minVersion) {
                minVersion = cookie.getVersion();
            }
        }

        if (minVersion == 1) {
            result.append("$Version=\"1\"; ");
        }

        for (int i = 0; i < cookies.size(); ++i) {
            if (i != 0) {
                result.append("; ");
            }

            result.append(cookies.get(i).toString());
        }

        List<String> cookieHeader = new java.util.ArrayList<String>();
        cookieHeader.add(result.toString());

        cookieMap.put("Cookie", cookieHeader);
        return Collections.unmodifiableMap(cookieMap);
    }

}