package com.xfarmer.common.util.weixin.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class NewMpTemplateMsg {

    @JSONField(name = "touser")
    private String touser;

    @JSONField(name = "template_id")
    private String templateId;

    @JSONField(name = "appid")
    private String appId;

    private String url;
    /**
     * 公众号模板消息所要跳转的小程序，小程序的必须与公众号具有绑定关系
     */
    @JSONField(name = "miniprogram")
    private GzhTemplateMiniprogram miniprogram;

    @JSONField(serialize =  false)
    private NewGzhTemplateData datas;



//    private String data ;
    @JSONField(name = "data")
    public Map<String, NewGzhTemplateData.NewValue> getData(){
     if(datas==null){
         return new HashMap<>();
     }
     return datas.getMap();
    }
}
