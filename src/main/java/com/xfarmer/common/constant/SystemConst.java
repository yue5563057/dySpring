package com.xfarmer.common.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SystemConst {

    public static final String UTF8 = "UTF-8";

    public static final String RESET_FARMING_TIME = "farming:reset:time:";

    @Value("${spring.dyBase.systemConst.hostStr}")
    String hostStr;
    @Value("${spring.dyBase.systemConst.systemHostStr}")
    String systemHostStr;
    @Value("${spring.dyBase.systemConst.isDev}")
    private boolean isDevStr = false;
    @Value("${spring.dyBase.systemConst.farmingPushUrl}")
    private String farmingPushUrl;
    @Value("${spring.dyBase.systemConst.farmingPushUrlSaas}")
    private String farmingPushUrlSaas;
    @Value("${spring.dyBase.systemConst.saasH5BaseUrl}")
    private String saasH5BaseUrl;
    @Value("${spring.dyBase.systemConst.apiHost}")
    private String apiHost;
    @Value("${spring.dyBase.systemConst.qrcodeImg}")
    private String qrcodeImg;
    @Value("${spring.dyBase.systemConst.profilesActive}")
    private String profilesStr;
    @Value("${spring.dyBase.systemConst.auditType}")
    private Integer auditType;

    private static synchronized void setInit(Boolean isDevStr, String qrcodeImg, String profiles, Integer auditType) {
        isDev = isDevStr;
        QRCODE_IMG = qrcodeImg;
        PROFILES = profiles;
        AUDIT_TYPE = auditType;
    }

    private static synchronized void setInitHost(String systemHostStr, String hostStr, String farmingPushUrl, String farmingPushUrlSaas
            , String saasH5BaseUrl, String apiHost) {
        SYSTEM_HOST = systemHostStr;

        FARMING_PUSH_URL = farmingPushUrl;
        FARMING_PUSH_URL_SAAS = farmingPushUrlSaas;
        SAAS_H5_BASE_URL = saasH5BaseUrl;
        API_HOST = apiHost;
    }


    @Value("${spring.dyBase.systemConst.swaggerApiPath}")
    private String swaggerApiPathStr;


    @Value("${spring.dyBase.systemConst.swaggerApiTitle}")
    private String swaggerTitleStr;

    @Value("${swagger_api_description}")
    private String swaggerDescriptionStr;


    @PostConstruct
    public void init() {
        setInit(isDevStr, qrcodeImg, profilesStr, auditType);
        setInitHost(systemHostStr, hostStr, farmingPushUrl, farmingPushUrlSaas, saasH5BaseUrl, apiHost);
        setInitSwagger(swaggerApiPathStr, swaggerTitleStr, swaggerDescriptionStr);
    }

    private static synchronized void setInitSwagger(String swaggerApiPathStr, String swaggerTitleStr, String swaggerDescriptionStr) {
        swaggerApiPath = swaggerApiPathStr;
        swaggerTitle = swaggerTitleStr;
        swaggerDescription = swaggerDescriptionStr;
    }

    public static String swaggerApiPath;
    public static String swaggerTitle;
    public static String swaggerDescription;

    /**
     * 审批端
     */
    public static Integer AUDIT_TYPE;

    /**
     * 二维码图片
     */
    public static String QRCODE_IMG;

    /***
     * api路径
     */
    public static String API_HOST;

    /**
     * 是否是开发模式
     */
    public static boolean isDev;

    /**
     * 后台系统配置host
     */
    public static String SYSTEM_HOST;

    /**
     * 环境
     */
    public static String PROFILES;

    /**
     * 前端系统配置host
     */
    public static String HOST;

    /**
     * SaasH5的前端主路径
     */
    public static String SAAS_H5_BASE_URL;
    /**
     * 农户推送跳转的url
     */
    public static String FARMING_PUSH_URL;
    /**
     * 农户推送跳转的urlSAAS
     */
    public static String FARMING_PUSH_URL_SAAS;
    /**
     * 扫码登录秘钥常量
     */
    public static String TOKEN_SECRET = "XFARMER:SECRET";

    /**
     * 公众号授权
     */
    public static String TOKEN_GZH_SECRET = "XFARMER:Wx:GZH:SECRET";

    /**
     * 开发的兼职部门
     */
    public static final int DEV_PART_TIME_DEPARTMENT = 9;

    /**
     * 正式的兼职部门
     */
    public static final int PROD_PART_TIME_DEPARTMENT = 12;

    /**
     * 常量 值：ok
     */
    public static final String OK = "ok";

    /**
     * 常量 值：OK
     */
    public static final String UP_OK = "OK";

    /**
     * 常量 值：errmsg
     */
    public static final String ERR_MSG = "errmsg";

    /**
     * 常量 值：errcode
     */
    public static final String ERR_CODE = "errcode";

    /**
     * 通用值 已删除
     */
    public static final Integer IS_DEL_TRUE = 1;

    /**
     * 通用值 未删除
     */
    public static final Integer IS_DEL_FALSE = 0;


    /**
     * 时间格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String STANDARD_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
}
