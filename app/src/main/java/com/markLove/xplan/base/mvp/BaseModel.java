package com.markLove.xplan.base.mvp;

import com.markLove.xplan.api.util.RequestCallBack;
import com.markLove.xplan.api.util.RxObserver;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 暂时无方法，方便后续扩展
 */
public class BaseModel {

    /**
     * retrofit 请求网络
     * @param observable
     * @param requestCallBack
     */
    public void requestData(Observable observable , RequestCallBack requestCallBack){
        observable.timeout(50, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver(requestCallBack));
    }
}
