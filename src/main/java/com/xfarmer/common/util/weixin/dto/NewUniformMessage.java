package com.xfarmer.common.util.weixin.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class NewUniformMessage {

    String touser;

    @JSONField(name = "mp_template_msg")
    NewMpTemplateMsg mpTemplateMsg;
}
