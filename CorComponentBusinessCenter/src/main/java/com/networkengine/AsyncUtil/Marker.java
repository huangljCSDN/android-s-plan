package com.networkengine.AsyncUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 标记器
 *
 * 该类起到了对象标记的作用,可以对所有需要标记的对象集合附加标记功能,只能进行是与否两种情况的简单标记,
 * 不支持多状态标记,并且要求对象集合为同一类型,使用场景 界面多选,任务记录等
 */

public abstract class Marker<T> {

  /**
   * 通过结构定义,建立对象与标记之间的绑定关系
   * @param <T>
   */
  public static class Mark<T> {
    public T t;

    public boolean value = false;

    public Mark(T t) {
      this.t = t;
    }
  }

  /**
   * 标记集合
   */
  ArrayList<Mark<T>> markers = new ArrayList<Mark<T>>();

  /**
   * 匹配方法
   * @param internal
   * @param external
   * @return
   */
  public abstract boolean compare(T internal, T external);

  /**
   * 缺省构造
   */
  public Marker() {

  }

  /**
   * 构造
   * @param datas 对象结合
   */
  public Marker(List<T> datas) {
    init(datas);
  }

  /**
   * 初始化
   * @param datas
   */
  private void init(List<T> datas) {
    synchronized (this) {
      if (datas == null) {
        return;
      }
      for (T t : datas) {
        markers.add(new Mark<T>(t));
      }
    }
  }

  /**
   * 添加对象元素
   * @param t 对象
   */
  public void add(T t) {
    synchronized (this) {
      if (markers == null) {
        markers = new ArrayList<>();
      }
      markers.add(new Mark<T>(t));
    }
  }

  /**
   * 标记所有
   * @return 是否全部标记
   */
  public boolean markAll() {
    synchronized (this) {
      if (markers == null) {
        return false;
      }

      for (Mark mark : markers) {
        mark.value = true;
      }
      return true;
    }
  }

  /**
   * 单个标记
   * @param t 对象
   * @return
   */
  public boolean mark(T t) {
    return mark(t, true);
  }

  /**
   * 取消标记
   * @param t 对象
   * @return
   */
  public boolean unMark(T t) {
    return mark(t, false);
  }

  public boolean isAllMark() {
    synchronized (this) {
      for (Mark mark : markers) {
        if (!mark.value) {
          return false;
        }
      }
      return true;
    }
  }

  /**
   * 双向标记
   * @param t 对象
   * @param value 标记为true或false
   * @return
   */
  private boolean mark(T t, boolean value) {
    synchronized (this) {
      if (markers == null) {
        return false;
      }
      for (Mark<T> mark : markers) {
        if (compare(mark.t, t)) {
          mark.value = value;
          return true;
        }
      }
      return false;
    }
  }

  public ArrayList<Mark<T>> getMarkers() {
    return markers;
  }
}