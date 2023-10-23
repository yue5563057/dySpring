package com.xfarmer.common.util.busi;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 根据实现类的类名获取spring加载的对象
 *
 * @author: paul
 * @create: 2021/06/17 13:04
 **/
@Component
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    public SpringUtil() { /** NULL */}


    private static synchronized void setApplication(ApplicationContext arg0){
        applicationContext = arg0;
    }

    @Override
    public void setApplicationContext(ApplicationContext arg0) {
        if (applicationContext == null) {
            setApplication(arg0);
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
}