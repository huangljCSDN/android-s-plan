package com.markLove.xplan.module.image;

import android.util.Log;

import org.reactivestreams.Subscription;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;

/**
 * Created by Administrator on 2018/5/3.
 * 采用Luban压缩
 */

public class LubanImageCompressor extends BaseImageCompressor {
    private static final String TAG = "LubanImageCompressor";

    private LubanImageCompressor() {
    }

    public static LubanImageCompressor newInstance() {
        return new LubanImageCompressor();
    }

    @Override
    public void startCompress() {
        super.startCompress();


        Flowable.just(mSrcImageFilePaths)
                .observeOn(Schedulers.io())
                .map(new Function<String[], List<String>>() {
                    @Override
                    public List<String> apply(String[] filePaths) throws Exception {
                        List<File> files = Luban.with(mContext).load(Arrays.asList(filePaths)).get();

                        List<String> destFilePaths = new ArrayList<>();
                        if (files != null && files.size() > 0) {
                            for (int i = 0; i < files.size(); i++) {
                                String srcImageFilePath = mSrcImageFilePaths[i];
                                File srcImageFile = new File(srcImageFilePath);
                                String srcImageMame = srcImageFile.getName();
                                String destFilePath = mDestImageDir + srcImageMame;
                                File destFile = new File(destFilePath);
                                boolean b = files.get(i).renameTo(destFile);
                                if (b) {
                                    destFilePaths.add(destFile.getAbsolutePath());
                                }
                            }
                        }
                        return destFilePaths;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        Log.e(TAG, "doOnSubscribe: " + Thread.currentThread().getName());
                        if (mListener != null) {
                            String msg = "开始进行压缩";
                            mListener.onCompressStart(msg);
                        }
                    }
                })
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> destFilePaths) throws Exception {
                        if (mListener != null) {
                            mListener.onCompressComplete(destFilePaths);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //error
                        Log.e(TAG, "doOnError: " + Thread.currentThread().getName());
                        if (mListener != null) {
                            mListener.onCompressError(throwable.getMessage());
                        }
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //complete
                    }
                });
    }

    @Override
    public void syncStartCompress() {
        super.syncStartCompress();

        Flowable.just(mSrcImageFilePaths)
                .map(new Function<String[], List<String>>() {
                    @Override
                    public List<String> apply(String[] filePaths) throws Exception {
                        List<File> files = Luban.with(mContext).load(Arrays.asList(filePaths)).get();

                        List<String> destFilePaths = new ArrayList<>();
                        if (files != null && files.size() > 0) {
                            for (int i = 0; i < files.size(); i++) {
                                String srcImageFilePath = mSrcImageFilePaths[i];
                                File srcImageFile = new File(srcImageFilePath);
                                String srcImageMame = srcImageFile.getName();
                                String destFilePath = mDestImageDir + srcImageMame;
                                File destFile = new File(destFilePath);
                                boolean b = files.get(i).renameTo(destFile);
                                if (b) {
                                    destFilePaths.add(destFile.getAbsolutePath());
                                }
                            }
                        }
                        return destFilePaths;
                    }
                })
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        Log.e(TAG, "doOnSubscribe: " + Thread.currentThread().getName());
                        if (mListener != null) {
                            String msg = "开始进行压缩";
                            mListener.onCompressStart(msg);
                        }
                    }
                })
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> destFilePaths) throws Exception {
                        if (mListener != null) {
                            mListener.onCompressComplete(destFilePaths);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        //error
                        Log.e(TAG, "doOnError: " + Thread.currentThread().getName());
                        if (mListener != null) {
                            mListener.onCompressError(throwable.getMessage());
                        }
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //complete
                    }
                });
    }
}
