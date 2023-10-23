package com.xfarmer.common.util.secret;

import com.xfarmer.common.exception.MyException;

import java.security.MessageDigest;

/**
 * @author 东岳
 */
public class Md5Util {

    private Md5Util(){

    }
    private Md5Util instance ;

    public synchronized Md5Util getInstance(){
        if(instance==null){
            instance = new Md5Util();
        }
        return instance;
    }

    public static String encrypt32(String encryptStr) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(encryptStr.getBytes());
            StringBuilder hexValue = new StringBuilder();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            encryptStr = hexValue.toString();
        } catch (Exception e) {
            throw new MyException(e.toString());
        }
        return encryptStr;
    }


    public static String encrypt16(String encryptStr) {
        return encrypt32(encryptStr).substring(8, 24);
    }

}
