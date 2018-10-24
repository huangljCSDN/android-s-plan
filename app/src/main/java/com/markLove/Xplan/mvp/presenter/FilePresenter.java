package com.markLove.Xplan.mvp.presenter;

import com.markLove.Xplan.api.util.RequestCallBack;
import com.markLove.Xplan.bean.BaseBean;
import com.markLove.Xplan.mvp.contract.FileContract;
import com.markLove.Xplan.mvp.model.FileModel;
import com.markLove.Xplan.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by huanglingjun on 2018/5/16.
 */

public class FilePresenter extends FileContract.Presenter {
    private FileModel mModel;

    public FilePresenter(){
        mModel = new FileModel();
    }

    @Override
    public void upload(List<File> files) {
        if (!isAttach()) return;
        getView().showLoading();

        mModel.upload(files, new RequestCallBack<BaseBean<Object>>() {
            @Override
            public void onSuccess(BaseBean<Object> json) {
                LogUtils.i("FilePresenter",json.toString());
                getView().hideLoading();
//                getView().uploadSuccess(json.Data);
            }

            @Override
            public void onFail(String result) {
                LogUtils.i("FilePresenter",result);
                getView().hideLoading();
                getView().showError(result);
            }
        });
    }

    @Override
    public void download(Map<String, String> map) {
        if (!isAttach()) return;
        getView().showLoading();

        mModel.download(map, new RequestCallBack<Object>() {
            @Override
            public void onSuccess(Object json) {
                LogUtils.i("FilePresenter",json.toString());
                getView().hideLoading();
                getView().downloadSuccess(json.toString());
            }

            @Override
            public void onFail(String result) {
                LogUtils.i("FilePresenter",result);
                getView().hideLoading();
                getView().showError(result);
            }
        });
    }
}
