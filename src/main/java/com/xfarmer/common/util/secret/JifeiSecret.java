package com.xfarmer.common.util.secret;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class JifeiSecret {
    private static String ALGO = "AES";
    private static String ALGO_MODE = "AES/CBC/NoPadding";

    private JifeiSecret(){}

    static byte[] md5(String source) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source.getBytes());
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static String encrypt(String data, String key, String iv) {
        try {
            byte[] keyMd5 = md5(key);
            byte[] ivMd5 = md5(iv);

            //  byte[] iv = toByteArray(aiv);  // 因为要求IV为16byte，而此处aiv串为32位字符串，所以将32位字符串转为16byte
            Cipher cipher = Cipher.getInstance(ALGO_MODE);
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keyspec = new SecretKeySpec(keyMd5, ALGO);
            IvParameterSpec ivspec = new IvParameterSpec(ivMd5);
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);
            return Base64.encodeBase64String(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //  此函数是将字符串每两个字符合并生成byte数组
    public static byte[] toByteArray(String hexString) {
        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() >> 1];
        int index = 0;
        for (int i = 0; i < hexString.length(); i++) {
            if (index > hexString.length() - 1) {
                return byteArray;
            }
            byte highDit = (byte) (Character.digit(hexString.charAt(index), 16) & 0xFF);
            byte lowDit = (byte) (Character.digit(hexString.charAt(index + 1), 16) & 0xFF);
            byteArray[i] = (byte) (highDit << 4 | lowDit);
            index += 2;
        }
        return byteArray;
    }

    //  此函数是pkcs7padding填充函数
    public static String pkcs7padding(String data) {
        int bs = 16;
        int padding = bs - (data.length() % bs);
        String paddingText = "";
        for (int i = 0; i < padding; i++) {
            paddingText += (char) padding;
        }
        return data + paddingText;
    }


    /**
     * 随机生产6位字符串
     *
     * @return
     */
    public static String randomCreateStr() {
        String result = "";
        for (int i = 0; i < 6; i++) {
            int intVal = (int) (Math.random() * 26 + 97);
            result += (char) intVal;
        }
        return result;
    }

    /**
     * 生成签名数据
     *
     * @param data 待加密的数据
     * @param key  加密使用的key
     * @return
     */
    public static String getSignature(String data, String key) {
        try {
            byte[] keyBytes = key.getBytes();
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : rawHmac) {
                sb.append(byteToHexString(b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String byteToHexString(byte b) {
        char[] digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] ob = new char[2];
        ob[0] = digit[(b >>> 4) & 0X0f];
        ob[1] = digit[b & 0X0F];
        String s = new String(ob);
        return s;
    }
}
