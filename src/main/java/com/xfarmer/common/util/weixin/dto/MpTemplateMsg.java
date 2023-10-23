package com.xfarmer.common.util.weixin.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class MpTemplateMsg {
    @JSONField(name = "appid")
    private String appId;
    @JSONField(name = "template_id")
    private String templateId;
    private String url;
    /**
     * 公众号模板消息所要跳转的小程序，小程序的必须与公众号具有绑定关系
     */
    private GzhTemplateMiniprogram miniprogram;
    private GzhTemplateData data;



}
