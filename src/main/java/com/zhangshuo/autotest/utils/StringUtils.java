package com.zhangshuo.autotest.utils;

public class StringUtils {


    public static String firstUpperCase(String str){
        char[] chars = str.toCharArray();
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] = (char)(chars[0] - 32);
        }
        return new String(chars);
    }
}
