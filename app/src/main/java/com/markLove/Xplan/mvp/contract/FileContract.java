package com.markLove.Xplan.mvp.contract;

import com.markLove.Xplan.api.util.RequestCallBack;
import com.markLove.Xplan.base.mvp.BaseModel;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.mvp.BaseView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.http.QueryMap;

/**
 * 文件上传、下载
 */
public interface FileContract {

    interface View extends BaseView {
        void uploadSuccess(ArrayList<String> path);
        void downloadSuccess(String json);
    }

    abstract class Model extends BaseModel {
        public abstract void upload(List<File> files, RequestCallBack requestCallBack);
        public abstract void download(@QueryMap Map<String, String> map, RequestCallBack requestCallBack);
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void upload(List<File> files);
        public abstract void download(@QueryMap Map<String, String> map);
    }
}
