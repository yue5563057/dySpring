package com.xfarmer.common.constant;

/**
 * @author 东岳
 */
public enum FarmerInfoSearchEnum {
    /**
     * 农户名称
     */
    INFO_FAMRE_NAME("info.farmer_name", "info.farmer_name"),
    /**
     * 农户ID
     */
    INFO_ID("info.id", "info.id"),
    /**
     * 农户电话
     */
    INFO_PHONE_NUMBER("info.phone_number", "info.phone_number"),
    /**
     * 农户省份
     */
    INFO_PROVINCE_ID("info.province_id", "info.province_id"),
    /**
     * 农户城市
     */
    INFO_CITY_ID("info.city_id", "info.city_id"),
    /**
     * 农户区域
     */
    INFO_AREA_ID("info.area_id", "info.area_id"),
    /**
     * 农户更新时间
     */
    INFO_UPDATE_TIME("info.update_time", "info.update_time"),
    /**
     * 农户是否删除
     */
    INFO_IS_DEL("info.is_del", "info.is_del"),
    /**
     * 农户合作社中间表--合作社id
     */
    TWUF_COOPERATION_ID("twuf.cooperation_id", "twuf.cooperation_id"),

    /**农户总分*/
    INFO_TOTAL_SCORE("info.total_score", "info.total_score");

    FarmerInfoSearchEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
