package com.xfarmer.common.util;

import com.xfarmer.common.constant.SystemReturnConst;
import com.xfarmer.common.constant.URLCodeConst;
import lombok.Data;


@Data
public class ApiResponse<T> {

    private static final long serialVersionUID = 8689997638893312109L;
    /**
     * 提示的信息
     */
    private String msg = SystemReturnConst.SUCCESS;

    /**
     * 返回的代码
     */
    private int code = 0;
    /**
     * 返回的数据集合
     */
    private T data;

    public ApiResponse(String msg, T data) {
        this.msg = msg;
        this.data = data;
    }

    public ApiResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ApiResponse(int code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public ApiResponse(String msg) {
        this.msg = msg;
    }

    public ApiResponse(T data) {
        this.data = data;
    }

    public ApiResponse() {
    }

    public boolean isState() {
        return this.getCode() == 0;
    }

    public static ApiResponse<String> success() {
        return new ApiResponse<>(URLCodeConst.SUCCESS, SystemReturnConst.SUCCESS);
    }


    public ApiResponse<T> save(Boolean isSave, T data) {
        if (isSave != null && isSave) {
            return new ApiResponse<>(SystemReturnConst.SAVE_SUCCESS, data);
        }
        return new ApiResponse<>(URLCodeConst.SAVE_FAIL, SystemReturnConst.SAVE_FAIL, data);
    }

    public ApiResponse<T> updata(Boolean isUpdata, T data) {
        if (isUpdata != null && isUpdata) {
            return new ApiResponse<>(SystemReturnConst.UPDATE_SUCCESS, data);
        }
        return new ApiResponse<>(URLCodeConst.UPDATE_FAIL, SystemReturnConst.UPDATE_FAIL, data);
    }

    public ApiResponse<T> delete(Boolean isDelete, T data) {
        if (isDelete != null && isDelete) {
            return new ApiResponse<>(SystemReturnConst.DEL_SUCCESS, data);
        }
        return new ApiResponse<>(URLCodeConst.DELETE_ERROR, SystemReturnConst.DEL_FAIL, data);
    }


    public ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }


}
