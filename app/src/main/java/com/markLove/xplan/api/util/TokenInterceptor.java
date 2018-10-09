package com.markLove.xplan.api.util;

import android.util.Log;

import com.markLove.xplan.api.RetrofitApiService;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

/**
 * Created by huanglingjun on 2018/5/15.
 */

public class TokenInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        Log.i("response.code =",response.code()+"");

        if (isTokenExpired(response)){
            //同步请求方式，静默获取服务器最新token
            String newToken = getNewToken();
            Request newRequest = chain.request()
                    .newBuilder()
                    .header("token",newToken)
//                    .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
//                    .header("Accept-Encoding", "gzip, deflate")
                    .build();
            //重新请求
            return chain.proceed(newRequest);
        }
        return response;
    }

    /**
     * 获取新token，使用同步请求
     * @return
     * @throws IOException
     */
    private String getNewToken() throws IOException{
        String refreshToken = "12412414";
        // 通过一个特定的接口获取新的token，此处要用到同步的retrofit请求
        RetrofitApiService service = RetrofitUtil.getInstance().getApiServer(RetrofitApiService.class);
        Call<String> call = service.refreshToken(refreshToken);

        //要用retrofit的同步方式
        String newToken = call.execute().body();

        return newToken;
    }

    /**
     * 判断token是否过期
     * @param response
     * @return
     */
    private boolean isTokenExpired(Response response) {
        if (response.code() == 404) {
            return true;
        }
        return false;
    }
}
