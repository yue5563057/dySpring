package com.xfarmer.common.util.weixin.wechat;

import lombok.Data;

@Data
public class PayConfigDto {


    String appId;
    String timeStamp;
    String nonceStr;
    String packageStr;
    String signType;
    String paySign;
    String orderId;


}
