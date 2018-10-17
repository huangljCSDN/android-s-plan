package com.markLove.Xplan.api.util;

import android.content.Context;

import com.markLove.Xplan.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by huanglingjun on 2018/5/11.
 */

public class RetrofitUtil {
    private Context mContext;
    private Retrofit retrofit;
    //由于该对象会被频繁调用，采用单例模式，下面是一种线程安全模式的单例写法
    private volatile static RetrofitUtil instance;

    public static RetrofitUtil getInstance(){
        if (instance == null){
            synchronized (RetrofitUtil.class){
                if (instance == null){
                    instance = new RetrofitUtil();
                }
            }
        }
        return instance;
    }

    private RetrofitUtil(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.APP_HOST)
                .client(OkHttpUtil.getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    //返回一个泛型
    public <T> T getApiServer(Class<T> server) {
        return retrofit.create(server);
    }

}
