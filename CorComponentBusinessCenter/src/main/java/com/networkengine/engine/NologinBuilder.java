package com.networkengine.engine;

import android.content.Context;

/**
 * Created by lXy_pro on 17/7/12.
 */

public class NologinBuilder extends EngineBuilder {
    public NologinBuilder(Context ct) {
        super(ct);
    }

    @Override
    public String setUserName() {
        return "";
    }

    @Override
    public String setPwd() {
        return "";
    }
}
