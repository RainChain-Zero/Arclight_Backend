package com.rainchain.arclight.utils;


import org.mozilla.universalchardet.UniversalDetector;

public class CharsetDetectUtil {
    public static String detect(byte[] content) {
        UniversalDetector detector = new UniversalDetector(null);

        detector.handleData(content, 0, content.length);

        detector.dataEnd();

        return detector.getDetectedCharset();
    }
}
