package com.xfarmer.common.util.weixin;

import com.xfarmer.common.util.HttpTool;
import com.xfarmer.common.util.OtherUtil;
import com.xfarmer.common.util.StringUtil;
import com.xfarmer.common.util.secret.Md5Util;
import com.xfarmer.common.util.weixin.wechat.PayConfigDto;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 微信支付
 */
public class WechatPayUtil {

    private WechatPayUtil() {

    }

    private static String unifiedOrderUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 获取支付凭证
     *
     * @param money     支付金额
     * @param notifyUrl 回调地址
     * @param payName   支付名称
     * @param orderId   订单号码
     * @param openId    支付用户
     * @return
     */
    public static PayConfigDto paySign(BigDecimal money, String notifyUrl, String spBillCreateIp, String payName, String orderId, String openId,
                                       String appId, String mchId, String wxKey

    ) {
        int d = money.multiply(BigDecimal.valueOf(100.00)).intValue();
        Map<String, String> params = new HashMap<>();
        params.put("appid", appId);// 应用ID
        params.put("mch_id", mchId); // 微信商户的ID
        params.put("body", payName); // 设置商品的名称 不要超过50个字符串
        params.put("out_trade_no", orderId);// 生成订单ID
        params.put("total_fee", d + "");// 支付的金额 以分为单位
        params.put("spbill_create_ip", spBillCreateIp);
        params.put("openid", openId);
        params.put("trade_type", "JSAPI");
        double random = Math.random();
        String nonceStr = Md5Util.encrypt32("XFARMERPAY" + random);
        params.put("nonce_str", nonceStr);
        params.put("notify_url", notifyUrl); // 设置回调的URL 地址
        String sign = createSign(params, wxKey);
        params.put("sign", sign);
        String map1 = OtherUtil.mapToXml(params);
        String xmlResult = HttpTool.doPost(unifiedOrderUrl, new HashMap<>(), map1);
        try {
            Map<String, String> map = OtherUtil.getMapFromXml(xmlResult);
            String prepayId = map.get("prepay_id");
            Map<String, String> packageParams = new HashMap<>();
            packageParams.put("appId", appId);
            packageParams.put("timeStamp", System.currentTimeMillis() / 1000 + "");
            packageParams.put("nonceStr", nonceStr);
            packageParams.put("package", "prepay_id=" + prepayId);
            packageParams.put("signType", "MD5");
            String packageSign = createSign(packageParams, wxKey);
            PayConfigDto payConfigDto = new PayConfigDto();
            payConfigDto.setAppId(appId);
            payConfigDto.setTimeStamp(System.currentTimeMillis() / 1000 + "");
            payConfigDto.setNonceStr(nonceStr);
            payConfigDto.setPackageStr("prepay_id=" + prepayId);
            payConfigDto.setSignType("MD5");
            payConfigDto.setPaySign(packageSign);
            payConfigDto.setOrderId(orderId);
            return payConfigDto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String createSign(Map<String, String> params, String paternerKey) {
        params.remove("sign");
        String stringA = packageSign(params, false);
        String stringSignTemp = stringA + "&key=" + paternerKey;
        return Md5Util.encrypt32(stringSignTemp).toUpperCase();
    }

    public static String packageSign(Map<String, String> params, boolean urlEncoder) {
        TreeMap<String, String> sortedParams = new TreeMap<>(params);
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> stringStringEntry : sortedParams.entrySet()) {
            String value = stringStringEntry.getValue();
            if (StringUtil.isNotNull(value)) {
                if (first) {
                    first = false;
                } else {
                    sb.append("&");
                }
                sb.append(stringStringEntry.getKey()).append("=");
                if (urlEncoder) {
                    value = urlEncode(value);
                }

                sb.append(value);
            }
        }

        return sb.toString();
    }

    public static String urlEncode(String src) {
        return URLEncoder.encode(src, StandardCharsets.UTF_8).replace("+", "%20");
    }

}
