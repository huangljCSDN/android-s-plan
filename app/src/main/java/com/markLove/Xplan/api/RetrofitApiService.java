package com.markLove.Xplan.api;

import com.markLove.Xplan.bean.PostQueryInfo;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by huanglingjun on 2018/5/11.
 */

public interface RetrofitApiService {

    /**
     * 使用rxjava请求
     * @param type
     * @param postid
     * @return
     */
    @POST("query")
    Observable<PostQueryInfo> searchRx(@Query("type") String type, @Query("postid") String postid);

    @POST("query")
    Observable<PostQueryInfo> searchRx2(@QueryMap Map<String, String> map);

    /**
     * 刷新token
     * @param token
     * @return
     */
    Call<String> refreshToken(@Query("token") String token);
}
