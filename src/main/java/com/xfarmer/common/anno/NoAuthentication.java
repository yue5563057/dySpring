package com.xfarmer.common.anno;

import java.lang.annotation.*;

/**
 * @author 东岳
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoAuthentication {
}
