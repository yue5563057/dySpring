package com.xfarmer.common.util.secret;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Base64Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;

/**
 * AES加密工具类，tPlus采用的加密方法
 *
 * @author: paul
 * @create: 2021/06/16
 **/
public class AESUtils {


    /**
     * aes加密
     *
     * @param str 需要加密的字符串
     * @param key 加密的秘钥
     * @return 加密后的字符串
     * @throws Exception
     */
    public static String aesEncrypt(String str, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        if (str == null || key == null) {
            return null;
        }
        Cipher cipher = Cipher.getInstance(ALGORITHM5);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES"));
        byte[] bytes = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(bytes);
    }


    /**
     * aes加密
     *
     * @param str 需要加密的字符串
     * @param key 加密的秘钥
     * @param vi  偏移量
     * @return 加密后的字符串
     * @throws Exception
     */
    public static String aesEncrypt(String str, String key, String vi) throws Exception {
        if (str == null || key == null) {
            return null;
        }
        Cipher cipher = Cipher.getInstance(ALGORITHM5);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec iv = new IvParameterSpec(vi.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
        byte[] bytes = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(bytes);
    }

    /**
     * aes解密
     *
     * @param str 需要解密的字符串
     * @param key 解密的秘钥
     * @return 解密后的字符串
     * @throws Exception
     */
    public static String aesDecrypt(String str, String key) throws Exception {
        if (str == null || key == null) {
            return null;
        }
        Cipher cipher = Cipher.getInstance(ALGORITHM5);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES"));
        byte[] bytes = Base64.decodeBase64(str);
        bytes = cipher.doFinal(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    // 加密模式
    public static final String ALGORITHM = "AES/ECB/PKCS7Padding";
    // 加密模式
    public static final String ALGORITHM5 = "AES/ECB/PKCS5Padding";

    /**
     *      * 加密
     *      * @PARAM content
     *      * @PARAM key
     *      * @return
     *      
     */
    public static String encryptPkcs7(String content, String key, String vi) {
        byte[] result = null;
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(vi.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, paramSpec);
            result = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Base64.encodeBase64String(result);
    }


    /**
     * BASE64解密
     *
     * @throws Exception
     */
    public static String encryptBase64(String key) {
        return new String(Base64Utils.decodeFromString(key));
    }

    /**
     * BASE64加密
     *
     * @return
     */
    public static String encryptBase64(byte[] key) {
        return new String(Base64Utils.encode(key));
    }


}
