package com.xfarmer.common.util.weixin.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Map;

@Data
public class GzhTemplate {

    @JSONField(name = "touser")
    private String toUser;
    @JSONField(name = "template_id")
    private String templateId;
    @JSONField(name = "url")
    private String url;
    @JSONField(name = "miniprogram")
    private GzhTemplateMiniprogram miniProgram;
    @JSONField(name = "data")
    private Object data;


    /**
     * 推送模板
     *
     * @param toUser      推送人
     * @param templateId 模板id
     * @param data        模板内容
     */
    public GzhTemplate(String toUser, String templateId, GzhTemplateData data) {
        this.toUser = toUser;
        this.templateId = templateId;
        this.data = data;
    }

    /**
     * 推送模板消息
     *
     * @param toUser      推送人
     * @param templateId 模板id
     * @param url         跳转外部链接
     * @param data        模板内容
     */
    public GzhTemplate(String toUser, String templateId, String url, GzhTemplateData data) {
        this.toUser = toUser;
        this.templateId = templateId;
        this.url = url;
        this.data = data;
    }

    /**
     * 推送模板消息
     *
     * @param toUser      推送人
     * @param templateId 模板id
     * @param miniProgram 小程序链接
     * @param data        模板内容
     */
    public GzhTemplate(String toUser, String templateId, GzhTemplateMiniprogram miniProgram, GzhTemplateData data) {
        this.toUser = toUser;
        this.templateId = templateId;
        this.miniProgram = miniProgram;
        this.data = data;
    }

    /**
     * 推送模板消息
     *
     * @param toUser      推送人
     * @param templateId 模板id
     * @param url         跳转外部链接
     * @param miniProgram 小程序链接
     * @param data        模板内容
     */
    public GzhTemplate(String toUser, String templateId, String url, GzhTemplateMiniprogram miniProgram, GzhTemplateData data) {
        this.toUser = toUser;
        this.templateId = templateId;
        this.url = url;
        this.miniProgram = miniProgram;
        this.data = data;
    }


    /**
     * 推送模板
     *
     * @param toUser      推送人
     * @param templateId 模板id
     * @param data        模板内容
     */
    public GzhTemplate(String toUser, String templateId, Map<String, GzhTemplateData.Value> data) {
        this.toUser = toUser;
        this.templateId = templateId;
        this.data = data;
    }

    /**
     * 推送模板消息
     *
     * @param toUser      推送人
     * @param templateId 模板id
     * @param url         跳转外部链接
     * @param data        模板内容
     */
    public GzhTemplate(String toUser, String templateId, String url, Map<String, GzhTemplateData.Value> data) {
        this.toUser = toUser;
        this.templateId = templateId;
        this.url = url;
        this.data = data;
    }

    /**
     * 推送模板消息
     *
     * @param toUser      推送人
     * @param templateId 模板id
     * @param miniProgram 小程序链接
     * @param data        模板内容
     */
    public GzhTemplate(String toUser, String templateId, GzhTemplateMiniprogram miniProgram, Map<String, GzhTemplateData.Value> data) {
        this.toUser = toUser;
        this.templateId = templateId;
        this.miniProgram = miniProgram;
        this.data = data;
    }

    /**
     * 推送模板消息
     *
     * @param toUser      推送人
     * @param templateId 模板id
     * @param url         跳转外部链接
     * @param miniProgram 小程序链接
     * @param data        模板内容
     */
    public GzhTemplate(String toUser, String templateId, String url, GzhTemplateMiniprogram miniProgram, Map<String, GzhTemplateData.Value> data) {
        this.toUser = toUser;
        this.templateId = templateId;
        this.url = url;
        this.miniProgram = miniProgram;
        this.data = data;
    }


}
