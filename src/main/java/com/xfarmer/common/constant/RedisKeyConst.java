package com.xfarmer.common.constant;

public class RedisKeyConst {
    /**一天*/
    public static final int DAY = 3600 * 24;//一天的

    /**
     * 短信发送的缓存
     */
    public static final String MESSAGE_SEND = "Message:Send";

    /**
     * 短信发送的缓存
     */
    public static final String MESSAGE_SEND_EMPLOYEE = "Message:Send:Employee:";

    /**
     * 存放员工验证码的+手机号码
     */
    public static final String MESSAGE_CODE_EMPLOYEE = "Message:Send:Code:Employee:";

    /**
     * 短信发送的缓存(修改手机号）
     */
    public static final String MESSAGE_SEND_UPDATE_PHONE_NUMBER = "Message:Send:Employee:";

    /**
     * 存放修改手机号的验证码+手机号码
     */
    public static final String MESSAGE_CODE_UPDATE_PHONE_NUMBER = "Message:Send:Code:Employee:";


    /**
     * 发送频率 1分钟
     */
    public static final int MESSAGE_SEND_TIME = 60;

    /**
     * 时间 5秒
     */
    public static final int TIMTE_FIVE_SECOND = 5;
    /**
     * 存放验  证码的+手机号码
     */
    public static final String MESSAGE_CODE = "Message:Send:Code:";
    /**
     * 验证码有效期 10分钟
     */
    public static final int MESSAGE_CODE_TIME = 600;
    /**
     * 验证码有效期 5分钟
     */
    public static final int SESSION_TIME = 300;

    /**
     * 用户推送id
     */
    public static final String USER_J_PULISH_ID = "User:JPulish:";

    /**
     * 微信用户信息Session存放的地方
     */
    public static final String WEIXINUSER_KEY = "weixin:user:Session:";
    /**
     * 微信用户信息Session存放的地方
     */
    public static final String WEIXINUSER_TOKEN = "weixin:user:Token:";


    public static final String INVENTORY_TYPE = "T+:InventoryTypeList";
    public static final String INVENTORY_LIST = "T+:InventoryList";


    public static final String SYSTEM_ADMIN_SESSION = "SYSTEM_ADMIN";
    public static final String SYSTEM_ADMIN_ID_SESSION = "SYSTEM_ADMIN_ID";

    public static final String SYSTEM_PROVINCES_CITY_AREA = "SYSTEM_PROVINCES_CITY_AREA";

    /**
     * 微信用户信息Session存放的地方
     */
    public static final String EMPLOYEE_TO_TOKEN = "weixin:Employee:TO:Token:";

    public static final String USERNAME_AND_PASSWORD = "userNameAndPassword:user:Session:";
    public static final String FILE_INFO_KEY = "fileInfo:id:";

    public static final String WEIXINU_USER_TO_TOKEN = "weixin:User:H5:TO:Token:";
    /**合作社小程序手机号sessionId*/
    public static final String COOPERATION_USER_PHONE_SESSION = "cooperation:user:phone:session";
    /**合作社用户id和登录的token*/
    public static final String COOPERATION_USER_ID_TO_TOKEN = "cooperation:user:id:to:token:";
    /**生成pdf的id*/
    public static final String PAY_ORDER_PDF_ID = "payOrderPDFId:";
    public static final String TPLUS_PUSH_LEDGER_ID = "tplus:push:ledger:id:";
    /**半天*/
    public static final long HALFDAY = 12 * 3600;
    /**15分钟*/
    public static final long FIFTEEN_MINUTES = 60*15;
    /**半小时*/
    public static final long HALF_HOUR = 60*30;
    /**45分钟*/
    public static final long FORTY_FIVE_MINUTES = 60*45;
    /**1小时*/
    public static final long ONE_HOUR = 60*60;
    /**2小时*/
    public static final long TWO_HOUR = 3600*2;
    /**4小时*/
    public static final long FOUR_HOUR = 3600*4;
    /**6小时*/
    public static final long SIX_HOUR = 3600*6;
    /**8小时*/
    public static final long EIGHT_HOUR = 3600*8;
    public static final String FARMER_SOURCE_TYPE ="FarmerSourceTypeAll";
    /**用户拨打电话key*/
    public static final String CALL_PHONE = "call:phone:residual:number:user:";

    /**合作社收购单用户未读*/
    public static String COOPERATION_LEDGER_USER_NOT_READ="cooperation:ledger:not:read:userId:";

    /**省市信息*/
    public static String PROVINCES_CITY ="provinceAndCity:";

    /**
     * 提现--短信发送的缓存
     */
    public static final String WITHDRAW_MESSAGE_SEND_USERINFO = "Withdraw:Message:Send:UserInfo";
    /**
     * 提现--短信发送的缓存
     */
    public static final String WITHDRAW_MESSAGE_CODE_USERINFO = "Withdraw:Message:Code:UserInfo:";
    /**token对应的用户*/
    public static final String USER_LOGIN_TOKEN="user:login:token:";
    /**用户id对应的所有token*/
    public static final String USER_LOGIN_TOKEN_USER = "user:login:token:user:";
}
