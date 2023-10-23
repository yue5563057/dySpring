package com.xfarmer.common.util.busi;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class BeanFactory implements ApplicationContextAware {
    private static ApplicationContext nowApplicationContext;

    @Override
    public synchronized  void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        nowApplicationContext = applicationContext;
    }

    public static synchronized ApplicationContext getApplicationContext() {
        return nowApplicationContext;
    }

}
