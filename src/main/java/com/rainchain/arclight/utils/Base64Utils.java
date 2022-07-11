package com.rainchain.arclight.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Utils {
    public static String convertToUrlBase64(String str) {
        return Base64.getUrlEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    public static String convertToNormalBase64(String str){
        return Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    public static String convertToPlain(String b64){
        return new String (Base64.getUrlDecoder().decode(b64), StandardCharsets.UTF_8);
    }
}
