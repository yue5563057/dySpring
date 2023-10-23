package com.xfarmer.common.util.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xfarmer.common.constant.SystemBizConst;
import com.xfarmer.common.constant.SystemConst;
import com.xfarmer.common.constant.URLCodeConst;
import com.xfarmer.common.exception.MyException;
import com.xfarmer.common.util.ApiResponse;
import com.xfarmer.common.util.HttpTool;
import com.xfarmer.common.util.StringUtil;
import com.xfarmer.common.util.redis.RedisUtil;
import com.xfarmer.common.util.weixin.dto.NewMpTemplateMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author 任东岳
 */
@Component
public class WxApiUtils {

    private static RedisUtil redisUtil;

    @Autowired
    RedisUtil redisUtil1;


    public WxApiUtils() {
    }


    @PostConstruct
    public void init() {
        setRedisUtil(redisUtil1);
    }

    static synchronized void setRedisUtil(RedisUtil redisUtil1) {
        redisUtil = redisUtil1;
    }

    private static String getToken(String appId, String appSecret) {
        if (SystemConst.isDev) {
            Map<String, String> map = new HashMap<>();
            map.put("appId", appId);
            String doGet = HttpTool.doGet("http://api.xfarmer.com/gettok", map);
            JSONObject object = JSON.parseObject(doGet);
            return object.getString("msg");
        } else {
            String accTokenKey = "COOPERATION_ACCESS_TOKEN:" + appId;
            Object o = redisUtil.get(accTokenKey);
            if (o == null) {
                String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token";
                Map<String, String> map = new TreeMap<>();
                map.put(SystemBizConst.WX_GRANT_TYPE, "client_credential");
                map.put(SystemBizConst.WX_APPID, appId);
                map.put(SystemBizConst.WX_SECRET, appSecret);
                String s = HttpTool.doGet(tokenUrl, map);
                JSONObject object = JSON.parseObject(s);
                if (object.getString(SystemBizConst.ACCESS_TOKEN) != null) {
                    String accessToken = object.getString(SystemBizConst.ACCESS_TOKEN);
                    boolean set = redisUtil.set(accTokenKey, accessToken, 7100);
                    if (set) {
                        return accessToken;
                    } else {
                        throw new MyException("获取token失败!");
                    }
                } else {
                    throw new MyException("获取token失败!");
                }
            }
            return (String) o;
        }
    }

    /**
     * 根据openid获取用户的详细信息
     *
     * @param openId openid
     * @return
     */
    public static ApiResponse<JSONObject> findUserInfo(String openId, String appId, String appSecret) {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info";
        Map<String, String> map = new TreeMap<>();
        map.put(SystemBizConst.ACCESS_TOKEN, getToken(appId, appSecret));
        map.put(SystemBizConst.WX_OPENID, openId);
        map.put("lang", "zh_CN");
        String s = HttpTool.doGet(url, map);
        JSONObject jsonObject = JSON.parseObject(s);
        if (jsonObject.getInteger("subscribe") != 0) {
            return new ApiResponse<>(jsonObject);
        } else {
            return new ApiResponse<>(URLCodeConst.FAILED_USER_INFO, "该用户没有关注公众号");
        }
    }


