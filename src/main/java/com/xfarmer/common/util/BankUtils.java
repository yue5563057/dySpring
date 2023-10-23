package com.xfarmer.common.util;

import com.alibaba.fastjson.JSONObject;
import com.xfarmer.common.constant.URLCodeConst;

import java.util.HashMap;
import java.util.Map;

public class BankUtils {


    /**
     * 银行卡信息查询
     *
     * @param acctName    姓名
     * @param acctPan     银行卡号
     * @param certId      身份证号码
     * @param phoneNumber 预留手机号
     */
    public static ApiResponse<Object> checkBank(String acctName, String acctPan, String certId, String phoneNumber) {
        String host = "https://lundroid.market.alicloudapi.com";
        String path = "/lianzhuo/verifi";
        String appcode = "077a661395c340488fe5349f81852b2a";
        if (StringUtil.isNull(acctName) ||
                StringUtil.isNull(acctPan) ||
                StringUtil.isNull(certId) ||
                StringUtil.isNull(phoneNumber)
        ) {
            return new ApiResponse<>(URLCodeConst.PARAM_ERROR, "参数错误！");
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<>();
        querys.put("acct_name", acctName);
        querys.put("acct_pan", acctPan);
        querys.put("cert_id", certId);
        querys.put("phone_num", phoneNumber);
        String get = HttpTool.doGet(host + path, querys, headers);
        JSONObject jsonObject = JSONObject.parseObject(get);
        JSONObject resp = jsonObject.getJSONObject("resp");
        if (resp != null) {
            if (0 == resp.getInteger("code")) {
                return new ApiResponse<>(URLCodeConst.SUCCESS, "查询成功!");
            }
            return new ApiResponse<>(URLCodeConst.INTERFACE_ERROR, resp.getString("desc"));
        }
        return new ApiResponse<>(URLCodeConst.INTERFACE_ERROR, "银行卡信息查询失败!");
    }
}
