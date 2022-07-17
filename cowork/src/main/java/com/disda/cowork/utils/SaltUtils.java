
package com.disda.cowork.utils;

import org.springframework.context.annotation.Bean;

import java.util.Random;

/**
 * @program: cowork-back
 * @description: 生成盐
 * @author: Disda
 * @create: 2022-01-29 19:21
 */
public class SaltUtils {
    public static String generateSalt(int length){
        StringBuilder sb = new StringBuilder();
        String lib = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i=0;i<length;i++){
            sb.append(lib.charAt(new Random().nextInt(lib.length())));
        }
        return sb.toString();
    }


}