    /**
     * 用code换取openId
     *
     * @param code 登录code
     * @return
     */
    public static ApiResponse<JSONObject> codeToOpenId(String code, String appId, String appSecret) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
        Map<String, String> param = new HashMap<>();
        param.put(SystemBizConst.WX_APPID, appId);
        param.put(SystemBizConst.WX_SECRET, appSecret);
        param.put("code", code);
        param.put(SystemBizConst.WX_GRANT_TYPE, "authorization_code");
        String s = HttpTool.doGet(url, param);
        JSONObject jsonObject = JSON.parseObject(s);
        if (StringUtil.isNotNull(jsonObject.getString(SystemBizConst.WX_OPENID))) {
            return new ApiResponse<>(jsonObject);
        } else {
            return new ApiResponse<>(URLCodeConst.FAILED_USER_INFO, jsonObject.getString(SystemBizConst.WX_ERRMSG));
        }
    }

    /**
     * 用code换取openId
     *
     * @param code 登录code
     * @return
     */
    public static ApiResponse<JSONObject> codeToOpenIdByXapp(String code, String appId, String appSecret) {
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String, String> param = new HashMap<>();
        param.put(SystemBizConst.WX_APPID, appId);
        param.put(SystemBizConst.WX_SECRET, appSecret);
        param.put("js_code", code);
        param.put(SystemBizConst.WX_GRANT_TYPE, "authorization_code");
        String s = HttpTool.doGet(url, param);
        JSONObject jsonObject = JSON.parseObject(s);
        if (StringUtil.isNotNull(jsonObject.getString(SystemBizConst.WX_OPENID))) {
            return new ApiResponse<>(jsonObject);
        } else {
            return new ApiResponse<>(URLCodeConst.FAILED_USER_INFO, jsonObject.getString(SystemBizConst.WX_ERRMSG));
        }
    }

    private static String apiUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";

    /**
     * 获取ticket
     *
     * @return
     */
    public static ApiResponse<String> getTicket(String appId, String appSecret) {

        String key = appId + ":" + "getTicket";
        if (redisUtil.get(key) != null) {
            return new ApiResponse<>(URLCodeConst.SUCCESS, "cuccess", (String) redisUtil.get(key));
        }
        Map<String, String> param = new HashMap<>();
        param.put(SystemBizConst.ACCESS_TOKEN, getToken(appId, appSecret));
        param.put("type", "jsapi");
        String s = HttpTool.doGet(apiUrl, param);
        JSONObject jsonObject = JSON.parseObject(s);
        if (0 == jsonObject.getInteger(SystemBizConst.ERRCODE) && SystemConst.OK.equals(jsonObject.getString(SystemBizConst.WX_ERRMSG))) {
            String ticket = jsonObject.getString("ticket");
            redisUtil.set(key, ticket, 7100);
            return new ApiResponse<>(URLCodeConst.SUCCESS, "cuccess", ticket);
        } else {
            return new ApiResponse<>(jsonObject.getInteger(SystemBizConst.ERRCODE), jsonObject.getString(SystemBizConst.WX_ERRMSG));
        }
    }

    /**
     * 获取临时素材
     *
     * @return
     */
    public static ApiResponse<String> findMedia(String mediaId, String appId, String appSecret) {
        String url = "https://api.weixin.qq.com/cgi-bin/media/get";
        Map<String, String> param = new HashMap<>();
        param.put(SystemBizConst.ACCESS_TOKEN, getToken(appId, appSecret));
        param.put("media_id", mediaId);
        String s = HttpTool.doGet(url, param);
        return new ApiResponse<>(s);
    }


    public static ApiResponse<JSONObject> getUserPhoneNumber(String code, String appId, String appSecret) {
        String url = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + getToken(appId, appSecret);
        Map<String, String> param = new HashMap<>();
        param.put("code", code);
        String s = HttpTool.doPostJson(url, JSON.toJSONString(param));
        JSONObject jsonObject = JSON.parseObject(s);
        if (jsonObject.getInteger(SystemBizConst.ERRCODE) == 0) {
            return new ApiResponse<>(jsonObject);
        }
        return new ApiResponse<>(jsonObject.getInteger(SystemBizConst.ERRCODE), jsonObject.getString("errmsg"));
    }


    /**
     * 获取 URL Link
     */
    public static ApiResponse<JSONObject> generateUrlLink(String path, String query, String appId, String appSecret) {
        String url = "https://api.weixin.qq.com/wxa/generate_urllink?access_token=" + getToken(appId, appSecret);
        Map<String, Object> param = new HashMap<>();
        param.put("path", path);
        param.put("query", query);
        param.put("is_expire", true);
        param.put("expire_type", 1);
        param.put("expire_interval", 1);
        param.put("env_version", "release");
        String s = HttpTool.doPostJson(url, JSON.toJSONString(param));
        JSONObject jsonObject = JSON.parseObject(s);
        if (jsonObject.getInteger(SystemBizConst.ERRCODE) == 0) {
            return new ApiResponse<>(jsonObject);
        }
        return new ApiResponse<>(jsonObject.getInteger(SystemBizConst.ERRCODE), jsonObject.getString("errmsg"));
    }

    public static final String envId = "xfarmer-8ghgedfp386799ca";
    public static final String templateId = "844110";

    public String pushUrlMessage(String templateParam, String phoneNumber, String xappUrl, String query, String appId, String appSecret) {
        String url = "https://api.weixin.qq.com/tcb/sendsmsv2?access_token=" + getToken(appId, appSecret);
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("env", envId);
        hashMap.put("url_link", generateUrlLink(xappUrl, query, appId, appSecret).getData().getString("url_link"));
        hashMap.put("template_id", templateId);
        hashMap.put("template_param_list", templateParam);
//        "尊敬的极农7110，您的极农账号已开通，微信搜索[极农]或"
//       "您的供货信息已成功发布，微信搜索[极农]或"
        hashMap.put("phone_number_list", "+86" + phoneNumber);
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        String post = "";
//        if(!SystemConst.isDev){
        post = HttpTool.doPost(url, stringStringHashMap, JSONObject.toJSONString(hashMap));
//        }
        return post;
    }

    /**
     * 微信内容检测
     *
     * @param textContent 文本内容
     * @param scene       场景枚举值（1 资料；2 评论；3 论坛；4 社交日志）
     * @return
     */
    public JSONObject msgSecCheck(String textContent, Integer scene, String openId, String appId, String appSecret) {
        String url = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token=" + getToken(appId, appSecret);
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("version", 2);
        hashMap.put("openid", openId);
        hashMap.put("scene", scene);
        hashMap.put("content", textContent);
        hashMap.put("nickname", textContent);
        hashMap.put("title", textContent);
        hashMap.put("signature", textContent);
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        String post = HttpTool.doPost(url, stringStringHashMap, JSONObject.toJSONString(hashMap));
        JSONObject jsonObject = JSONObject.parseObject(post);
        return jsonObject;
    }

    /**
     * 推送公众号模板消息
     *
     * @param template
     * @return
     */
    public static ApiResponse<JSONObject> pushGzhTemplate(NewMpTemplateMsg template, String appId, String appSecret) {
        if (template == null) {
            return new ApiResponse<>(URLCodeConst.PARAM_ERROR, "模板参数为空!");
        }
        String templateJson = JSON.toJSONString(template);
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + getToken(appId, appSecret);
        Map<String, String> header = new HashMap<>();
        String s = HttpTool.doPost(url, header, templateJson);
        JSONObject jsonObject = JSON.parseObject(s);
        return new ApiResponse<>(jsonObject);
    }
}
