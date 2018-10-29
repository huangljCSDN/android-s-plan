package com.networkengine.controller;

import android.text.TextUtils;

import com.networkengine.engine.LogicEngine;
import com.networkengine.httpApi.Api;
import com.networkengine.httpApi.FileTransApiService;

import java.io.IOException;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by liuhao on 2017/3/4.
 */

public class FileTransController {

    private FileTransApiService mApiService;

    public interface FileTransListener<T> {
        /**
         * 访问成功
         *
         * @param t
         */
        void onSuccess(T t);

        /**
         * @param msg
         */
        void onFail(String msg);

    }


    public FileTransController() {
        mApiService = Api.fileTransService(LogicEngine.getInstance().getEngineParameter()
                , LogicEngine.getInstance().getUser());
    }


    public static String DOWNLOAD_ENTITY = "action_download_entity";
    public static String UPLOAD_ENTITY = "action_upload_entity";
    public static String FAVORITEE_NTITY = "action_favortee_entity";




    /**
     * 下载
     *
     * @param map
     * @return
     * @throws IOException
     */
    public ResponseBody download(Map<String, String> map) throws IOException {
        if (map == null) {
            return null;
        }
        for (String key : map.keySet()) {
            String value = map.get(key);
            if (null == value) {
                map.put(key, "");
            }
        }
        Call<ResponseBody> bodyCall = mApiService.downloadFile(map);

        Response<ResponseBody> execute = bodyCall.execute();
        ResponseBody body = execute.body();
        if (body == null)
            return null;
        return body;
    }

    /**
     * 下载
     *
     * @param url
     * @return
     * @throws IOException
     */
    public ResponseBody downloads(String url) throws IOException {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        Call<ResponseBody> bodyCall = mApiService.downloadFiles(url);

        Response<ResponseBody> execute = bodyCall.execute();
        ResponseBody body = execute.body();
        if (body == null)
            return null;
        return body;
    }


    /**
     * 上传
     *
     * @param body
     * @return
     * @throws IOException
     */
    public String uploadFile(RequestBody body) throws IOException {
        Call<String> call = mApiService.uploadFile(body);
        Response<String> execute = call.execute();
        return execute.body();
    }

    /**
     * 上传
     *
     * @param url
     * @param body
     * @return
     * @throws IOException
     */
    public String uploadFiles(String url, RequestBody body) throws IOException {
        Call<String> call = mApiService.uploadFiles(url, body);
        Response<String> execute = call.execute();
        return execute.body();
    }


    /**
     * 带请求头的上传
     *
     * @param url
     * @param header
     * @param body
     * @return
     * @throws IOException
     */
    public String uploadFile(String url, Map<String, String> header, RequestBody body) throws IOException {
        if (TextUtils.isEmpty(url)) {
            return mApiService.uploadFile("", header, body).execute().body();
        } else {
            return mApiService.uploadFile(url, header, body).execute().body();
        }
    }
}
