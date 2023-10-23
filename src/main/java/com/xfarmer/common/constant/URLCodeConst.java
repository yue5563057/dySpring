package com.xfarmer.common.constant;



public class URLCodeConst {



    private URLCodeConst(){
    }

    /** 成功 返回 code*/
    public static final Integer SUCCESS = 0;

    /**
     * 失败 登录平台错误
     */
    public static final Integer PLATFORM_ERROR = 101;
    /**
     * 失败 用户未登录
     */
    public static final Integer USER_UN_LOGIN = 102;
    /**
     * 权限错误
     */
    public static final Integer PERMISSION_ERROR = 103;
    /**
     * 登录成功
     */
    public static final Integer LOGIN_SUCCESS = 104;
    /**
     * 登录失败
     */
    public static final Integer LOGIN_FAIL = 105;
    /**
     * 解析code错误
     */
    public static final Integer PARSE_CODE_FAIL = 106;
    /**
     * 微信用户信息获取失败
     */
    public static final Integer FAILED_USER_INFO = 107;
    /**
     * 发送验证码错误
     */
    public static final Integer MESSAGE_ERROR = 108;
    /**
     * 用户绑定失败
     */
    public static final Integer BIND_ERROR = 109;
    /**
     * 微信号未绑定手机号
     */
    public static final Integer WX_NOT_BIND_PHONE = 110;
    /**
     * 微信H5用户未选择操作合作社
     */
    public static final Integer WX_NOT_SEL_COOPERATION = 111;
    /**
     * 合作社小程序用户未手机号登录或session过期
     */
    public static final Integer COOPERATION_NOT_SESSION = 112;
    /**
     * 请完善用户信息
     */
    public static final Integer FINISH_USER_INFO = 113;
    /**
     * 用户未关注公众号
     */
    public static final Integer IS_NOT_FOCUS_WEIXIN = 114;
    /**
     * 未查询到合作社和农户角色
     */
    public static final Integer NOT_COOPERATION_AND_FARMER_ROLE = 115;

    /**
     * 微信推送模板消息失败
     */
    public static final Integer WEIXIN_TEMPLATE_ERROR = 116;
    /**
     * 未绑定微信小程序openId
     */
    public static final Integer NOT_BIND_APP_OPENID = 117;
    /**
     * 未在head里面加入clientId
     */
    public static final Integer CLIENT_NOT = 118;
    /**
     * clientId值错误
     */
    public static final Integer CLIENT_ID_ERROR = 119;
    /**
     * 账户错误
     */
    public static final Integer ACCOUNT_ERROR =120;
    /**
     * 银行卡信息错误
     */
    public static final Integer ACCOUNT_BANK_ERROR = 121;
    /**
     * 合作社信息未完善
     */
    public static final int COOPERATION_NOT_SUCCESS_INFO = 122;
    /**
     * 参数错误
     */
    public static final Integer PARAM_ERROR = 1;
    /**
     * 解析参数错误
     */
    public static final Integer PARSE_PARAM = 12;
    /**
     * 弹出模态框
     */
    public static final Integer ERROR_POP_MODEL = 13;
    /**
     * 对象不存在
     */
    public static final Integer NOT_EXIST = 2;
    /**
     * 保存失败
     */
    public static final Integer SAVE_FAIL = 3;
    /**
     * 修改失败
     */
    public static final Integer UPDATE_FAIL = 4;
    /**
     * 版本错误
     */
    public static final Integer VERSION_FAIL = 5;
    /**
     * 重复提交
     */
    public static final Integer REPEAT_SUBMIT = 6;
    /**
     * 服务器异常
     */
    public static final Integer SERVER_ERROR = 7;
    /**
     * 状态错误
     */
    public static final Integer STATUS_ERROR = 8;
    /**
     * 删除错误错误
     */
    public static final Integer DELETE_ERROR = 9;
    /**
     * 文件上传错误
     */
    public static final Integer UPLOAD_FAIL = 10;
    /**
     * T+供应商错误
     */
    public static final Integer TPLUS_PARTNER_ERROR = 1001;
    /**
     * T+错误
     */
    public static final Integer TPLUS_ERROR = 1000;
    public static final Integer APP_ERROR = 11;
    /**
     * 外部接口错误
     */
    public static final Integer INTERFACE_ERROR = 12;
    /**
     * 微信小程序未设置溯源批次
     */
    public static final Integer TRACE_NOT_PLANT = 201;
    /**
     * 服务器异常
     */
    public static final Integer SYSTEM_ERROR = -1;

    /**
     * 用户已经和合作社绑定
     */
    public static final Integer BIND_ON_COOPERATION = 301;

    /**
     * 微信支付配置异常
     */
    public static final Integer PAY_CONFIG_ERROR = 601;
}
