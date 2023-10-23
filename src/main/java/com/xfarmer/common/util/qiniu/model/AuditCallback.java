package com.xfarmer.common.util.qiniu.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AuditCallback {
    private String id;
    private String pipeline;
    private int code;
    private String desc;
    private String reqid;
    private String inputBucket;
    private String inputKey;
    private List<FopItem> items;

    @Data
    public static class FopItem {
        private String cmd;
        private int code;
        private String desc;
        private FopItemResult result;
        @JsonProperty("returnOld")
        private int returnOld;
    }

    @Data
    public static class FopItemResult {
        private boolean disable;
        private FopItemResultDetail result;
    }

    @Data
    public static class FopItemResultDetail {
        private int code;
        private String message;
        private FopItemResultScenes scenes;
        private String suggestion;
    }

    @Data
    public static class FopItemResultScenes {
        private FopItemResultScene politician;
        private FopItemResultScene pulp;
        private FopItemResultScene terror;
    }

    @Data
    public static class FopItemResultScene {
        private FopItemResultLabelResult result;
        private String suggestion;
    }

    @Data
    public static class FopItemResultLabelResult {
        private String label;
        private double score;
    }
}
