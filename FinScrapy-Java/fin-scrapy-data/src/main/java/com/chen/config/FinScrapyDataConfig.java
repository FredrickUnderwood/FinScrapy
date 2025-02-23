package com.chen.config;

import com.chen.thread.SubscriptionThread;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FinScrapyDataConfig implements InitializingBean, DisposableBean {

    @Autowired
    private SubscriptionThread subscriptionThread;

    @Override
    public void destroy() throws Exception {
        subscriptionThread.stop();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        subscriptionThread.start();
    }
}
