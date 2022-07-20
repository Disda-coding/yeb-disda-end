package com.disda.cowork.utils;

/**
 * @program: cowork-back
 * @description:
 * @author: Disda
 * @create: 2022-07-19 21:07
 */
public class RndUtils {
    public static String generatorCode() {
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        return String.valueOf(code);
    }
}