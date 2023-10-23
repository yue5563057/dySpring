package com.xfarmer.common.util.dingding.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 东岳
 */
@Data
public class DingDingProcessData {



    /**
     * 审批流的唯一码。
     *
     * process_code在审批模板编辑页面的URL中获取。
     * 必填：是
     */
    @JSONField(name = "processCode")
    String processCode;
    /**
     * 发起人id
     * 必填：是
     */
    @JSONField(name = "originatorUserId")
    String originatorUserId;
    /**
     * 发起人所在的部门，如果发起人属于根部门，传-1。
     * 必填：是
     */
    @JSONField(name = "deptId")
    Integer deptId;
    /**
     * 应用标识AgentId
     * 必填：否
     */
    @JSONField(name = "microappAgentId")
    Integer agentId;
    /**
     * 审批人userid列表，最大列表长度20。
     *
     * 多个审批人用逗号分隔，按传入的顺序依次审批。
     * 必填：否
     */
    @JSONField(name = "approvers")
    String approvers;
    /**
     * 抄送人userid列表，最大列表长度20。
     * 必填：否
     */
    @JSONField(name = "ccList")
    List<String> ccList;
    /**
     * 抄送时间点，取值：
     *
     * START：开始时抄送
     *
     * FINISH：结束时抄送
     *
     * START_FINISH：开始和结束时都抄送
     * 必填：否
     */
    @JSONField(name = "ccPosition")
    String ccPosition;

    /**
     * 审批流表单参数，最大列表长度20。
     *
     * 仅支持下表列举的表单控件。
     * 必填：否
     */
    @JSONField(name = "formComponentValues")
    List<DingDingProcessDataItem> formComponentValues;


    public DingDingProcessData addItem(DingDingProcessDataItem selectField) {
        if(this.formComponentValues==null){
            this.formComponentValues = new ArrayList<>();
        }
        this.formComponentValues.add(selectField);
        return this;
    }
}
