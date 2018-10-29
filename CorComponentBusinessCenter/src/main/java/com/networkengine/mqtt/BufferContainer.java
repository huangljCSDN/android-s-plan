package com.networkengine.mqtt;

import java.util.ArrayList;
import java.util.List;

/**
 * 泛型缓冲容器
 * Created by pengpeng on 17/1/6.
 */

public class BufferContainer<T> extends ArrayList<T> implements Runnable {

    /**
     * 同步锁
     */
    final Object lock = new Object();

    /**
     * 缓冲线程
     */
    private Thread mThread;//TODO 可以考虑用线程池托管

    /**
     * 结束标记
     */
    private boolean mIsFinish = false;

    /**
     * 缓冲适配器
     */
    BufferAdapter<T> mBufferAdapter;

    /**
     * 记录上一次新任务push的时间
     */
    long mWhenLastPush;

    /**
     * 缓冲适配器
     *
     * @param <T>泛型参数
     */
    public interface BufferAdapter<T> {

        /**
         * 获得周期
         *
         * @return 返回缓冲周期
         */
        long getCycle();

        /**
         * 缓冲结束的回调
         *
         * @param list 本次缓冲容的任务集合
         */
        void onPull(List<T> list);

    }

    /**
     * 构造
     *
     * @param BufferAdapter 缓冲适配器
     */
    public BufferContainer(BufferAdapter BufferAdapter) {
        mBufferAdapter = BufferAdapter;
        mThread = new Thread(this);
        mThread.start();
    }

    /**
     * 将新任务push进缓冲器
     *
     * @param t
     */
    public void pushTask(T t) {
        synchronized (mThread) {
            this.add(t);
            mThread.notify();
        }
    }

    /**
     * 关闭容器
     */
    public void close() {
        synchronized (mThread) {
            mIsFinish = true;
            mThread.notify();
        }
    }

    /**
     * 缓冲线程的具体实现
     */
    @Override
    public void run() {
        while (!mIsFinish) {
            synchronized (mThread) {
                /*
                * 容器中无任务，线程进入等待
                * */
                if (this.isEmpty()) {
                    try {
                        mThread.wait();
                    } catch (InterruptedException ignored) {
                    }
                    continue;
                }
            }

                /*
                * 获得当前时间
                * */
                long currentTime = System.currentTimeMillis();

            synchronized (mThread){
                /*
                * 第一次等一个周期并记录当前时间
                * */
                if (mWhenLastPush == 0) {
                    mWhenLastPush = currentTime;
                    try {
                        mThread.wait(mBufferAdapter.getCycle());
                    } catch (InterruptedException ignored) {
                    }
                    continue;
                }
                /*
                * 已经开始计时并再次被唤醒有两种情况
                * case 1 缓冲周期结束
                * case 2 新任务加入，重置缓冲周期
                * */
                else {
                    /*
                    * 通过对比本次唤醒时间和上一次唤醒时间与周期之和 判断当前情况
                    * */
                    if ((mWhenLastPush + mBufferAdapter.getCycle()) > currentTime) {
                        /*
                        * case 2 先于周期等待被唤醒说明有新任务加入
                        * 重置当前时间并继续等待一个周期
                        * */
                        mWhenLastPush = currentTime;
                        try {
                            mThread.wait(mBufferAdapter.getCycle());
                        } catch (InterruptedException ignored) {
                        }
                        continue;
                    } else {
                        /*
                        * case 1 周期内没有新任务加入
                        * 向外pull出缓冲中所有任务，并让缓冲线程进入等待
                        * */
                        mBufferAdapter.onPull((List) this.clone());
                        this.clear();
                        mWhenLastPush = 0;
                        try {
                            mThread.wait();
                        } catch (InterruptedException ignored) {
                        }
                    }

                }

            }


        }
    }

}
