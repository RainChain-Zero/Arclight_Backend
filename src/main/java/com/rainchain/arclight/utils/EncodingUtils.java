package com.rainchain.arclight.utils;


import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/*
f*ck content-type
f*ck Character encoding
 */
public class EncodingUtils {
    public static String charReader(HttpServletRequest request) throws IOException {
        int len = request.getContentLength();
        ServletInputStream reader = request.getInputStream();
        byte[] buffer = new byte[len];
        reader.read(buffer, 0, len);
        //转换成UTF-8格式
        return new String(buffer, StandardCharsets.UTF_8);
    }
}
