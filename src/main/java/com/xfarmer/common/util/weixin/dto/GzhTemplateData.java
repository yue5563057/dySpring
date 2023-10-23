package com.xfarmer.common.util.weixin.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class GzhTemplateData {

    Value first;

    Value keyword1;

    Value keyword2;

    Value keyword3;

    Value keyword4;

    Value keyword5;


    Value remark;


    public GzhTemplateData(Value first, Value keyword1, Value keyword2, Value keyword3, Value keyword4, Value keyword5, Value remark) {
        this.first = first;
        this.keyword1 = keyword1;
        this.keyword2 = keyword2;
        this.keyword3 = keyword3;
        this.keyword4 = keyword4;
        this.keyword5 = keyword5;
        this.remark = remark;
    }

    public GzhTemplateData(Value first, Value keyword1, Value keyword2, Value keyword3, Value keyword4, Value remark) {
        this.first = first;
        this.keyword1 = keyword1;
        this.keyword2 = keyword2;
        this.keyword3 = keyword3;
        this.keyword4 = keyword4;
        this.remark = remark;
    }


    public GzhTemplateData(Value first, Value keyword1, Value keyword2, Value keyword3, Value remark) {
        this.first = first;
        this.keyword1 = keyword1;
        this.keyword2 = keyword2;
        this.keyword3 = keyword3;
        this.remark = remark;
    }


    public GzhTemplateData(Value first, Value keyword1, Value keyword2, Value remark) {
        this.first = first;
        this.keyword1 = keyword1;
        this.keyword2 = keyword2;
        this.remark = remark;
    }

    public GzhTemplateData(Value first, Value keyword1, Value remark) {
        this.first = first;
        this.keyword1 = keyword1;
        this.remark = remark;
    }

    public GzhTemplateData(Value first, Value remark) {
        this.first = first;
        this.remark = remark;
    }

    @Data
    public static class Value {
        @JSONField(name = "value")
        String valueStr;

        String color;

        /**
         * 设置值 默认黑色
         *
         * @param valueStr 值
         */
        public Value(String valueStr) {
            this.valueStr = valueStr;
        }

        /**
         * 值+颜色
         *
         * @param valueStr 值
         * @param color 字体颜色
         */
        public Value(String valueStr, String color) {
            this.valueStr = valueStr;
            this.color = color;
        }
    }
}
