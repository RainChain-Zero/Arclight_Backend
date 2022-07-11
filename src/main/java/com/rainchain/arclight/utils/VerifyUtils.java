package com.rainchain.arclight.utils;


import com.rainchain.arclight.exception.OperationFailException;

import java.util.regex.Pattern;

public class VerifyUtils {
    public static void passwordVerify(String password) {
        boolean digitFlag = false, letterFlag = false;

        //密码合法性判断(英文+数字+至少8位）
        for (int i = 0; i < password.length(); ++i) {
            if (Character.isDigit(password.charAt(i))) {
                digitFlag = true;
            } else if (Character.isLetter(password.charAt(i))) {
                letterFlag = true;
            }
            if (digitFlag && letterFlag) {
                break;
            }
        }
        if (!digitFlag || !letterFlag) {
            throw new OperationFailException("密码至少同时包含1位数字和1位英文字符");
        }
    }

    public static void qqVerify(String qq) {
        if (!Pattern.compile("[0-9]*").matcher(qq).matches()) {
            throw new OperationFailException("不合法的QQ号！");
        }
    }

    public static String decodeQq(String key) {
        String keyDecrypted = DesUtils.decryptBasedDes(Base64Utils.convertToPlain(key));
        return keyDecrypted.substring(0, keyDecrypted.indexOf("arclight"));
    }
}
