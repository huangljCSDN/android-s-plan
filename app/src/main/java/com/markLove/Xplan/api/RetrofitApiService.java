package com.markLove.Xplan.api;

import com.markLove.Xplan.bean.BaseBean;
import com.markLove.Xplan.bean.GroupDetailBean;
import com.markLove.Xplan.bean.ListBean;
import com.markLove.Xplan.bean.MerchantBean;
import com.markLove.Xplan.bean.MerchantInfoBean;
import com.markLove.Xplan.bean.NearUserBean;
import com.markLove.Xplan.bean.PostQueryInfo;
import com.markLove.Xplan.bean.UploadFileBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
//    Call<String> refreshToken(@Query("token") String token);

    /**
     * 店里的人
     * @param map
     * @return
     */
    @POST("merchant/discover/getMerchantUserList")
    Observable<BaseBean<ArrayList<NearUserBean>>> getMerchantUserList(@QueryMap Map<String, String> map);

    /**
     * 附近的店
     * @param map
     * @return
     */
    @POST("merchant/discover/getNearMerchant")
    Observable<BaseBean<ArrayList<MerchantBean>>> getNearMerchant(@QueryMap Map<String, String> map);

    /**
     * 附近的人
     * @param map
     * @return
     */
    @POST("merchant/discover/getNearUser")
    Observable<BaseBean<ListBean<ArrayList<NearUserBean>>>> getNearUser(@QueryMap Map<String, String> map);

    /**
     * 新增轨迹
     * @param map
     * @return
     */
    @POST("user/locus/addLocus")
    Observable<BaseBean<Object>> addLocus(@QueryMap Map<String, String> map);

    /**
     * 更新定位
     * @param map
     * @return
     */
    @POST("merchant/discover/updateUserPlace")
    Observable<BaseBean> updateUserPlace(@QueryMap Map<String, String> map);

    /**
     * 文件下载
     * @param map
     * @return
     */
    @GET("system/dfs/download")
    Observable<BaseBean> download(@QueryMap Map<String, String> map);

    /**
     * 文件上传  通过 List<MultipartBody.Part> 传入多个part实现多文件上传
     * @param files  每个part代表一个
     * @return
     */
    @Multipart
    @POST("system/dfs/upload")
    Observable<BaseBean<UploadFileBean>> upload(@Part() List<MultipartBody.Part> files);

    /**
     * 文件上传  通过 MultipartBody和@body作为参数来上传
     * @param files   MultipartBody包含多个Part
     * @return
     */
    @POST("system/dfs/upload")
    Observable<BaseBean<UploadFileBean>> upload(@Body MultipartBody files);

    /**
     * 进入组局
     * @param map
     * @return
     */
    @POST("user/group/joinGroup")
    Observable<BaseBean<Object>> joinGroup(@QueryMap Map<String, String> map);

    /**
     * 组局详情
     * @param map
     * @return
     */
    @POST("user/group/groupDetails")
    Observable<BaseBean<GroupDetailBean>> groupDetails(@QueryMap Map<String, String> map);

    /**
     * 同意报名
     * @param map
     * @return
     */
    @POST("user/group/participateGroup")
    Observable<BaseBean<Object>> participateGroup(@QueryMap Map<String, String> map);

    /**
     * 报名参加组局
     * @param map
     * @return
     */
    @POST("user/group/applyGroup")
    Observable<BaseBean<Object>> applyGroup(@QueryMap Map<String, String> map);

    /**
     * 店铺聊天室里的用户
     * @param map
     * @return
     */
    @POST("merchant/info/getUsersByGroup")
    Observable<BaseBean<Object>> getUsersByGroup(@QueryMap Map<String, String> map);

    /**
     * 店铺详细信息
     * @param map
     * @return
     */
    @GET("merchant/info/getMerchantInfo")
    Observable<BaseBean<MerchantInfoBean>> getMerchantInfo(@QueryMap Map<String, String> map);

    /**
     * 关注
     * @param map
     * @return
     */
    @POST("user/operation/focus")
    Observable<BaseBean> focus(@QueryMap Map<String, String> map);

    /**
     * 取消关注
     * @param map
     * @return
     */
    @POST("user/operation/cancelFocus")
    Observable<BaseBean> cancelFocus(@QueryMap Map<String, String> map);

    /**
     * 是否处于黑名单
     * @param map
     * @return
     */
    @POST("user/operation/isBlackList")
    Observable<BaseBean> isBlackList(@QueryMap Map<String, String> map);
}
