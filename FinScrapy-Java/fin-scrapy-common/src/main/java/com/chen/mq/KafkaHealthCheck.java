package com.chen.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class KafkaHealthCheck {

    @Autowired
    private SendMqService sendMqService;

    @Value("${fin-scrapy.mq.health_check_topic_name}")
    private String healthCheckTopicName;

    @Scheduled(fixedRate = 60 * 10 * 1000)
    public void sendHealthCheck() {
        String message = "Health check at: " + LocalDateTime.now();
        sendMqService.send(healthCheckTopicName, message);
    }
}
