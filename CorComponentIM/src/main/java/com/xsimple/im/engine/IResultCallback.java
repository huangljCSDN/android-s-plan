package com.xsimple.im.engine;

public interface IResultCallback<SuccessResult, FailResult> {

    void success(SuccessResult result);

    void fail(FailResult failInfo);
}
