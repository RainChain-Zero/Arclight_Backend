package com.rainchain.arclight.utils;


import org.mozilla.universalchardet.Constants;

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
        String res = CharsetDetectUtil.detect(buffer);
        if (null == res || res.equals(Constants.CHARSET_GB18030)) {
            return new String(buffer, "GB18030");
        } else {
            return new String(buffer, StandardCharsets.UTF_8);
        }
    }
}
