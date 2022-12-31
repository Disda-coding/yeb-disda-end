package com.disda.cowork.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: cowork-back
 * @description: 通过Aes加密
 * @author: Disda
 * @create: 2022-01-29 22:07
 */
@Component
public class AesUtils {


    //使用AES-128-CBC加密模式，key需要为16位,key和iv可以相同，也可以不同!
    private static String KEY;
    private static String IV;

    private static final String CIPHER_ALGORITHM_CBC = "AES/CBC/NoPadding";

    /**
     * 因为静态变量不能直接从配置文件通过@value获取，所以采用此方法
     *
     */


    @Value("${aes.iv}")
    public void aesiv(String iv) {
        IV = iv;
    }

    /**
     * 加密方法 返回base64加密字符串
     * 和前端保持一致
     *
     * @param data 要加密的数据
     * @param key  加密key
     * @return 加密的结果
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        try {
            //"算法/模式/补码方式"NoPadding PKCS5Padding
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
            int blockSize = cipher.getBlockSize();

            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            return new Base64().encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密方法
     *
     * @param data 要解密的数据
     * @param key  解密key
     * @return 解密的结果
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws Exception {
        try {
//        byte[] encrypted1 = new Base64().decode(data);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] original = cipher.doFinal(new Base64().decode(data));
            String originalString = new String(original);
            return originalString.trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用默认的key和iv加密
     *
     *
     * @param s
     * @param KEY
     * @param data
     * @return
     * @throws Exception
     */
    public static String encrypt(String s, String KEY, String data) throws Exception {
        return encrypt(data, AesUtils.KEY, IV);
    }

    /**
     * 使用默认的key和iv解密
     *
     *
     * @param s
     * @param KEY
     * @param data
     * @return
     * @throws Exception
     */
    public static String decrypt(String s, String kEY, String data) throws Exception {
        return decrypt(data, AesUtils.KEY, IV);
    }



}