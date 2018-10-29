package com.networkengine.httpApi.encrypt;

/**
 * Created by pengpeng on 17/5/2.
 */

public interface IEncrypt {

    String encrypt(byte[] source) throws Exception;

    String decrypt(byte[] encryptedData) throws Exception;

}
