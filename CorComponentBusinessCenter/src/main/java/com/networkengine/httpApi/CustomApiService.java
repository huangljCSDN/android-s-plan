package com.networkengine.httpApi;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by pengpeng on 17/4/13.
 */

public interface CustomApiService {
    /**
     * @param url
     * @param headers
     * @param requestParam 主动拼装的RequestBody 在加密的时候，不会走加密解析，需要在拼装RequestBody的时候，加密
     * @return
     */
    @POST
    Call<String> doBodyPost(@Url String url, @HeaderMap Map<String, String> headers, @Body RequestBody requestParam);

    @FormUrlEncoded
    @POST
    Call<String> doFormPost(@Url String url, @HeaderMap Map<String, String> headers, @FieldMap Map<String, String> names);

    @GET
    Call<String> doFormGet(@Url String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, String> names);

    /**
     * 带请求头上传
     *
     * @param url
     * @param headerMap
     * @param body
     * @return
     */
    @POST
    Call<String> uploadFile(@Url String url, @HeaderMap Map<String, String> headerMap, @Body RequestBody body);


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
     * 文件下载
     *
     * @param url
     * @return
     */
    @Streaming
    @Headers({"Content-Type: multipart/byteranges", "Connection: keep-alive"})
    @GET()
    Call<ResponseBody> downloadFile(@Url String url);

//    @GET
//    Call<CheckSignResult> checkSign(@Url String url, @HeaderMap Map<String, String> headerMap);
//
//    @POST
//    Call<CheckApprovalResult> checkApproval(@Url String url, @HeaderMap Map<String, String> headerMap);

}
