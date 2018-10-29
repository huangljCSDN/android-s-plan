package com.networkengine.httpApi.encrypt;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.networkengine.httpApi.encrypt.aes.AESOperator;
import com.networkengine.httpApi.encrypt.rsa.RSAOperator;

public class EncryptCenter {
    public static final String ETYPE_AEM = "AEM";
    public static final String ETYPE_REM = "REM";
    public static final String ETYPE_DEM = "DEM";

    private static String DEFAULTS_TYPE;

    public static void setDefaultsType(String sType) {
        EncryptCenter.DEFAULTS_TYPE = sType;
    }

    public static String getType() {
        return TextUtils.isEmpty(DEFAULTS_TYPE) ? "" : DEFAULTS_TYPE;
    }

    public static String encrtpy(String source, String type) {
        IEncrypt encrypImpl = Factory.getEncryptImpl(type);
        if (encrypImpl == null) {
            return source;
        }
        try {
            return encrypImpl.encrypt(source.getBytes());
        } catch (Exception e) {
            Log.e("hh", "encrtpy### " + e.getMessage());
            return source;
        }
    }

    public static String decrypt(String source, String type) {
        IEncrypt encrypImpl = Factory.getEncryptImpl(type);
        if (encrypImpl == null) {
            return source;
        }
        try {
            return encrypImpl.decrypt(Base64.decode(source, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
            return source;
        }
    }

    static class Factory {
        public static IEncrypt getEncryptImpl(String type) {
            if (TextUtils.isEmpty(type)) {
                type = DEFAULTS_TYPE;
            }
            if (ETYPE_AEM.equals(type)) {
                return new AESOperator();
            } else if (ETYPE_REM.equals(type)) {
                return new RSAOperator();

            } else {
                return null;
            }
        }
    }

}
