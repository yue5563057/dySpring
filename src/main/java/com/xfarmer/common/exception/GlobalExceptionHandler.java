package com.xfarmer.common.exception;

import com.alibaba.fastjson.JSONException;
import com.xfarmer.common.constant.URLCodeConst;
import com.xfarmer.common.util.ApiResponse;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.ibatis.exceptions.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

/**
 * @author 东岳
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @Autowired
    MeterRegistry registry;

    @ExceptionHandler({NotLoginException.class, MyException.class, AuthenticationException.class, Exception.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ApiResponse<String> globalException(HttpServletRequest request, Exception exception) {
        exception.printStackTrace();
        String requestUri = request.getRequestURI();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            exception.printStackTrace(pw);
        } finally {
            pw.close();
        }

        Counter.builder("exceptions")
                .tag("class", exception.getClass().getSimpleName())
                .tag("message", exception.getMessage())
                .register(registry)
                .increment();
        if (exception instanceof JSONException) {
            return new ApiResponse<>(URLCodeConst.PARSE_PARAM, "保存数据失败，请检查输入信息！");
        } else if (exception instanceof MyException) {
            return new ApiResponse<>(URLCodeConst.SERVER_ERROR, exception.getMessage());
        } else if (exception instanceof NotLoginException || exception instanceof AuthenticationException) {
            return new ApiResponse<>(URLCodeConst.USER_UN_LOGIN, exception.getMessage());
        } else if (exception instanceof MethodArgumentTypeMismatchException) {
            return new ApiResponse<>(URLCodeConst.PARSE_PARAM, "输入错误,请检查输入内容!");
        } else if (exception instanceof PersistenceException) {
            return new ApiResponse<>(URLCodeConst.PERMISSION_ERROR, "保存数据失败，请检查输入信息！");
        } else if (exception instanceof NullPointerException) {
            return new ApiResponse<>(URLCodeConst.SERVER_ERROR, "后端服务器错误！");
        } else if (exception instanceof SQLException) {
            return new ApiResponse<>(URLCodeConst.SERVER_ERROR, "数据库错误!");
        } else {
            return new ApiResponse<>(URLCodeConst.SYSTEM_ERROR, exception.getMessage());
        }
    }


}
