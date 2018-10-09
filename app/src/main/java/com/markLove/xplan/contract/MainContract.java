package com.markLove.xplan.contract;

import com.markLove.xplan.api.util.RequestCallBack;
import com.markLove.xplan.base.mvp.BaseModel;
import com.markLove.xplan.base.mvp.BasePresenter;
import com.markLove.xplan.base.mvp.BaseView;
import com.markLove.xplan.bean.PostQueryInfo;

/**
 * 合约类，将view，Model和Presenter 整合到一起。减少接口类数量，方便修改
 */
public interface MainContract {

    interface View<T> extends BaseView {
        void refreshUI(T t);
    }

    abstract class Model extends BaseModel {
        public abstract void searchRx(String type, String postid, RequestCallBack requestCallBack);
    }

    abstract class Presenter extends BasePresenter<View<PostQueryInfo>> {
        public abstract void getData(String type, String postid);
    }
}
