package com.networkengine.httpApi;

import android.text.TextUtils;

import com.networkengine.ConfigUtil;
import com.networkengine.database.table.Member;
import com.networkengine.engine.EngineParameter;
import com.networkengine.engine.LogicEngine;
import com.networkengine.httpApi.cookie.InMemoryCookieStore;
import com.networkengine.httpApi.cookie.JavaNetCookieJar;
import com.networkengine.httpApi.encrypt.EncryptCenter;
import com.networkengine.httpApi.intercept.H5Interceptor;
import com.networkengine.httpApi.intercept.MchlInterceptor;
import com.networkengine.httpApi.ssl.SSLHelper;
import com.networkengine.httpApi.ssl.SafeHostnameVerifier;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class Api {

    //超时时间
    private static final int DEFAULT_TIMEOUT = 60;

    //默认的CookieStore
    private static InMemoryCookieStore mInMeoryCookieStore = null;

    public static InMemoryCookieStore getInMemoryCookieStore() {
        if (mInMeoryCookieStore == null) {
            mInMeoryCookieStore = new InMemoryCookieStore();
        }
        return mInMeoryCookieStore;
    }

//    /**
//     * 获取MXM API
//     *
//     * @param engineParameter 基础参数
//     * @return HttpApi
//     */
//    public static MxmApiService mxmService(Context context,EngineParameter engineParameter) {
//        //注释 ： 接入平台的兼容处理，mxm的地址返回在登陆接入平台的返回中，第一个mxm的请求的baserurl为空，故做兼容处理
//        String baseUrl = engineParameter.mxmServiceBaseUrl;
//
//        if (TextUtils.isEmpty(baseUrl) || baseUrl.equals("://:")) {
//            baseUrl = "http://v.coracle.com:10000/mxm/";
//        }
//        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
//                .baseUrl(baseUrl);
//
////        retrofitBuilder.addConverterFactory(ScalarsConverterFactory.create());
//        //加密开关
//        if (TextUtils.isEmpty(EncryptCenter.getType())) {
//            retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
//        } else {
//            retrofitBuilder.addConverterFactory(EncryptConverterFactory.create());
//        }
//
////        retrofitBuilder.addConverterFactory(MoshiConverterFactory.create());
//
//        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
//
//        httpClientBuilder.cookieJar(new JavaNetCookieJar());
//
//
//        //  Interceptor mxMInterceptor = getMxMInterceptor(engineParameter);
//
//        //登陆失效拦截器
//        Interceptor mxMInterceptor = new MxmInterceptor(engineParameter, null);
//
//        httpClientBuilder.addInterceptor(mxMInterceptor);
//
////                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
//        httpClientBuilder.sslSocketFactory(SSLHelper.createSSLSocketFactory())
//                .hostnameVerifier(new SafeHostnameVerifier());
//
//        //设置302 不重定向
//        //   httpClientBuilder.followRedirects(false);
//
//        retrofitBuilder.client(httpClientBuilder.build());
//        //创建服务
//        Retrofit authRetrofit = retrofitBuilder.build();
//
//
//        return authRetrofit.create(MxmApiService.class);
//    }
//
//
//    /**
//     * 获取MXM 文件上传 API
//     *
//     * @return MxmApiService
//     */
//    public static MxmApiService mxmFileService() {
//
//        String baseUrl = LogicEngine.getMxmUrl();
//        Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl(baseUrl);
//
////        retrofitBuilder.addConverterFactory(ScalarsConverterFactory.create());
//        retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
////        retrofitBuilder.addConverterFactory(MoshiConverterFactory.create());
//
//        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
//        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
//        httpClientBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
//        httpClientBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
//
//        httpClientBuilder.cookieJar(new JavaNetCookieJar());
//
//        httpClientBuilder.addInterceptor(getMxMFileInterceptor())
//                .sslSocketFactory(SSLHelper.createSSLSocketFactory())
//                .hostnameVerifier(new SafeHostnameVerifier());
//
//        retrofitBuilder.client(httpClientBuilder.build());
//        //创建服务
//
//        Retrofit retrofit = retrofitBuilder.build();
//
//        return retrofit.create(MxmApiService.class);
//    }


    /**
     * 获取MCHL API
     *
     * @param engineParameter
     * @param member
     * @return
     */
    public static MchlApiService mchlService(EngineParameter engineParameter, Member member) {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(engineParameter.imServiceBaseUrl);

        // retrofitBuilder.addConverterFactory(SignConverterFactory.create(ct));
//        retrofitBuilder.addConverterFactory(ScalarsConverterFactory.create());
        retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        if (TextUtils.isEmpty(EncryptCenter.getType())) {
            retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
        } else {
            retrofitBuilder.addConverterFactory(EncryptConverterFactory.create());
        }
//        retrofitBuilder.addConverterFactory(MoshiConverterFactory.create());

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        //  Interceptor mchlInterceptor = getMchlInterceptor(engineParameter, member);
        Interceptor mchlInterceptor = new MchlInterceptor(engineParameter, member);

        httpClientBuilder.addInterceptor(mchlInterceptor)
                .sslSocketFactory(SSLHelper.createSSLSocketFactory())
                .hostnameVerifier(new SafeHostnameVerifier());
        retrofitBuilder.client(httpClientBuilder.build());
        //创建服务
        Retrofit authRetrofit = retrofitBuilder.build();

        return authRetrofit.create(MchlApiService.class);
    }

    /**
     * 获取文件预览服务API
     *
     * @return
     */
    public static PreviewApiService preService() {
        String baseUrl = ConfigUtil.getPreviewHost();
        if (TextUtils.isEmpty(baseUrl)) {
            baseUrl = "http://172.16.23.95:8088/xoffice";
        }

        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create());

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .sslSocketFactory(SSLHelper.createSSLSocketFactory())
                .hostnameVerifier(new SafeHostnameVerifier());

        retrofitBuilder.client(httpClientBuilder.build());
        //创建服务
        Retrofit authRetrofit = retrofitBuilder.build();

        return authRetrofit.create(PreviewApiService.class);
    }


    /**
     * 获取文件传输 API
     *
     * @param engineParameter
     * @return
     */
    public static FileTransApiService fileTransService(EngineParameter engineParameter, Member member) {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(engineParameter.imServiceBaseUrl);

//        retrofitBuilder.addConverterFactory(ScalarsConverterFactory.create());
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
//        retrofitBuilder.addConverterFactory(MoshiConverterFactory.create());

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        //设置超时
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.addInterceptor(getFileTransInterceptor(engineParameter, member))
                .sslSocketFactory(SSLHelper.createSSLSocketFactory())
                .hostnameVerifier(new SafeHostnameVerifier());
        retrofitBuilder.client(httpClientBuilder.build());
        //创建服务
        Retrofit authRetrofit = retrofitBuilder.build();
        return authRetrofit.create(FileTransApiService.class);
    }

    /**
     * 主要用于H5的文件上传
     *
     * @param
     * @return
     */
    public static CustomApiService customApiService() {
        String baseUrl = LogicEngine.getMxmUrl();
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl(baseUrl);

//        retrofitBuilder.addConverterFactory(ScalarsConverterFactory.create());
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
//        retrofitBuilder.addConverterFactory(MoshiConverterFactory.create());

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        httpClientBuilder.cookieJar(new JavaNetCookieJar());

        httpClientBuilder.addInterceptor(getH5Interceptor())  // .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .sslSocketFactory(SSLHelper.createSSLSocketFactory())
                .hostnameVerifier(new SafeHostnameVerifier());

        retrofitBuilder.client(httpClientBuilder.build());
        //创建服务

        Retrofit retrofit = retrofitBuilder.build();

        return retrofit.create(CustomApiService.class);
    }

    /**
     * H5请求的拦截器
     *
     * @return
     */
    public static CustomApiService h5CustomApiService() {
        String baseUrl = LogicEngine.getMxmUrl();
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl(baseUrl);


//        retrofitBuilder.addConverterFactory(ScalarsConverterFactory.create());
        if (TextUtils.isEmpty(EncryptCenter.getType())) {
            retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
        } else {
            retrofitBuilder.addConverterFactory(EncryptConverterFactory.create());
        }
//        retrofitBuilder.addConverterFactory(MoshiConverterFactory.create());

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        httpClientBuilder.cookieJar(new JavaNetCookieJar());
        //Interceptor h5Interceptor = getH5Interceptor();
        Interceptor h5Interceptor = new H5Interceptor(LogicEngine.getInstance().getEngineParameter(), LogicEngine.getInstance().getUser());

        httpClientBuilder.addInterceptor(h5Interceptor);
        httpClientBuilder.sslSocketFactory(SSLHelper.createSSLSocketFactory())
                .hostnameVerifier(new SafeHostnameVerifier());

        retrofitBuilder.client(httpClientBuilder.build());
        //创建服务

        Retrofit retrofit = retrofitBuilder.build();

        return retrofit.create(CustomApiService.class);
    }


    private static Interceptor getMchlInterceptor(final EngineParameter engineParameter
            , final Member member) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request request = chain.request();

                Request.Builder requestBuilder = request.newBuilder();

                if (null != engineParameter) {
                    requestBuilder.header("X-xSimple-appKey", !TextUtils.isEmpty(engineParameter.appKey) ? engineParameter.appKey : "");
                    requestBuilder.header("User-Agent", !TextUtils.isEmpty(engineParameter.userAgent) ? engineParameter.userAgent : "");
                }

                if (null != member) {
                    requestBuilder.header("X-xSimple-LoginName", !TextUtils.isEmpty(member.getId().toString()) ? member.getId().toString() : "");
                    requestBuilder.header("X-xSimple-AuthToken", !TextUtils.isEmpty(member.getUserToken()) ? member.getUserToken() : "");
                    requestBuilder.header("X-xSimple-SysCode", !TextUtils.isEmpty(member.getUserSystem()) ? member.getUserSystem() : "");
                    requestBuilder.header("X-xSimple-SysUserID", !TextUtils.isEmpty(member.getUserId()) ? member.getUserId() : "");
                }

                if (!TextUtils.isEmpty(EncryptCenter.getType())) {
                    requestBuilder.header("X-xSimple-EM", EncryptCenter.getType());
                }
                requestBuilder.header("Accept-Encoding", "");

                requestBuilder.header("Content-Type", "text/plain; charset=UTF-8");

                requestBuilder.method(request.method(), request.body());

                request = requestBuilder.build();

                return chain.proceed(request);
            }
        };

    }

    /**
     * 获取 H5 网络请求的拦截器
     * 主要用于H5的文件上传
     * 不要加加密的头
     *
     * @return
     */
    private static Interceptor getH5Interceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                Request.Builder requestBuilder = request.newBuilder();
                // 一定不要有，否则H5文件上传会加密
