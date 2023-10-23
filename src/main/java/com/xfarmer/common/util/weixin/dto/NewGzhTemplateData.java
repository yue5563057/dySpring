package com.xfarmer.common.util.weixin.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class NewGzhTemplateData {

    Map<String, NewValue> map;

    public void putValue(String name, NewValue value) {
        if (this.map == null) {
            this.map = new HashMap<>();
        }
        this.map.put(name, value);
    }




    @Data
    public static class NewValue {

        @JSONField(name = "value")
        String valueStr;

        String color;

        /**
         * 设置值 默认黑色
         *
         * @param valueStr 值
         */
        public NewValue(String valueStr) {
            this.valueStr = valueStr;
        }

        /**
         * 值+颜色
         *
         * @param valueStr 值
         * @param color    字体颜色
         */
        public NewValue(String valueStr, String color) {
            this.valueStr = valueStr;
            this.color = color;
        }
    }
}


