package com.rainchain.arclight.utils;


public class TimeUtils {
    public static String convertToTimeH(String lastTime){
        int lastTimeh = 0;
        if (lastTime.endsWith("d")) {
            lastTimeh = Integer.parseInt(lastTime.replace("d", "")) * 24;
        } else {
            lastTimeh = Integer.parseInt(lastTime.replace("h", ""));
        }
        return String.valueOf(lastTimeh);
    }
}
