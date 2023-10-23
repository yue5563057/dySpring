package com.xfarmer.common.util.weixin.wechat;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class PayCallBackDto {


    @JacksonXmlProperty(localName = "transaction_id")
    String transactionId;
    @JacksonXmlProperty(localName = "nonce_str")
    String nonceStr;
    @JacksonXmlProperty(localName = "bank_type")
    String bankType;
    @JacksonXmlProperty(localName = "open_id")
    String openId;
    String sign;
    @JacksonXmlProperty(localName = "fee_type")
    String feeType;
    @JacksonXmlProperty(localName = "mch_id")
    String mchId;
    @JacksonXmlProperty(localName = "cash_fee")
    String cashFee;
    @JacksonXmlProperty(localName = "out_trade_no")
    String outTradeNo;
    String appid;
    @JacksonXmlProperty(localName = "total_fee")
    String totalFee;
    @JacksonXmlProperty(localName = "result_code")
    String resultCode;
    @JacksonXmlProperty(localName = "time_end")
    String timeEnd;
    @JacksonXmlProperty(localName = "is_subscribe")
    String isSubscribe;
    @JacksonXmlProperty(localName = "return_code")
    String returnCode;


}
