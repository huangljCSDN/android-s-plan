package com.networkengine.httpApi.encrypt.aes;

import android.util.Base64;

import com.networkengine.httpApi.encrypt.IEncrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * AES 是一种可逆加密算法，对用户的敏感信息加密处理 对原始数据进行AES加密后，在进行Base64编码转化；
 */
public class AESOperator implements IEncrypt {

    /*
     * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
     */
    private static String sKey = "coracle012345678";
    private static String ivParameter = "0392039203920300";


    /**
     * @param source sSrc.getBytes("utf-8")
     * @return
     * @throws Exception
     */
    @Override
    public String encrypt(byte[] source) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());//
        // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(source);
        return Base64.encodeToString(encrypted, Base64.DEFAULT);// 此处使用BASE64做转码。
    }

    /**
     * @param encryptedData Base64.decode(sSrc, Base64.DEFAULT);
     * @return
     * @throws Exception
     */
    @Override
    public String decrypt(byte[] encryptedData) throws Exception {
        byte[] raw = sKey.getBytes("ASCII");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] original = cipher.doFinal(encryptedData);
        return new String(original, "utf-8");
    }
}