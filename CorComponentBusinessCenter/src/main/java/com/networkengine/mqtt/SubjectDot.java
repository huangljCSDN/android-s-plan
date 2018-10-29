package com.networkengine.mqtt;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 信息点：创建一个临时的信息点，可以向这个信息中心添加订阅 例如IM 中的 UIObserver，当需要时 信息点向监听者广播信息
 * Created by pengpeng on 17/3/30.
 */

public abstract class SubjectDot<Key, Observer, Notice> {
//    public HashMap<Key, Observer> mUIObservers = new HashMap<>();

    public HashMap<Key, FilterObserver<Observer, Notice>> observers = new HashMap<>();

    public void attach(Key key, Observer observer, Filter<Notice>... filter) {

        FilterObserver<Observer, Notice> filterObserver;

        if (filter == null || filter.length == 0) {
            filterObserver = new FilterObserver<>(observer);
        } else {
            filterObserver = new FilterObserver<>(observer, filter);
        }

        observers.put(key, filterObserver);
    }

    public void notice(Notice notices) {
        Iterator<FilterObserver<Observer, Notice>> it = observers.values().iterator();
        while (it.hasNext()) {
            FilterObserver<Observer, Notice> filterObserver = it.next();
            if (filterObserver == null) {
                continue;
            }
            execute(filterObserver.observer, filterObserver.processFilter(notices));
        }
    }

    public void notice(Key key, Notice notices) {
        FilterObserver<Observer, Notice> filterObserver = observers.get(key);
        if (filterObserver == null) {
            return;
        }
        execute(filterObserver.observer, filterObserver.processFilter(notices));
    }

    public boolean containsKey(Key key) {
        return observers.containsKey(key);
    }

    public void dettach(Key key) {
        observers.remove(key);
    }

//    public void dettach(Key key) {
//        mUIObservers.remove(key);
//    }

//    public void attach(Key key, Observer observer) {
//        mUIObservers.put(key, observer);
//    }

//    public void notice(Notice notices) {
//        Iterator<Observer> it = mUIObservers.values().iterator();
//        while (it.hasNext()) {
//            Observer observer = it.next();
//            if (observer == null) {
//                continue;
//            }
//            execute(observer, notices);
//        }
//    }


//    public void notice(Key key, Notice notices) {
//
//        Observer observer = mUIObservers.get(key);
//        if (observer == null)
//            return;
//        execute(observer, notices);
//    }

    public abstract void execute(Observer observer, Notice notices);

    public static class FilterObserver<Observer, DataType> {
        private List<Filter<DataType>> filters;
        private Observer observer;

        public FilterObserver(Observer observer, Filter<DataType>[] filters) {
            this.filters = Arrays.asList(filters);
            this.observer = observer;
        }

        public FilterObserver(Observer observer) {
            this.observer = observer;
        }

        public List<Filter<DataType>> getFilters() {
            return filters;
        }

        public void setFilters(Filter<DataType>[] filterArrays) {
            this.filters = Arrays.asList(filterArrays);
        }

        public void setFilters(List<Filter<DataType>> filters) {
            this.filters = filters;
        }

        public Observer getObserver() {
            return observer;
        }

        public void setObserver(Observer observer) {
            this.observer = observer;
        }

        public DataType processFilter(DataType notice) {

            if (filters == null) {
                return notice;
            }
            Iterator<Filter<DataType>> iterator = filters.iterator();
            if (!iterator.hasNext()) {
                return notice;
            }

            return processFilter(iterator, notice);
        }

        private DataType processFilter(Iterator<Filter<DataType>> iterator, DataType notice) {
            if (iterator == null) {
                return notice;
            }
            if (iterator.hasNext()) {
                DataType dataType = iterator.next().process(notice);
                return processFilter(iterator, dataType);
            }

            return notice;
        }
    }

    public interface Filter<Org> {
        Org process(Org org);
    }

}
