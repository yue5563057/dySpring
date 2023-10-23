package com.xfarmer.common.util.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xfarmer.common.constant.SystemConst;
import com.xfarmer.common.constant.SystemReturnConst;
import com.xfarmer.common.constant.URLCodeConst;
import com.xfarmer.common.exception.MyException;
import com.xfarmer.common.util.ApiResponse;
import com.xfarmer.common.util.HttpTool;
import com.xfarmer.common.util.redis.RedisUtil;
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
public class CompanyWxUtile {


    private static RedisUtil redisUtil;

    @Autowired
    RedisUtil redisUtil1;

    /**
     * 微信成功的返回值
     */
    private static String wxCallBackSuccess = "ok";

    /**
     * 微信返回的消息字段名称
     */
    private static String wxCallBackFiledMsg = "errmsg";
    /**
     * 微信返回的编码字段名称
     */
    private static String wxCallBackFiledCode = "errcode";

    /**
     * token
     */
    private static String accessToken = "access_token";

    /**
     * 正常返回 --  success
     */
    private static String success = SystemReturnConst.SUCCESS;


    /**
     * 获取部门信息错误
     */
    private static String findDeptError = "获取部门信息错误!";

    public CompanyWxUtile() {
    }


    private synchronized void setRedisUtil(RedisUtil redisUtil1) {
        redisUtil = redisUtil1;
    }

    @PostConstruct
    public void init() {
        setRedisUtil(redisUtil1);
    }

    /**
     * 获取企业微信小程序身份令牌
     *
     * @return 返回令牌
     */
    private synchronized static String getToken(String corpId, String agentId) {
        String accTokenKey = "QYWXXCX_ACCESS_TOKEN:CorpID:" + corpId + "AgentId:" + agentId;
        Object o = redisUtil.get(accTokenKey);
        if (o == null) {
            String tokenUrl = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";
            Map<String, String> map = new TreeMap<>();
            map.put("corpid", corpId);
            map.put("corpsecret", agentId);
            String s = HttpTool.doGet(tokenUrl, map);
            JSONObject object = JSON.parseObject(s);
            if (wxCallBackSuccess.equals(object.getString(wxCallBackFiledMsg)) && 0 == object.getInteger(wxCallBackFiledCode)) {
                String backAccessToken = object.getString(CompanyWxUtile.accessToken);
                redisUtil.set(accTokenKey, backAccessToken, 7100);
                return backAccessToken;
            } else {
                throw new MyException("获取token失败!");
            }
        }
        return (String) o;
    }


    /**
     * 解析小程序code
     *
     * @return code
     */
    public static String parsingCode(String code, String corpId, String agentId) {
        String parsingCodeUrl = "https://qyapi.weixin.qq.com/cgi-bin/miniprogram/jscode2session";
        Map<String, String> map = new TreeMap<>();
        map.put(accessToken, getToken(corpId, agentId));
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String s = HttpTool.doGet(parsingCodeUrl, map);
        return s;
    }

    /**
     * 解析后台code
     *
     * @param code
     * @return
     */
    public String parsingSystemCode(String code, String corpId, String agentId) {
        String parsingCodeUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo";
        Map<String, String> map = new TreeMap<>();
        map.put(accessToken, getToken(corpId, agentId));
        map.put("code", code);
        map.put("debug", "1");
        String s = HttpTool.doGet(parsingCodeUrl, map);
        return s;
    }


    /**
     * 根据id获取用户的详细信息
     */
    public ApiResponse<JSONObject> findUserInfo(String userId, String corpId, String agentId) {
        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/get";
        Map<String, String> param = new TreeMap<>();
        param.put(accessToken, getToken(corpId, agentId));
        param.put("userid", userId);
        String userInfoStr = HttpTool.doGet(url, param);
        JSONObject jsonObject = JSON.parseObject(userInfoStr);
        if (!wxCallBackSuccess.equals(jsonObject.getString(wxCallBackFiledMsg)) || 0 != jsonObject.getInteger(wxCallBackFiledCode)) {
            return new ApiResponse<>(URLCodeConst.PARAM_ERROR, "获取成员信息错误!");
        }
        return new ApiResponse<>(success, jsonObject);
    }

    /**
     * 获取部门（传参数为id查询，不传为所有查询）
     */
    public ApiResponse<JSONObject> findDepartment(String departmentId, String corpId, String agentId) {
        String url = "https://qyapi.weixin.qq.com/cgi-bin/department/list";
        Map<String, String> param = new TreeMap<>();
        param.put(accessToken, getToken(corpId, agentId));
        if (departmentId != null) {
            param.put("id", departmentId);
        }
        String userInfoStr = HttpTool.doGet(url, param);
        JSONObject jsonObject = JSON.parseObject(userInfoStr);
        if (!wxCallBackSuccess.equals(jsonObject.getString(wxCallBackFiledMsg)) || 0 != jsonObject.getInteger(wxCallBackFiledCode)) {
            return new ApiResponse<>(URLCodeConst.PARAM_ERROR, findDeptError);
        }
        return new ApiResponse<>(success, jsonObject);
    }

