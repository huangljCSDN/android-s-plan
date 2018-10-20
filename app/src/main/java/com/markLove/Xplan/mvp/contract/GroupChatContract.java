package com.markLove.Xplan.mvp.contract;

import com.markLove.Xplan.api.util.RequestCallBack;
import com.markLove.Xplan.base.mvp.BaseModel;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.mvp.BaseView;
import com.markLove.Xplan.bean.BaseMsgBean;
import com.markLove.Xplan.bean.MerchantBean;
import com.markLove.Xplan.bean.UserBean;

import java.util.Map;

import retrofit2.http.QueryMap;

/**
 * 合约类，将view，Model和Presenter 整合到一起。减少接口类数量，方便修改
 */
public interface GroupChatContract {

    interface View extends BaseView {
        void onJoinGroup(BaseMsgBean baseMsgBean);
        void onParticipateGroup(BaseMsgBean baseMsgBean);
    }

    abstract class Model extends BaseModel {
        public abstract void joinGroup(@QueryMap Map<String, String> map, RequestCallBack requestCallBack);
        public abstract void participateGroup(@QueryMap Map<String, String> map, RequestCallBack requestCallBack);
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void joinGroup(@QueryMap Map<String, String> map);
        public abstract void participateGroup(@QueryMap Map<String, String> map);
    }
}
