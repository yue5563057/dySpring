package com.xfarmer.common.util.dingding.model;

import lombok.Data;

/**
 * @author 东岳
 */
@Data
public class DingDingProcessDataItem {

    /**
     *  控件id。
     *  必填:否
     */
    String id;
    /**
     *  控件别名。
     *  必填:否
     */
    String bizAlias;
    /**
     *  控件名称。
     *  必填:是
     */
    String name;
    /**
     *  控件值。
     *  必填:是
     */
    String value;
    /**
     *  控件扩展值。
     *  必填:否
     */
    String extValue;
    /**
     *  控件类型，取值：
     *
     * TextField：单行输入框
     *
     * TextareaField：多行输入框
     *
     * NumberField：数字输入框
     *
     * DDSelectField：单选框
     *
     * DDMultiSelectField：多选框
     *
     * DDDateField：日期控件
     *
     * DDDateRangeField：时间区间控件
     *
     * TextNote：文字说明控件
     *
     * PhoneField：电话控件
     *
     * DDPhotoField：图片控件
     *
     * MoneyField：金额控件
     *
     * TableField：明细控件
     *
     * DDAttachment：附件
     *
     * InnerContactField：联系人控件
     *
     * RelateField：关联审批单
     *
     * FormRelateField：关联控件
     *
     * AddressField：省市区控件
     *
     * StarRatingField：评分控件
     *
     * DepartmentField：部门控件
     *  必填:否
     */
    String componentType;

    public DingDingProcessDataItem(String id,
                                   String bizAlias,
                                   String name,
                                   String value,
                                   String extValue,String componentType){
        this.id=id;
        this.bizAlias=bizAlias;
        this.name=name;
        this.value=value;
        this.extValue=extValue;
        this.componentType=componentType;
    }


    public DingDingProcessDataItem(String name,
                                   String value,String id){
        this.name=name;
        this.value=value;
        this.id=id;
    }



}
