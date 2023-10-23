package com.xfarmer.common.util.busi;

import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 东岳
 */
@Component
public class AliMessageUtil {

    private static String ACCESS_KEY_ID;
    private static String ACCESS_KEY_SECRET;



    private static synchronized void setInit(String accessKeyId, String accessKeySecret) {
        ACCESS_KEY_ID = accessKeyId;
        ACCESS_KEY_SECRET = accessKeySecret;
    }

    @Value("${spring.dyBase.aliMessage.key}")
    private String accessKeyId;
    @Value("${spring.dyBase.aliMessage.secret}")
    private String accessKeySecret;


    @PostConstruct
    public void init() {
        setInit(accessKeyId, accessKeySecret);

    }

    private AliMessageUtil() {

    }

    /**
     * 使用AK&SK初始化账号Client
     *
     * @return Client
     * @throws Exception
     */
    public static Client createClient() {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(ACCESS_KEY_ID)
                // 您的AccessKey Secret
                .setAccessKeySecret(ACCESS_KEY_SECRET);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        try {
            return new Client(config);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static SendSmsResponseBody getSendSmsResponseBody(Client client, SendSmsRequest sendSmsRequest) throws Exception {
        if (client == null) {
            SendSmsResponseBody sendSmsResponseBody = new SendSmsResponseBody();
            sendSmsResponseBody.code = "-1";
            sendSmsResponseBody.message = "初始化失败";
            return sendSmsResponseBody;
        }
        SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);
        return sendSmsResponse.body;
    }

    /**
     *  推送消息
     * @param signName 签名
     * @param phoneNumber 电话号码
     * @param templateCode 模板code
     * @param param 参数
     * @return
     */
    public static SendSmsResponseBody pushTemplate(String signName,String phoneNumber,String templateCode,Map<String,String> param) throws Exception {
        Client client = AliMessageUtil.createClient();
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setPhoneNumbers(phoneNumber)
                .setSignName(signName)
                .setTemplateCode(templateCode)
                .setTemplateParam(JSON.toJSONString(param));
        // 复制代码运行请自行打印 API 的返回值
        return getSendSmsResponseBody(client, sendSmsRequest);
    }


    /**
     * 创建供货信息发送推广短信
     *
     * @param phoneNumber 电话号码
     * @return
     */
    public static SendSmsResponseBody sendSupplyInfo(String signName,String phoneNumber) throws Exception {
        Client client = AliMessageUtil.createClient();
        Map<String, String> param = new HashMap<>();
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setPhoneNumbers(phoneNumber)
                .setSignName(signName)
                .setTemplateCode("SMS_263500326")
                .setTemplateParam(JSON.toJSONString(param));
        // 复制代码运行请自行打印 API 的返回值
        return getSendSmsResponseBody(client, sendSmsRequest);
    }


}
