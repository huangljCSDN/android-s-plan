package com.xsimple.im.engine;

import android.content.Context;

import com.networkengine.entity.LoginEntity;
import com.xsimple.im.cache.IMCache;
import com.xsimple.im.engine.adapter.IMAdapter;
import com.xsimple.im.engine.adapter.IMBaseAdapter;
import com.xsimple.im.engine.adapter.IMUserAdapter;

/**
 * Created by pengpeng on 17/5/11.
 */

public class IMProvider {

    private IMCache mIMCache;

    private IMAdapter mIMConverter;

    public IMProvider(IMCache imCache, IMUserAdapter<?> imUserConverter) throws IMBaseAdapter.AdapterException {
        this.mIMCache = imCache;
        this.mIMConverter = new IMAdapter(imCache, imUserConverter);
    }

    public LoginEntity getLoginResult(){
        return mIMCache.getmLoginEntity();
    }

    public IMEngine getIMEngine(Context context){
        return IMEngine.getInstance(context);
    }

    public IMAdapter getIMConverter() {
        return mIMConverter;
    }

    public void setIMConverter(IMAdapter mIMConverter) {
        this.mIMConverter = mIMConverter;
    }

}
