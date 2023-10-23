package com.xfarmer.common.util.weixin.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class UniformMessage {




    String touser;

    @JSONField(name = "mp_template_msg")
    MpTemplateMsg mpTemplateMsg;

}
