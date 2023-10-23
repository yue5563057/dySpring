package com.xfarmer.common.util.dingding;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author 东岳
 */
@Data
public class DingDingTemplateContent {

    Integer sort;
    Integer type;
    @JSONField(name = "content_type")
    String contentType;
    String content;
    String key;

     public DingDingTemplateContent(Integer sort,Integer type,String content,String contentType,String key){
         this.sort =sort;
         this.type=type;
         this.content=content;
         this.contentType=contentType;
         this.key=key;
     }






}
