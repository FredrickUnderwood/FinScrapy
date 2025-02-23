package com.chen.thread;

import com.alibaba.fastjson.JSON;
import com.chen.constant.ThreadConstant;
import com.chen.dto.EmailSubscriptionDTO;
import com.chen.dto.MessageParam;
import com.chen.dto.SendRequest;
import com.chen.mq.SendMqService;
import com.chen.redis.RealTimeDataRedisMapper;
import com.chen.redis.SubscriptionRedisMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class SubscriptionThread {

    private static final String LOG_PREFIX = "[SubscriptionThread]";

    private ExecutorService executorService;

    private volatile boolean stopNotifyThread = false;

    @Value("${fin-scrapy.subscription.tick_range}")
    private Long tickRange;

    @Value("${fin-scrapy.sources}")
    private String sources;

    @Value("${fin-scrapy.mq.email_subscription_topic_name}")
    private String emailSubscriptionTopic;

    @Autowired
    private SubscriptionRedisMapper subscriptionRedisMapper;

    @Autowired
    private RealTimeDataRedisMapper realTimeDataRedisMapper;

    @Autowired
    private SendMqService sendMqService;

    public void start() {
        List<String> sourceList = List.of(sources.split(","));
        executorService = Executors.newFixedThreadPool(sourceList.size());
        for (String source : sourceList) {
            executorService.submit(() -> processSpecifiedSource(source));
        }
    }

    private void processSpecifiedSource(String source) {
        try {
            TimeUnit.MILLISECONDS.sleep(ThreadConstant.ALIGN_MILLI - System.currentTimeMillis() % ThreadConstant.MILLI_PER_SECOND);
        } catch (InterruptedException e) {
            if (!stopNotifyThread) {
                log.error("{}Align thread failed for source: {}, error: {}", LOG_PREFIX, source, e.getMessage(), e);
            }
        }

        long lastCheckTime = System.currentTimeMillis() - tickRange;

        while (!stopNotifyThread) {
            try {
                long startTime = System.currentTimeMillis();
                Set<String> keywords = subscriptionRedisMapper.getActiveKeywords();
                realTimeDataRedisMapper.getRealTimeTitlesSpecifiedSource(source, startTime, lastCheckTime).forEach(realTimeData -> {
                    keywords.forEach(keyword -> {
                        if (realTimeData.getTitle().contains(keyword)) {

                            // TODO 发消息逻辑
                            Set<String> emails = subscriptionRedisMapper.getEmailsByKeyword(keyword);
                            if (Objects.nonNull(emails) && !emails.isEmpty()) {
                                EmailSubscriptionDTO emailSubscriptionDTO = EmailSubscriptionDTO.builder()
                                        .emails(emails)
                                        .realTimeData(realTimeData)
                                        .keyword(keyword)
                                        .source(source).build();
                                sendMqService.send(emailSubscriptionTopic, JSON.toJSONString(emailSubscriptionDTO));
                            }
                            Set<String> push = subscriptionRedisMapper.getPushByKeyword(keyword);
                            if (Objects.nonNull(push) && !push.isEmpty()) {

                            }
                        }
                    });
                });
                long timeCost = System.currentTimeMillis() - startTime;

                if (timeCost < tickRange) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(tickRange - timeCost);
                    } catch (InterruptedException e) {
                        if (!stopNotifyThread) {
                            log.error("{}Align thread failed for source: {}, error: {}", LOG_PREFIX, source, e.getMessage(), e);
                        }
                    }
                }
                lastCheckTime = startTime + 1;
            } catch (Exception e) {
                if (!stopNotifyThread) {
                    log.error("{}Thread run caught exception for source: {}, error: {}", LOG_PREFIX, source, e.getMessage(), e);
                }
            }

        }
    }

    public void stop() {
        stopNotifyThread = true;
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(ThreadConstant.THREAD_POOL_TIMEOUT, TimeUnit.SECONDS)) {
                log.warn("{}Timeout while shutting down subscription thread pool", LOG_PREFIX);
                executorService.shutdown();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
