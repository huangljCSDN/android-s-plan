package com.markLove.Xplan.mvp.contract;

import com.markLove.Xplan.api.util.RequestCallBack;
import com.markLove.Xplan.base.mvp.BaseModel;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.mvp.BaseView;
import com.markLove.Xplan.bean.MerchantInfoBean;
import com.markLove.Xplan.bean.PostQueryInfo;
import com.markLove.Xplan.bean.UserBean;

import java.util.Map;

import retrofit2.http.QueryMap;

/**
 * 店铺聊天室
 */
public interface ShopChatContract {

    interface View extends BaseView {
        void refreshUsersByGroup(Object t);
        void refreshMerchantInfo(MerchantInfoBean merchantInfoBean);
    }

    abstract class Model extends BaseModel {
        public abstract void getUsersByGroup(@QueryMap Map<String, String> map, RequestCallBack requestCallBack);
        public abstract void getMerchantInfo(@QueryMap Map<String, String> map, RequestCallBack requestCallBack);
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void getUsersByGroup(@QueryMap Map<String, String> map);
        public abstract void getMerchantInfo(@QueryMap Map<String, String> map);
    }
}
