package com.rainchain.arclight.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;


public class DesUtils {

    private static final byte[] DES_KEY = {};

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
