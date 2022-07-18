package com.rainchain.arclight.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * @author 慕北_Innocent
 * @version 1.0
 * @date 2022/2/12 20:20
 */
public class DesUtils {
    //这是使用DES加密算法所需要的key——RainChain 2022.01.15
    private static final byte[] DES_KEY = {1, 41, 113, -111, 73, 101, -91, -53};

    //密码的加密DES算法——RainChain 2022.01.15)
    public static String encryptBasedDes(String password) {
        String encryptedPassword;
        try {

            SecureRandom sr = new SecureRandom();

            DESKeySpec deskey = new DESKeySpec(DES_KEY);

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(deskey);

            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key, sr);

            encryptedPassword = new Base64().encodeToString(cipher.doFinal(password.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("加密失败！ERROR：", e);
        }
        return encryptedPassword;
    }

    public static String decryptBasedDes(String encryptedPassword) {
        String decryptedPassword;
        try {
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec(DES_KEY);

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(deskey);

            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key, sr);

            decryptedPassword = new String(cipher.doFinal(new Base64().decode(encryptedPassword)));
        } catch (Exception e) {
            throw new RuntimeException("解密失败！ERROR:", e);
        }
        return decryptedPassword;
    }
}
