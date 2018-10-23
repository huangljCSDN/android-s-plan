package com.markLove.Xplan.mvp.contract;

import com.markLove.Xplan.api.util.RequestCallBack;
import com.markLove.Xplan.base.mvp.BaseModel;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.mvp.BaseView;

import java.util.Map;

import retrofit2.http.QueryMap;

/**
 * 合约类，将view，Model和Presenter 整合到一起。减少接口类数量，方便修改
 */
public interface PublishContract {

    interface View extends BaseView {
        void refreshUI(String json);
    }

    abstract class Model extends BaseModel {
        public abstract void addLocus(@QueryMap Map<String, String> map, RequestCallBack requestCallBack);
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void addLocus(@QueryMap Map<String, String> map);
    }
}
