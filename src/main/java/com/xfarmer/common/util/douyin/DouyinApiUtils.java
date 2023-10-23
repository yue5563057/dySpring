package com.xfarmer.common.util.douyin;

import com.alibaba.fastjson.JSON;
import com.xfarmer.common.util.HttpTool;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * 抖音工具类
 */
public class DouyinApiUtils {

    @Value("${spring.dyBase.douyin.appId}")
    private static String appId ;
    @Value("${spring.dyBase.douyin.secret}")
    private static String secret ;

    private static final String TOKEN_URL="https://developer.toutiao.com/api/apps/v2/token";

    public static String getToken(){
        Map<String,String > param = new HashMap<>();
        param.put("appid",appId);
        param.put("secret",secret);
        param.put("grant_type","client_credential");
        String s = HttpTool.doPostJson(TOKEN_URL, JSON.toJSONString(param));
        System.out.println(s);
        return s;
    }

}
