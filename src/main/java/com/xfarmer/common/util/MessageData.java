package com.xfarmer.common.util;

public class MessageData<T> {

    /**历史消息*/
    public static final int TYPE_OLD_MESSAGE = 3;
    /**列表*/
    public static int TYPE_SESSION_LIST = 1;
    /**普通消息*/
    public static int TYPE_COMMON_MESSAGE = 0;
    /**普通消息*/
    public static int TYPE_USER_ID = 2;


    /**
     * 返回的代码
     */
    private int type = 0;
    /**
     * 返回的数据集合
     */
    private T data;

    public MessageData(int type, T data) {
        this.type = type;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public int getType() {
        return type;
    }


}
