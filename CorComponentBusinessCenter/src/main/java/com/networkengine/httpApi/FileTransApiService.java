package com.networkengine.httpApi;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface FileTransApiService {

    /**
     * 文件上传
     *
     * @param body
     * @return
     */
    @POST("file/upload")
    Call<String> uploadFile(@Body RequestBody body);

    /**
     * 文件上传
     *
     * @param url
     * @param body
     * @return
     */
    @POST()
    Call<String> uploadFiles(@Url String url, @Body RequestBody body);

    /**
     * 带请求头上传
     *
     * @param url
     * @param headerMap
     * @param body
     * @return
     */
    @POST("file/upload")
    Call<String> uploadFile(@Url String url, @HeaderMap Map<String, String> headerMap, @Body RequestBody body);


    /**
     * 文件下载
     *
     * @param map
     * @return
     */
    @Streaming
    @Headers({"Content-Type: multipart/byteranges", "Connection: keep-alive"})
    @GET("file/download")
    Call<ResponseBody> downloadFile(@QueryMap Map<String, String> map);


    /**
     * 文件下载
     *
     * @param url
     * @return
     */
    @Streaming
    @Headers({"Content-Type: multipart/byteranges", "Connection: keep-alive"})
    @GET()
    Call<ResponseBody> downloadFiles(@Url String url);
}
