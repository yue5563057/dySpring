package com.xfarmer.common.util.weixin;

import com.alibaba.fastjson.JSONObject;
import com.xfarmer.common.util.HttpTool;

import java.util.HashMap;
import java.util.Map;

public class CloudFunctionUtils {

    public static void xx(String accessToken, String urlLink, String templateId, String phoneNumber) {
        String url = "https://api.weixin.qq.com/tcb/sendsmsv2?access_token=" + accessToken;
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("env", "cloud1-1grapvsc31626afb");
        hashMap.put("url_link", urlLink);
        hashMap.put("template_id", templateId);
        hashMap.put("template_param_list", "尊敬的极农7110，您的极农账号已开通，微信搜索[极农]或");
        hashMap.put("phone_number_list", "+86" + phoneNumber);
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        HttpTool.doPost(url, stringStringHashMap, JSONObject.toJSONString(hashMap));
    }

}
