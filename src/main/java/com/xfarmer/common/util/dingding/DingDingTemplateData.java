package com.xfarmer.common.util.dingding;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class DingDingTemplateData {

    @JSONField(name = "dd_from")
    private String ddFrom = "Xfarmer";
    /**模板Id*/
    @JSONField(name = "template_id")
    private String templateId ;
    /**用户Id*/
    @JSONField(name = "userid")
    private String userId;
    /**内容*/
    private List<DingDingTemplateContent> contents;
    @JSONField(name = "to_chat")
    /**发送日志到员工时是否发送单聊消息*/
    private boolean toChat = true;
    @JSONField(name = "to_cids")
    /**日志要发送到的群ID*/
    private String toCids;
    @JSONField(name = "to_userids")
    /**日志发送到的员工userid*/
    private List<String> toUserIds;


    public DingDingTemplateData(String templateId,String userId,List<DingDingTemplateContent> dingDingTemplateContents){
        this.templateId = templateId;
        this.userId=userId;
        this.contents=dingDingTemplateContents;
    }

}

