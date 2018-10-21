package com.markLove.Xplan.api.util;

import android.util.Log;

import com.markLove.Xplan.base.App;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by huanglingjun on 2018/5/15.
 */

public class OkHttpUtil {

    private static OkHttpClient mOkHttpClient;

//    //设置缓存目录
//    private static final File cacheDirectory = new File(MyApplication.getMyApplication().getCacheDir().getAbsolutePath(), "httpCache");
//
//    private static Cache cache = new Cache(cacheDirectory, 10 * 1024 * 1024);
//
//    //请求拦截
//    private static RequestInterceptor requestInterceptor = new RequestInterceptor();
//
//    //响应拦截
//    private static ResponseInterceptor responseInterceptor = new ResponseInterceptor();


    public static OkHttpClient getOkHttpClient() {
        if (null == mOkHttpClient) {
            mOkHttpClient = new OkHttpClient.Builder()
//                    .cookieJar(CookieJar.NO_COOKIES)
                    .connectTimeout(1000L, TimeUnit.MILLISECONDS)
                    .readTimeout(1000L,TimeUnit.MILLISECONDS)
                    .writeTimeout(10000L,TimeUnit.MILLISECONDS)
                    .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                        @Override
                        public void log(String message) {
                            Log.i("http", message);
                        }
                    })
                    .setLevel(HttpLoggingInterceptor.Level.BASIC))
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            HttpUrl httpUrl = request.url()
                                    .newBuilder()
                                    .build();

                            Request newRequest = chain.request()
                                    .newBuilder()
                                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                                    .addHeader("Token", App.getInstance().getToken())
//                                .addHeader("Accept-Encoding", "gzip, deflate")
//                                .addHeader("Connection", "keep-alive")
//                                .addHeader("Accept", "*/*")
                                    .url(httpUrl)
                                    .build();
                            return chain.proceed(newRequest);
                        }
                    })
                    .build();
        }
        return mOkHttpClient;
    }
}
