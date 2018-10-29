package com.networkengine.httpApi.ssl;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public class SSLHelper {

    final static String CORACLE_CRT = "-----BEGIN CERTIFICATE-----\n" +
            "MIIFHzCCBAegAwIBAgIQZ+IXbzHD9iwt2B3cAB0ttzANBgkqhkiG9w0BAQsFADBS\n" +
            "MQswCQYDVQQGEwJDTjEaMBgGA1UEChMRV29TaWduIENBIExpbWl0ZWQxJzAlBgNV\n" +
            "BAMTHldvU2lnbiBDbGFzcyAzIE9WIFNlcnZlciBDQSBHMjAeFw0xNjA0MDEwMjAx\n" +
            "MDJaFw0xODA2MDEwMjAxMDJaMIGLMQswCQYDVQQGEwJDTjESMBAGA1UECAwJ5bm/\n" +
            "5Lic55yBMRIwEAYDVQQHDAnmt7HlnLPluIIxPDA6BgNVBAoMM+a3seWcs+W4guWJ\n" +
            "jea1t+WchuiIn+e9kee7nOenkeaKgOiCoeS7veaciemZkOWFrOWPuDEWMBQGA1UE\n" +
            "AwwNKi5jb3JhY2xlLmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEB\n" +
            "AMCSqruV2VoaB15Y0RxbGFrzBAxxCkn+4dklT3dvGrWTjGKQ6CRFq5cRrgq7dgDV\n" +
            "9gWXGApzL+nD4RnWaSYoJsVRbCFz1rzp/Ju593rVZU5Dn6bWG1SMFD5Pi+ucXGwE\n" +
            "gaOvXMD2NEy4Qkir6BuMAXm4t8bn8caa2NB8GmnWBaz3CnMkHF3H3yNICyK3rBgh\n" +
            "hs1302R+ow8EuntdgLoh3js0G+TSwNTOXh9jMr2j7UekgrsGNYwq5RuoHMBf6nYm\n" +
            "ZbI17ytq2x0wT6F+lC3loxvlWEIjoj2sEQnaz95hTp2bg6w/VhxBmvkY3B0oSCm5\n" +
            "IUDKeszQQu/qWJvpIf4rfdMCAwEAAaOCAbUwggGxMA4GA1UdDwEB/wQEAwIFoDAd\n" +
            "BgNVHSUEFjAUBggrBgEFBQcDAgYIKwYBBQUHAwEwCQYDVR0TBAIwADAdBgNVHQ4E\n" +
            "FgQUet9rsfyVQa7PEBtFAzExDPfu/acwHwYDVR0jBBgwFoAU+YvsBDhqP6oGxpSt\n" +
            "c5UqsMjmuPswcwYIKwYBBQUHAQEEZzBlMC8GCCsGAQUFBzABhiNodHRwOi8vb2Nz\n" +
            "cDEud29zaWduLmNvbS9jYTYvc2VydmVyMzAyBggrBgEFBQcwAoYmaHR0cDovL2Fp\n" +
            "YTEud29zaWduLmNvbS9jYTYuc2VydmVyMy5jZXIwOAYDVR0fBDEwLzAtoCugKYYn\n" +
            "aHR0cDovL2NybHMxLndvc2lnbi5jb20vY2E2LXNlcnZlcjMuY3JsMDUGA1UdEQQu\n" +
            "MCyCDSouY29yYWNsZS5jb22CC2NvcmFjbGUuY29tgg4qLmtpbmdub2RlLmNvbTBP\n" +
            "BgNVHSAESDBGMAgGBmeBDAECAjA6BgsrBgEEAYKbUQEBAjArMCkGCCsGAQUFBwIB\n" +
            "Fh1odHRwOi8vd3d3Lndvc2lnbi5jb20vcG9saWN5LzANBgkqhkiG9w0BAQsFAAOC\n" +
            "AQEAUWSIdmF7eRlJcvQeoOUeZp6o+gGB5a2Xg6JhtsW8PckeJvT6Bf87RL4m3QNl\n" +
            "ASj9gDzfEfZsEoWG1HHH021qjaHWeeveWo43aLQ3bA4CUdeao8o6uXjfBYNSQokm\n" +
            "7YWBwE05SGfF1agDDfAx3/2wGWca3gnmsB9zByU8XgWyfUO8zSVLk/c/akygRJLg\n" +
            "trP1/XUk5NHeRvt1/PprXK4rFW5nmCM2bhsdGGxbGtHLhq7C0SIE3VpKgnYDydPT\n" +
            "S9e/ZRYZSCdA0+xVMDDa0az6c5RQkdWmyq1GzIxBKHlYdTmys+M0nSX2l1ZnsHHU\n" +
            "ZjXw9ryeOjhZlqqmeK/sKmxFUQ==\n" +
            "-----END CERTIFICATE-----\n" +
            "\n" +
            "-----BEGIN CERTIFICATE-----\n" +
            "MIIFozCCA4ugAwIBAgIQdZbCPvqJWUVuefcXus9k8zANBgkqhkiG9w0BAQsFADBV\n" +
            "MQswCQYDVQQGEwJDTjEaMBgGA1UEChMRV29TaWduIENBIExpbWl0ZWQxKjAoBgNV\n" +
            "BAMTIUNlcnRpZmljYXRpb24gQXV0aG9yaXR5IG9mIFdvU2lnbjAeFw0xNDExMDgw\n" +
            "MDU4NThaFw0yOTExMDgwMDU4NThaMFIxCzAJBgNVBAYTAkNOMRowGAYDVQQKExFX\n" +
            "b1NpZ24gQ0EgTGltaXRlZDEnMCUGA1UEAxMeV29TaWduIENsYXNzIDMgT1YgU2Vy\n" +
            "dmVyIENBIEcyMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1nSHr5nA\n" +
            "V5aZwol0PJJVmb8fBwA1BSaWFlsDwUI3M74/DU//u5QmkdcUFngb9xOiS0zlXKcQ\n" +
            "QDVZMNF3meOdKcK+MZW9kmFbsCP7Z1jVUuR7L/BzHHOUVbrIaFkCEBDk9xHww7bX\n" +
            "rlaAAJ5lZKaDkUHm7ad6ZaUfMC4TPL/fY5fzlvBSMrT0e5hX7TZP9yFKKJ3dHJKz\n" +
            "TY2cWIsXIdjcobeuc3iKxLbpfyiOmtUunjnp2ll048iXEDKUGVnUD4lXROblKxcw\n" +
            "YlKYf6sNpQHqBEHK+hMOO4cGur1HMddjAwH0vqE3EZ8eAZVODz9UHpKmnzCM/pjo\n" +
            "VpZmBOE1/lmsVwIDAQABo4IBcDCCAWwwDgYDVR0PAQH/BAQDAgEGMB0GA1UdJQQW\n" +
            "MBQGCCsGAQUFBwMCBggrBgEFBQcDATASBgNVHRMBAf8ECDAGAQH/AgEAMDAGA1Ud\n" +
            "HwQpMCcwJaAjoCGGH2h0dHA6Ly9jcmxzMS53b3NpZ24uY29tL2NhMS5jcmwwbQYI\n" +
            "KwYBBQUHAQEEYTBfMCcGCCsGAQUFBzABhhtodHRwOi8vb2NzcDEud29zaWduLmNv\n" +
            "bS9jYTEwNAYIKwYBBQUHMAKGKGh0dHA6Ly9haWExLndvc2lnbi5jb20vY2ExZzIt\n" +
            "c2VydmVyMy5jZXIwHQYDVR0OBBYEFPmL7AQ4aj+qBsaUrXOVKrDI5rj7MB8GA1Ud\n" +
            "IwQYMBaAFOFmzw7R8bNLtwYgFP6HEtX2/vs+MEYGA1UdIAQ/MD0wOwYMKwYBBAGC\n" +
            "m1EGAwIBMCswKQYIKwYBBQUHAgEWHWh0dHA6Ly93d3cud29zaWduLmNvbS9wb2xp\n" +
            "Y3kvMA0GCSqGSIb3DQEBCwUAA4ICAQBeZ7p4MgW2t6/n3mp6gmQOoAvynpq6xitv\n" +
            "Vjq0YlerfK1gUJY0nKOIz9mPUK/28AA2Gx8fh1U8YJrwsA2agC2KO74Fs9eggLa4\n" +
            "GetR2+xkVPEaiUpIoU0/MX3EeZRL8d6rg69fhr6WHLM+HOe8lrLoWqy1WMs8Vm8K\n" +
            "p6XQNomCJoy5H7brj354/FuLeRzW30enVvSYTsep1Q51VgZ/tDdGCMbpT4tbQxzg\n" +
            "RT6VIHHAHJgW7/J436xNu79WDs+Fr8+/BO1ya/0fVw5YkUQRWDtiOwl4s6R1auyz\n" +
            "wisyzLONw6Nu3IrV6ErEC3vbMF2VM8PRo2lkW6iqlkhzc+PJuSTfF3Wqrwc6z76b\n" +
            "ioCnv3zi6Srm/bAs5+bmfrM1FWUA9OE5cw4oS/AMmJ466857ep5AwVBllprnS3fN\n" +
            "3ct9l7TqCbLpSSjDMOCHFfAm6tgD/ezaCINl3HfFbj0094fDHB0mM+wzrMaZU6tg\n" +
            "9LDZ7mRaMwdwE3SIB/WG+RjTskfIrgNKU94cZdYKLjpRk+63428K++n+Tui7HcKX\n" +
            "qwq57TYyG02hzAOmnbPZHNVn4o90PJIqdLFWUN9TFdch1uvz+2PjICwKdDcLwaE1\n" +
            "aoRw9EX4sraBSar9VEWQTecEB194FN06uyv5clDsaOo8qNGAu741Q5fDMrL1qq3J\n" +
            "f4OffWkeFQ==\n" +
            "-----END CERTIFICATE-----\n" +
            "\n" +
            "-----BEGIN CERTIFICATE-----\n" +
            "MIIGXDCCBESgAwIBAgIHGcKFMOk7NjANBgkqhkiG9w0BAQsFADB9MQswCQYDVQQG\n" +
            "EwJJTDEWMBQGA1UEChMNU3RhcnRDb20gTHRkLjErMCkGA1UECxMiU2VjdXJlIERp\n" +
            "Z2l0YWwgQ2VydGlmaWNhdGUgU2lnbmluZzEpMCcGA1UEAxMgU3RhcnRDb20gQ2Vy\n" +
            "dGlmaWNhdGlvbiBBdXRob3JpdHkwHhcNMDYwOTE3MjI0NjM2WhcNMTkxMjMxMjM1\n" +
            "OTU5WjBVMQswCQYDVQQGEwJDTjEaMBgGA1UEChMRV29TaWduIENBIExpbWl0ZWQx\n" +
            "KjAoBgNVBAMTIUNlcnRpZmljYXRpb24gQXV0aG9yaXR5IG9mIFdvU2lnbjCCAiIw\n" +
            "DQYJKoZIhvcNAQEBBQADggIPADCCAgoCggIBAL3Kjay4kRVWl3trXHrC3mvZobDD\n" +
            "ECP6p6GyzDH6PtmmKW8WPeBr+LhAX9s5qAB6i6BNVH3CInj8jgm4qIXXzJWXS3TY\n" +
            "nn7wAOQOia5JKEQaEJkyDyWIU6QNsw8SCBYLA3EnHH/h29L9Z2jEBV0KDl1w19iX\n" +
            "oLxTQZqRjfSeNmZ6flbBkF/msWggNqSMJCwsRwtZdmYwtb7e7Y/4ndO7ATDm8vMO\n" +
            "4CySgPOF+SiKtFQumu33dvwVaBbrSmzrLhKP1M/+DMdcHQt+BTK+XrAJKkLVyU6Q\n" +
            "s1kNu3p+zdUIWrR/2BxpEfknD3sGr1SDGHvh3VR6UWhud/zGv1JKZkahsmcau6NP\n" +
            "d6C+Xf/8VgtDcneQyp758jn1Dan06tfnsxAvMEI3IcwwcMmGmA/MWE2Du33lGqU3\n" +
            "jbasMpcAOmNxJB6eN8T/dNQ3wOL+iEZgEd0IP1A2q7h6pJViam6wymohWmnz8/sd\n" +
            "cDmV86dupoGJoYjFO3HKo1Lug7v9oHf05G/nQtttSpmKNEi8F9zkgAgitvIxwD8E\n" +
            "PuufIHnWuAZkZAIx16nNUvuERWkJACrcVYvEBkZLwEodCVs5KP2pq84A+S5ISybm\n" +
            "MEylWMq0RIJP55EeM8Owk/8R/IHSyh9xKd12T5Ilrx2Btw8vjMMGzC8no0rkDpm6\n" +
            "fB5FH3+qGUWW/fw9AgMBAAGjggEHMIIBAzASBgNVHRMBAf8ECDAGAQH/AgECMA4G\n" +
            "A1UdDwEB/wQEAwIBBjAdBgNVHQ4EFgQU4WbPDtHxs0u3BiAU/ocS1fb++z4wHwYD\n" +
            "VR0jBBgwFoAUTgvvGqRAW6UXaYcwyjRoQ9BBrvIwaQYIKwYBBQUHAQEEXTBbMCcG\n" +
            "CCsGAQUFBzABhhtodHRwOi8vb2NzcC5zdGFydHNzbC5jb20vY2EwMAYIKwYBBQUH\n" +
            "MAKGJGh0dHA6Ly9haWEuc3RhcnRzc2wuY29tL2NlcnRzL2NhLmNydDAyBgNVHR8E\n" +
            "KzApMCegJaAjhiFodHRwOi8vY3JsLnN0YXJ0c3NsLmNvbS9zZnNjYS5jcmwwDQYJ\n" +
            "KoZIhvcNAQELBQADggIBALZt+HD74g1MmLMHSRX1BMRsysr1aKAI/hJtnAQGya2a\n" +
            "kVI+eMRc7p9UHe7j8V4wyUnhOeCmnTZsV/rmNE9V6IeoLN0F8VgSkejKzih4j98H\n" +
            "hQGl3EWWBdSAsisFmsuapYvgOmfmc0e+Sv0nsYjv5srPjQ4mn/pfV3itbf6umzUI\n" +
            "scO6wQBKS30Uvffx01UYrNAzcIhtxAlxFKYrT4iB5wsAN6kVfX7XAZY/L697Yq4K\n" +
            "Sr9LOS41EIv+BDnkPDoMCVZAOrX0wmgMtflSze6d+Jj8eOdYR48cc1hpM6v/3d+O\n" +
            "JAF3mBk6sGZ5vOEIow5PwQSz8wHI69NZHDXSkx5wZYJ/28/7yJkSYMNEbzqAS9e+\n" +
            "IaoUemTL3TdDRVsyLkXw2VkfaxjwfOlVNhlhX7V98Y29iOR1S5jdJ7DkhEQqYYRX\n" +
            "BYIRH6o1WPMgDq9Z7/pVcnINJtCbU0mszjcuZWH/9uwb6vbxptPRtXu+NfQiwbyN\n" +
            "Ab1oXoMNL+zW2mMMJ9FUPuSo085LMriRlP/7W0ktdRiounGaO67ZwKlPh5Hti3tr\n" +
            "IJiJOYNPgMRpzBfJyE6+5KmlgXZwBgQyzYNl9Lx9PhO80uhvY6q1O9qNhjKCeJ3Z\n" +
            "zP+/V2R07Sg9RGIVYUv3lLANKmcc8MubpZK/+EFawT1g7Z+7uG2bzqlqFj9+6gbx\n" +
            "-----END CERTIFICATE-----\n";

    public static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory socketFactory = null;
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");

            sslContext.init(null, new TrustManager[]{new TrustCerts()}, new SecureRandom());

            socketFactory = sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return socketFactory;

        // 加入到信任列表, 有问题呆排查
//        SSLSocketFactory sslSocketFactory = null;
//        try {
//            // InputStream inputStream = context.getAssets().open("cor.crt");
//            InputStream inputStream = new Buffer().writeUtf8(CORACLE_CRT).inputStream();
//            // StringBufferInputStream tsIn = new StringBufferInputStream(CORACLE_CRT);//加载证书
//
//            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
//            X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
//            String alias = certificate.getSubjectX500Principal().getName();
//
//            String defaultType = KeyStore.getDefaultType();
//            KeyStore trustStore = KeyStore.getInstance(defaultType);
//            trustStore.load(null, null);
//            trustStore.setCertificateEntry(alias, certificate);
//            inputStream.close();
//
//            // 服务器端需要验证的客户端证书，其实就是客户端的keystore
//            TrustManagerFactory trustManagerFactory = TrustManagerFactory
//                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
//            trustManagerFactory.init(trustStore);
//
//            //初始化SSLContext
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//
//            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
//
//            sslSocketFactory = sslContext.getSocketFactory();
//
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
//
//        return sslSocketFactory;
    }

}
