package com.xfarmer.common.util.weixin.tools;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 东岳
 */
public class MessageUtil {
    /**
     * 返回消息类型：文本
     */
    public static final String RESP_MESSAGE_TYPE_TEXT = "text";

    /**
     * 返回消息类型：音乐
     */
    public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

    /**
     * 返回消息类型：图文
     */
    public static final String RESP_MESSAGE_TYPE_NEWS = "news";

    /**
     * 请求消息类型：文本
     */
    public static final String REQ_MESSAGE_TYPE_TEXT = "text";

    /**
     * 请求消息类型：图片
     */
    public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

    /**
     * 请求消息类型：链接
     */
    public static final String REQ_MESSAGE_TYPE_LINK = "link";

    /**
     * 请求消息类型：地理位置
     */
    public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

    /**
     * 请求消息类型：音频
     */
    public static final String REQ_MESSAGE_TYPE_VOICE = "voice";

    /**
     * 请求消息类型：推送
     */
    public static final String REQ_MESSAGE_TYPE_EVENT = "event";

    /**
     * 事件类型：subscribe(订阅)
     */
    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

    /**
     * 事件类型：unsubscribe(取消订阅)
     */
    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

    /**
     * 事件类型：CLICK(自定义菜单点击事件)
     */
    public static final String EVENT_TYPE_CLICK = "CLICK";

    /**
     * 事件类型：taskcard_click(点击任务卡片按钮)
     */
    public static final String EVENT_TYPE_TASKCARD_CLICK = "taskcard_click";


    /**
     * 事件类型：open_approval_change(审批状态通知事件)
     */
    public static final String EVENT_TYPE_OPEN_APPROVAL_CHANGE = "sys_approval_change";

    public static final String EVENT_TYPE_ENTER_AGENT = "enter_agent";

    /**
     * 解析微信发来的请求（XML）
     *
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> parseXml(String msg) throws Exception {
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<>();
        // 从request中取得输入流
        InputStream inputStream = new ByteArrayInputStream(msg.getBytes(StandardCharsets.UTF_8));
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();
        // 遍历所有子节点
        for (Element e : elementList) {
            map.put(e.getName(), e.getText());
        }
        // 释放资源
        inputStream.close();
        inputStream = null;
        return map;
    }

    /**
     * org.dom4j.Document 转  com.alibaba.fastjson.JSONObject
     *
     * @param xml
     * @return
     * @throws DocumentException
     */
    public static JSONObject documentToJsonObject(String xml) throws DocumentException {
        return elementToJsonObject(strToDocument(xml).getRootElement());
    }

    /**
     * String 转 org.dom4j.Document
     *
     * @param xml
     * @return
     * @throws DocumentException
     */
    public static Document strToDocument(String xml) throws DocumentException {
        return DocumentHelper.parseText(xml);
    }

    /**
     * org.dom4j.Element 转  com.alibaba.fastjson.JSONObject
     *
     * @param node
     * @return
     */
    public static JSONObject elementToJsonObject(Element node) {
        JSONObject result = new JSONObject();
        // 当前节点的名称、文本内容和属性
        // 当前节点的所有属性的list
        List<Attribute> listAttr = node.attributes();
        // 遍历当前节点的所有属性
        for (Attribute attr : listAttr) {
            result.put(attr.getName(), attr.getValue());
        }
        // 递归遍历当前节点所有的子节点// 所有一级子节点的list
        List<Element> listElement = node.elements();
        if (!listElement.isEmpty()) {
            // 遍历所有一级子节点
            for (Element e : listElement) {
                // 判断一级节点是否有属性和子节点
                if (e.attributes().isEmpty() && e.elements().isEmpty()) {
                    // 沒有则将当前节点作为上级节点的属性对待
                    result.put(e.getName(), e.getTextTrim());
                } else {
                    // 判断父节点是否存在该一级节点名称的属性
                    if (!result.containsKey(e.getName())) {
                        // 没有则创建
                        result.put(e.getName(), new JSONArray());
                    }
                    // 将该一级节点放入该节点名称的属性对应的值中
                    ((JSONArray) result.get(e.getName())).add(elementToJsonObject(e));
                }
            }
        }
        return result;
    }

}
