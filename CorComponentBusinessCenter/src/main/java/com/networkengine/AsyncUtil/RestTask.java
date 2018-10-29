package com.networkengine.AsyncUtil;

import android.os.AsyncTask;

import java.util.concurrent.Executor;

/**
 * Rest耗时任务
 *
 * 该类为AsyncTask的子类,对耗时操作doInBackground维持抽象,实现了onPostExecute方法去触发监听,
 * 让外部产生感应,例如AsyncProcess在回调时对Task进行标记。
 */

public abstract class RestTask<M, Result> extends AsyncTask<Void, Integer, Result> {

  /**
   * 任务监听
   */
  TaskListener taskComplete;

  /**
   * 扩展数据
   */
  M mTag;

  /**
   * 任务监听
   *
   * @param <T> AsyncTask泛型
   */
  public interface TaskListener<T extends RestTask, Result> {
    void onComplete(T t, Result vale);
  }

  /**
   * 依赖回调接口
   */
  public interface DependCallBack {
    void onDependencyCreated(String key);
  }

  /**
   * 公共构造
   *
   * @param taskComplete 任务监听
   */
  public RestTask(TaskListener taskComplete) {
    this.taskComplete = taskComplete;
  }

  /**
   * 任务完成回调,不允许子类重写
   *
   * @param result
   */
  @Override
  protected final void onPostExecute(Result result) {
    super.onPostExecute(result);

    if (taskComplete != null) {
      taskComplete.onComplete(this, result);
    }
  }

  /**
   * 避免java.lang.Object[] cannot be cast to java.lang.Void[]
   */
  public void execute() {
    super.execute();
  }

  public void executeOnExecutor(Executor executor) {
    super.executeOnExecutor(executor);
  }

  public M getmTag() {
    return mTag;
  }

  public void setmTag(M mTag) {
    this.mTag = mTag;
  }
}