//                if (!TextUtils.isEmpty(EncryptCenter.getType())) {
//                    requestBuilder.header("X-xSimple-EM", EncryptCenter.getType());
//                }
                LogicEngine logicEngine = LogicEngine.getInstance();
                if (logicEngine != null) {
                    EngineParameter engineParameter = logicEngine.getEngineParameter();
                    if (engineParameter != null) {
                        String userAgent = engineParameter.userAgent;
                        requestBuilder.addHeader("User-Agent", TextUtils.isEmpty(userAgent) ? "" : userAgent);
                        requestBuilder.addHeader("X-xSimple-appKey", !TextUtils.isEmpty(engineParameter.appKey) ? engineParameter.appKey : "");
                    }
                    Member member = logicEngine.getUser();
                    if (null != member) {
                        requestBuilder.addHeader("X-xSimple-LoginName", !TextUtils.isEmpty(member.getId().toString()) ? member.getId().toString() : "");
                        requestBuilder.addHeader("X-xSimple-AuthToken", !TextUtils.isEmpty(member.getUserToken()) ? member.getUserToken() : "");
                        requestBuilder.addHeader("X-xSimple-SysCode", !TextUtils.isEmpty(member.getUserSystem()) ? member.getUserSystem() : "");
                        requestBuilder.addHeader("X-xSimple-SysUserID", !TextUtils.isEmpty(member.getUserId()) ? member.getUserId() : "");
                    }
                }

                //   requestBuilder.header("Accept-Encoding", "");

                requestBuilder.method(request.method(), request.body());
                request = requestBuilder.build();

                return chain.proceed(request);
            }
        };
    }

    /**
     * mxm 文件上传 拦截器，一定不要加密
     *
     * @return
     */
    private static Interceptor getMxMFileInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                Request.Builder requestBuilder = request.newBuilder();
                // 一定不要有，否则 mxm文件上传会加密
