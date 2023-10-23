package com.xfarmer.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xfarmer.common.exception.MyException;
import com.xfarmer.common.util.secret.AESUtils;
import com.xfarmer.common.util.secret.Md5Util;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class StringUtil {

    private StringUtil() {

    }

    /**
     * 判断字符串是否为空或空值
     *
     * @param str
     * @return
     */
    public static boolean isNull(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 判断字符串是否为空或空值
     *
     * @param str
     * @return
     */
    public static boolean isNotNull(String str) {
        return str != null && str.length() > 0;
    }

    /**
     * 生成UUID(去掉中间横杠)
     *
     * @return
     */
    public static String generId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成6位数字字符串
     *
     * @return
     */
    public static String get6Code() {
        Random random = null;
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            result.append(random.nextInt(10));
        }
        return result.toString();
    }

    private static String encode = "xfarmer";

    /**
     * 平台用户密码加密
     * 2次MD5
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public static String md5(String username, String password) {
        String md5String = stringToMd5(username + password + encode);
        return stringToMd5(md5String);
    }


    static void digui(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            String[] list = file.list();
            for (String s : list) {
                digui(new File(file.getAbsolutePath() + "/" + s));
            }
        }
    }


    public static String stringToMd5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new MyException("没有这个md5算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);

        if (md5code.length() != 32) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 32 - md5code.length(); i++) {
                sb.append("0").append(md5code);
            }
            return sb.toString();
        } else {
            return md5code;
        }

    }


    /**
     * 解密用户密码
     *
     * @param code code
     * @return
     */
    public static Map<String, String> decodeUserInfo(String code) throws Exception {
        //第一步解密base64解密code
        String base64 = AESUtils.encryptBase64(code);
        JSONObject parseObject = JSON.parseObject(base64);
        String username = parseObject.getString("username");
        String password = parseObject.getString("password");
        String numberCode = parseObject.getString("code");
        if (StringUtil.isNull(username) || StringUtil.isNull(password)) {
            //用户名或密码为空
            throw new MyException("用户密码参数错误");
        }
        String s = stringToMd5(username);
        String key = s.substring(0, 16);
        //得到真实的password
        String realPassword = AESUtils.aesDecrypt(password, key);
        //封装真实数据
        Map<String, String> user = new HashMap<>();
        user.put("username", username);
        user.put("password", realPassword);
        user.put("code", numberCode);
        return user;
    }

    /**
     * UUID中间-换成none字段
     *
     * @param none 替换UUID -
     * @return
     */
    public static String getSessionId(String none) {
        String s = UUID.randomUUID().toString().replaceAll("\\\\-", none);
        return Md5Util.encrypt32(s);
    }


    public static String substringBeforeLast(String str, String separator) {
        if (!isNull(str) && !isNull(separator)) {
            int pos = str.lastIndexOf(separator);
            return pos == -1 ? str : str.substring(0, pos);
        } else {
            return str;
        }
    }
}
