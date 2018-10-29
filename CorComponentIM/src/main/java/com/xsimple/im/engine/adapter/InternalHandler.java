package com.xsimple.im.engine.adapter;

import java.util.List;

/**
 * Created by pengpeng on 17/5/3.
 */

public interface InternalHandler<E> {

    boolean set(List<E> es);

    boolean update(E e);

    boolean insert(E e);

    boolean delete(E e);

}