    /**
     * 获取部门成员详情
     *
     * @param departmentId
     * @return
     */
    public ApiResponse<JSONObject> findUserListByDepartmentId(Integer departmentId, String corpId, String agentId) {
        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/list";
        Map<String, String> param = new TreeMap<>();
        param.put(accessToken, getToken(corpId, agentId));
        param.put("department_id", departmentId + "");
        String userInfoStr = HttpTool.doGet(url, param);
        JSONObject jsonObject = JSON.parseObject(userInfoStr);
        if (!wxCallBackSuccess.equals(jsonObject.getString(wxCallBackFiledMsg)) || 0 != jsonObject.getInteger(wxCallBackFiledCode)) {
            return new ApiResponse<>(URLCodeConst.PARAM_ERROR, findDeptError);
        }
        return new ApiResponse<>(success, jsonObject);
    }

    /**
     * 获取oa审批模板
     *
     * @param templateId
     * @return
     */
    public ApiResponse<JSONObject> getTemplateDetail(String templateId, String corpId, String agentId) {
//        BsAcXEcHp5xojTNaw4BUWfWrHGtjJbYYQYyW8wYiB
        String url = "https://qyapi.weixin.qq.com/cgi-bin/oa/gettemplatedetail?access_token=" + getToken(corpId, agentId);
        Map<String, String> param = new HashMap<>(16);
        param.put("template_id", templateId);
        String userInfoStr = HttpTool.doPostJson(url, JSON.toJSONString(param));
        JSONObject jsonObject = JSON.parseObject(userInfoStr);
        if (!wxCallBackSuccess.equals(jsonObject.getString(wxCallBackFiledMsg)) || 0 != jsonObject.getInteger(wxCallBackFiledCode)) {
            return new ApiResponse<>(URLCodeConst.PARAM_ERROR, "获取模板信息错误!");
        }
        return new ApiResponse<>(success, jsonObject);

    }


    /**
     * 获取部门成员
     *
     * @param departmentId 部门id
     * @param fetchChild   是否递归获取子部门下面的成员：1-递归获取，0-只获取本部门
     * @return
     */
    public ApiResponse<JSONObject> simplelist(Integer departmentId, Integer fetchChild, String corpId, String agentId) {
        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/list";
        Map<String, String> param = new HashMap<>();
        param.put(accessToken, getToken(corpId, agentId));
        param.put("department_id", departmentId + "");
        param.put("fetch_child", fetchChild + "");
        String userInfoStr = HttpTool.doGet(url, param);
        JSONObject jsonObject = JSON.parseObject(userInfoStr);
        if (0 != jsonObject.getInteger(wxCallBackFiledCode)) {
            return new ApiResponse<>(URLCodeConst.PARAM_ERROR, findDeptError);
        }
        return new ApiResponse<>(success, jsonObject);
    }


    /**
     * 给指定用户推送文本消息通知
     *
     * @return
     */
    public ApiResponse<JSONObject> pushTextMsg(String userId, String textContent, String corpId, String agentId) {
        String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + getToken(corpId, agentId);
        Map<String, Object> pushMsg = new HashMap<>();
        pushMsg.put("touser", userId);
        pushMsg.put("msgtype", "text");
        pushMsg.put("agentid", agentId);
        Map<String, String> textMap = new HashMap<>();
        textMap.put("content", textContent);
        pushMsg.put("text", textMap);
        String param = JSON.toJSONString(pushMsg);
        String userInfoStr = HttpTool.doPostJson(url, param);
        JSONObject jsonObject = JSON.parseObject(userInfoStr);
        if (0 != jsonObject.getInteger(wxCallBackFiledCode)) {
            return new ApiResponse<>(URLCodeConst.PARAM_ERROR, jsonObject.toJSONString());
        }
        return new ApiResponse<>(jsonObject);
    }

    public ApiResponse<String> getTicket(String corpId, String agentId) {
        String key = agentId + ":" + "getTicket";
        if (redisUtil.get(key) != null) {
            return new ApiResponse<>(URLCodeConst.SUCCESS, "cuccess", (String) redisUtil.get(key));
        }
        String apiUrl = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket";
        Map<String, String> param = new HashMap<>();
        param.put(accessToken, getToken(corpId, agentId));
        String s = HttpTool.doGet(apiUrl, param);
        JSONObject jsonObject = JSON.parseObject(s);
        if (0 == jsonObject.getInteger(wxCallBackFiledCode) && SystemConst.OK.equals(jsonObject.getString(wxCallBackFiledMsg))) {
            String ticket = jsonObject.getString("ticket");
            redisUtil.set(key, ticket);
            return new ApiResponse<>(URLCodeConst.SUCCESS, "cuccess", ticket);
        } else {
            return new ApiResponse<>(jsonObject.getInteger(wxCallBackFiledCode), jsonObject.getString(wxCallBackFiledMsg));
        }
    }


}
