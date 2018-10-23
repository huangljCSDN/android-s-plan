package com.markLove.Xplan.mvp.contract;

import com.markLove.Xplan.api.util.RequestCallBack;
import com.markLove.Xplan.base.mvp.BaseModel;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.mvp.BaseView;

import java.util.Map;

import retrofit2.http.QueryMap;

/**
 * 文件上传、下载
 */
public interface FileContract {

    interface View extends BaseView {
        void uploadSuccess(String json);
        void downloadSuccess(String json);
    }

    abstract class Model extends BaseModel {
        public abstract void upload(@QueryMap Map<String, String> map, RequestCallBack requestCallBack);
        public abstract void download(@QueryMap Map<String, String> map, RequestCallBack requestCallBack);
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void upload(@QueryMap Map<String, String> map);
        public abstract void download(@QueryMap Map<String, String> map);
    }
}
