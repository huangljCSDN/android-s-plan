package com.markLove.Xplan.mvp.contract;

import com.markLove.Xplan.api.util.RequestCallBack;
import com.markLove.Xplan.base.mvp.BaseModel;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.mvp.BaseView;
import com.markLove.Xplan.bean.BaseMsgBean;
import com.markLove.Xplan.bean.UserBean;

import java.util.Map;

import retrofit2.http.QueryMap;

/**
 *
 */
public interface CpChatContract {

    interface View<T> extends BaseView {
        void refreshUI(T t);
    }

    abstract class Model extends BaseModel {
        public abstract void focus(@QueryMap Map<String, String> map, RequestCallBack requestCallBack);
    }

    abstract class Presenter extends BasePresenter<View<BaseMsgBean>> {
        public abstract void focus(@QueryMap Map<String, String> map);
    }
}
