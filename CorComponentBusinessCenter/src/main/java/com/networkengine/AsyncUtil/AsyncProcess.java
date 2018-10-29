package com.networkengine.AsyncUtil;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 异步过程
 * <p>
 * 程序中复杂的异步流程处理类，可以对复杂逻辑进行分解编排。
 * 例如初始化动作中的加载流程涉及多个耗时的子任务，这些任务中还存在对其它子任务的结果依赖,
 * 可以利用此类对整个逻辑进行分解编排，创建多个该类实例，然后在多个实例之间创建依赖关系,
 * 通过内置的回调实现多实例协作处理复杂的异步流程。
 *
 * @param <T> AsyncTask泛型
 */

public class AsyncProcess<T extends RestTask> extends Marker<T> implements RestTask.TaskListener<T, Boolean>, RestTask.DependCallBack {

    static final String TAG = "AsyncProcess";

    /**
     * 执行情况反馈回调实例
     */
    private IAsyncTaskCallback<T> asyncTaskCallback;

    /**
     * 对外部存在依赖的任务表
     */
    private HashMap<String, List<T>> dependTaskMap = new HashMap<String, List<T>>();

    /**
     * 外部因对此流程存在依赖而注册的回调表
     */
    private HashMap<String, RestTask.DependCallBack> dependencies = new HashMap<String, RestTask.DependCallBack>();

    /**
     * 整个流程的执行结果
     */
    private boolean result = true;

    /**
     * 执行情况反馈回调接口
     */
    public interface IAsyncTaskCallback<T> {

        void oneTaskComplete(T t, boolean value);

        void allTaskComplete(List<Mark<T>> marks, boolean value);

    }

    /**
     * 公共构造
     *
     * @param asyncTaskCallback 执行情况反馈回调
     */
    public AsyncProcess(IAsyncTaskCallback<T> asyncTaskCallback) {
        this.asyncTaskCallback = asyncTaskCallback;
    }

    /**
     * 添加异步任务
     *
     * @param task 异步任务
     * @return 流程
     */
    public AsyncProcess addTask(T task) {
        add(task);
        return this;
    }

    /**
     * 添加存在依赖异步任务
     *
     * @param task      异步任务
     * @param dependKey 依赖键
     * @return 流程
     */
    public AsyncProcess addTask(T task, String dependKey) {
        Log.i(TAG, "addTask dependKey : " + dependKey + " , task : " + task);
        List dependList = dependTaskMap.get(dependKey);
        if (dependList == null) {
            dependList = new ArrayList<T>();
        }
        boolean createDependResult = dependList.add(task);

        if (createDependResult) {
            dependTaskMap.put(dependKey, dependList);
            add(task);
        }
        return this;
    }

    /**
     * 判断是否为存在依赖的任务
     *
     * @param r 任务
     * @return
     */
    public boolean isDependTask(T r) {
        Log.i(TAG, "isDependTask");
        Collection<List<T>> allDepends = dependTaskMap.values();
        for (List<T> depends : allDepends) {
            Log.i(TAG, "isDependTask depends size : " + depends.size());
            if (depends == null) {
                continue;
            }
            if (depends.contains(r)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 执行所有任务
     */
    public void executeAll() {
        execute(true);
    }

    /**
     * 执行所有不存在依赖的任务
     */
    public void executeWithoutDepend() {
        execute(false);
    }

    /**
     * 执行任务
     *
     * @param isAll 是否执行所有任务
     */
    private final void execute(boolean isAll) {
        synchronized (this) {
            if (markers == null || markers.isEmpty()) {
                return;
            }
            for (Mark<T> mark : markers) {
                if (mark == null) {
                    continue;
                }
                if (mark.t == null || (!isAll && isDependTask(mark.t))) {
                    continue;
                }
                /**
                 * 子任务在AsyncTask内置的缺省线程池中异步执行
                 */
                mark.t.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                //((RestTask)mark.t).execute();
            }
        }
    }

    /**
     * 获得任务数量
     *
     * @return 任务数量
     */
    public int getSize() {
        synchronized (this) {
            if (markers == null) {
                return 0;
            }
            return markers.size();
        }
    }

    /**
     * 停止流程
     */
    public void cancel() {
        synchronized (this) {
            if (markers == null || markers.isEmpty()) {
                return;
            }
            for (Mark<T> mark : markers) {
                if (mark == null) {
                    continue;
                }
                if (!mark.t.isCancelled()) {
                    mark.t.cancel(true);
                }
            }
        }
    }

    /**
     * 建立依赖
     *
     * @param key        依赖键
     * @param dependency 依赖
     */
    public void addDependencies(String key, RestTask.DependCallBack dependency) {
        dependencies.put(key, dependency);
    }

    /**
     * 依赖条件满足,通知所有对该流程存在依赖的回调
     */
    public void disposeDependencies() {
        Iterator iterator = dependencies.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, RestTask.DependCallBack> entry = (Map.Entry<String, RestTask.DependCallBack>) iterator.next();
            entry.getValue().onDependencyCreated(entry.getKey());
        }
    }

    /**
     * 匹配方法
     *
     * @param internal 内部对象
     * @param external 外部对象
     * @return 是否匹配
     */
    @Override
    public boolean compare(T internal, T external) {
        return internal.equals(external);
    }

    /**
     * 对依赖条件满足后回调方法的实现,具体为启动对应的依赖任务
     *
     * @param dependKey 依赖键
     */
    @Override
    public void onDependencyCreated(String dependKey) {
        List<T> dependTasks = dependTaskMap.get(dependKey);
        if (dependTasks == null || dependTasks.isEmpty()) {
            return;
        }
        for (T task : dependTasks) {
            if (task == null) {
                continue;
            }
            /**
             *子任务在AsyncTask内置的缺省线程池中异步执行
             */
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    /**
     * 流程中子任务执行情况的反馈
     *
     * @param restTask 子任务
     * @param value    执行结果
     */
    @Override
    public void onComplete(T restTask, Boolean value) {
        Log.i(TAG, "onComplete mark");
        mark(restTask);
        result &= value;
        if (asyncTaskCallback != null) {
            asyncTaskCallback.oneTaskComplete(restTask, value);
        }
        if (isAllMark()){
            disposeDependencies();
            if (asyncTaskCallback != null) {
                asyncTaskCallback.allTaskComplete(getMarkers(), result);
            }
        }
    }
}