package com.markLove.Xplan.api;

import com.markLove.Xplan.bean.BaseMsgBean;
import com.markLove.Xplan.bean.MerchantBean;
import com.markLove.Xplan.bean.UserBean;
import com.markLove.Xplan.bean.PlaceBean;
import com.markLove.Xplan.bean.PostQueryInfo;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
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

    /**
     * 刷新token
     * @param token
     * @return
     */
    Call<String> refreshToken(@Query("token") String token);

    /**
     * 店里的人
     * @param map
     * @return
     */
    @POST("merchant/discover/getMerchantUserList")
    Observable<Object> getMerchantUserList(@QueryMap Map<String, String> map);

    /**
     * 附近的店
     * @param map
     * @return
     */
    @POST("merchant/discover/getNearMerchant")
    Observable<Object> getNearMerchant(@QueryMap Map<String, String> map);

    /**
     * 附近的人
     * @param map
     * @return
     */
    @POST("merchant/discover/getNearUser")
    Observable<Object> getNearUser(@QueryMap Map<String, String> map);

    /**
     * 更新定位
     * @param map
     * @return
     */
    @POST("merchant/discover/updateUserPlace")
    Observable<PlaceBean> updateUserPlace(@QueryMap Map<String, String> map);

    /**
     * 文件下载
     * @param map
     * @return
     */
    @GET("system/dfs/download")
    Observable<BaseMsgBean> download(@QueryMap Map<String, String> map);

    /**
     * 文件上传
     * @param map
     * @return
     */
    @POST("system/dfs/upload")
    Observable<BaseMsgBean> upload(@QueryMap Map<String, String> map);

    /**
     * 加入组局
     * @param map
     * @return
     */
    @POST("user/group/joinGroup")
    Observable<BaseMsgBean> joinGroup(@QueryMap Map<String, String> map);

    /**
     * 同意报名
     * @param map
     * @return
     */
    @POST("user/group/participateGroup")
    Observable<BaseMsgBean> participateGroup(@QueryMap Map<String, String> map);

    /**
     * 关注
     * @param map
     * @return
     */
    @POST("user/operation/focus")
    Observable<BaseMsgBean> focus(@QueryMap Map<String, String> map);

}