//                if (!TextUtils.isEmpty(EncryptCenter.getType())) {
//                    requestBuilder.header("X-xSimple-EM", EncryptCenter.getType());
//                }
                LogicEngine logicEngine = LogicEngine.getInstance();
                if (logicEngine != null) {
                    EngineParameter engineParameter = logicEngine.getEngineParameter();
                    if (engineParameter != null) {
                        String userAgent = engineParameter.userAgent;
                        requestBuilder.addHeader("User-Agent", TextUtils.isEmpty(userAgent) ? "" : userAgent);
                        requestBuilder.addHeader("X-xSimple-appKey", !TextUtils.isEmpty(engineParameter.appKey) ? engineParameter.appKey : "");
                    }

                }
                requestBuilder.method(request.method(), request.body());
                request = requestBuilder.build();

                return chain.proceed(request);
            }
        };
    }


    private static Interceptor getMxMInterceptor(final EngineParameter engineParameter) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                Request.Builder requestBuilder = request.newBuilder();
                if (!TextUtils.isEmpty(EncryptCenter.getType())) {
                    requestBuilder.header("X-xSimple-EM", EncryptCenter.getType());
                }
                requestBuilder.addHeader("User-Agent", engineParameter.userAgent);

                requestBuilder.method(request.method(), request.body());
                request = requestBuilder.build();

                Response proceed = chain.proceed(request);

                return proceed;
            }
        };
    }


    private static Interceptor getFileTransInterceptor(final EngineParameter engineParameter
            , final Member member) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                Request.Builder requestBuilder = request.newBuilder();
                requestBuilder.addHeader("User-Agent"
                        , !TextUtils.isEmpty(engineParameter.userAgent) ? engineParameter.userAgent : "");
                requestBuilder.addHeader("X-xSimple-LoginName"
                        , !TextUtils.isEmpty(member.getId().toString()) ? member.getId().toString() : "");
                requestBuilder.addHeader("X-xSimple-appKey"
                        , !TextUtils.isEmpty(engineParameter.appKey) ? engineParameter.appKey : "");
                requestBuilder.addHeader("X-xSimple-AuthToken"
                        , !TextUtils.isEmpty(member.getUserToken()) ? member.getUserToken() : "");
                requestBuilder.method(request.method(), request.body());
                request = requestBuilder.build();

                return chain.proceed(request);
            }
        };
    }

}