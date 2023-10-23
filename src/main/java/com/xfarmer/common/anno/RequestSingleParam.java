package com.xfarmer.common.anno;

import org.springframework.web.bind.annotation.ValueConstants;

import java.lang.annotation.*;

/**
 * 自定义接收RequestBody中的单个参数
 * @author 东岳
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestSingleParam {

    String value();

    boolean required() default true;

    String defaultValue() default ValueConstants.DEFAULT_NONE;
}
