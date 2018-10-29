package com.xsimple.im.engine.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengpeng on 17/5/3.
 */

public abstract class IMBaseAdapter<Internal, External> {

    /**
     * 内部对象缓存集合
     */
    public List<Internal> internals = new ArrayList<>();

    /**
     * 内部处理器
     */
    public InternalHandler<Internal> mInternalHandler;

    public IMBaseAdapter() {
    }

    /**
     * 构造
     * @param sources
     * @throws AdapterException
     */
    public IMBaseAdapter(List<External> sources) throws AdapterException {
        setInternals(sources);
    }

    /**
     * 设置内部处理器
     * @param mInternalHandler
     * @throws AdapterException
     */
    public void setInternalHandler(InternalHandler<Internal> mInternalHandler) throws AdapterException {

        if(mInternalHandler == null){
            throw new AdapterException(AdapterException.ECODE_CONVERTER_SET_HANDLER_ERROR, "ContactAdapter setInternalHandler fail mInternalHandler is null!");
        }
        this.mInternalHandler = mInternalHandler;

        mInternalHandler.set(internals);
    }

    /**
     * 初始化内部数据
     * @param sources
     * @throws AdapterException
     */
    public void setInternals(List<External> sources) throws AdapterException {
        if(sources == null || sources.isEmpty()){
            throw new AdapterException(AdapterException.ECODE_CONVERTER_INIT_ERROR, "ContactAdapter setInternals fail sources is null!");
        }

        List<Internal> internals = new ArrayList<Internal>();

        for (External external : sources){
            if(external == null){
                throw new AdapterException(AdapterException.ECODE_CONVERTER_INIT_ERROR, "ContactAdapter setInternals fail, null item in source! ");
            }
            internals.add(transform(external));
        }

        this.internals = internals;

    }

    /**
     * 数据更新
     * @param sources
     * @return
     * @throws AdapterException
     */
    public boolean notifyDataSetChanged(List<External> sources) throws AdapterException {

        if(sources == null){
            return false;
        }

        setInternals(sources);

        if(mInternalHandler != null){
            return mInternalHandler.set(internals);
        }
        return false;
    }

    /**
     * 删除
     * @param external
     * @return
     * @throws AdapterException
     */
    public boolean delete(External external) throws AdapterException {
        if(external == null){
            return false;
        }

        if(mInternalHandler == null){
            return false;
        }

        Internal internal = findInternalByExternal(external);

        return mInternalHandler.delete(internal);
    }

    /**
     * 插入或更新
     * @param external
     * @return
     * @throws AdapterException
     */
    public boolean insertOrUpdate(External external) throws AdapterException {

        if(external == null){
           return false;
        }

        if(mInternalHandler == null){
            return false;
        }

        Internal internal = findInternalByExternal(external);

        if(internal == null){
            return mInternalHandler.insert(transform(external));
        }else {
            return mInternalHandler.update(internal);
        }

    }

    /**
     * 寻找内部对象
     * @param external
     * @return
     * @throws AdapterException
     */
    private Internal findInternalByExternal(External external) throws AdapterException {
        if(external == null){
            return null;
        }

        if(internals == null || internals.isEmpty()){
            return null;
        }
        for (Internal internal : internals){
            if(internal == null){
                throw new AdapterException(AdapterException.ECODE_CONVERTER_FIND_ERROR, "ContactAdapter setInternals fail, null item in source! ");
            }

            if(matching(internal, external)){
                return internal;
            }
        }

        return null;
    }

    /**
     * 匹配
     * @param internal
     * @param external
     * @return
     */
    public abstract boolean matching(Internal internal, External external);

    /**
     * 转换
     * @param external
     * @return
     */
    public abstract Internal transform(External external);

    public abstract External transto(Internal internal);

    public static class AdapterException extends Exception{

        public static final int ECODE_DE_ERROR = 0;
        public static final int ECODE_CONVERTER_INIT_ERROR = 1;
        public static final int ECODE_CONVERTER_FIND_ERROR = 2;
        public static final int ECODE_CONVERTER_SET_HANDLER_ERROR = 3;
        public static final int ECODE_CONVERTER_CONSTRUCTOR_ERROR = 4;

        private int mCode = ECODE_DE_ERROR;

        public AdapterException(int code, String message) {
            super(message);
            mCode = code;
        }

        public int getCode() {
            return mCode;
        }
    }
}
