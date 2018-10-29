package com.xsimple.im.engine.adapter;

import com.xsimple.im.cache.IMCache;
import com.xsimple.im.db.datatable.IMUser;

import java.util.List;

/**
 * Created by pengpeng on 17/5/3.
 */

public class IMAdapter<T> {

    private IMUserAdapter<T> mIMUserAdapter;

    private IMCache mIMCache;

    public IMAdapter(IMCache imCache, IMUserAdapter<T> mIMUserAdapter) throws IMBaseAdapter.AdapterException {
        if(mIMUserAdapter == null){
            throw new IMBaseAdapter.AdapterException(IMBaseAdapter.AdapterException.ECODE_CONVERTER_CONSTRUCTOR_ERROR, "IMConverter constructor fail mIMUserAdapter or mIMMessageConverter is null!");
        }
        this.mIMCache = imCache;
        this.mIMUserAdapter = mIMUserAdapter;
        this.mIMUserAdapter.setInternalHandler(getIMUserHandler());
    }

    private InternalHandler<IMUser> getIMUserHandler(){
        return new InternalHandler<IMUser>() {
            @Override
            public boolean set(List<IMUser> imUsers) {
                return mIMCache.insertAllUser(imUsers);
            }

            @Override
            public boolean update(IMUser imUser) {
                return mIMCache.insertOrReplaceUser(imUser);
            }

            @Override
            public boolean insert(IMUser imUser) {
                return mIMCache.insertOrReplaceUser(imUser);
            }

            @Override
            public boolean delete(IMUser imUser) {
                return mIMCache.deleteUser(imUser);
            }
        };
    }

    public IMUserAdapter<T> getIMUserAdapter() {
        return mIMUserAdapter;
    }

    public void setIMUserAdapter(IMUserAdapter<T> mIMUserAdapter) {
        this.mIMUserAdapter = mIMUserAdapter;
    }
}
