package com.markLove.Xplan.mvp.contract;

import com.markLove.Xplan.api.util.RequestCallBack;
import com.markLove.Xplan.base.mvp.BaseModel;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.mvp.BaseView;
import com.markLove.Xplan.bean.BaseBean;

import java.util.Map;

import retrofit2.http.QueryMap;

/**
 * 合约类，将view，Model和Presenter 整合到一起。减少接口类数量，方便修改
 */
public interface UserOperationContract {

    interface View extends BaseView {
        void onFocus(BaseBean baseBean);
        void isBlackSuccess(BaseBean baseBean);
        void isBlackError(String baseBean);
    }

    abstract class Model extends BaseModel {
        public abstract void focus(@QueryMap Map<String, String> map, RequestCallBack requestCallBack);
        public abstract void isBlackList(@QueryMap Map<String, String> map, RequestCallBack requestCallBack);
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void focus(@QueryMap Map<String, String> map);
        public abstract void isBlackList(@QueryMap Map<String, String> map);
    }
}
