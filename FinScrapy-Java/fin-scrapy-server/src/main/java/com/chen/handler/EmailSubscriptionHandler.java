package com.chen.handler;

import com.alibaba.fastjson.JSON;
import com.chen.dto.EmailSubscriptionDTO;
import com.chen.dto.MessageParam;
import com.chen.dto.SendRequest;
import com.chen.feign.BittenServiceFeignClient;
import com.chen.utils.SourceUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
public class EmailSubscriptionHandler {

    @Autowired
    private BittenServiceFeignClient bittenServiceFeignClient;
    
    @Autowired
    private SourceUtils sourceUtils;

    private static final Long messageTemplateId = 4L;


    @KafkaListener(topics = "#{'${fin-scrapy.mq.email_subscription_topic_name}'}", groupId = "#{'${fin-scrapy.mq.subscription_group_id}'}")
    public void consumer(ConsumerRecord<?, String> consumerRecord) {
        Optional<String> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        if (kafkaMessage.isPresent()) {
            EmailSubscriptionDTO emailSubscriptionDTO = JSON.parseObject(kafkaMessage.get(), EmailSubscriptionDTO.class);
            StringBuilder emailBuilder = new StringBuilder();
            emailSubscriptionDTO.getEmails().forEach(email -> {emailBuilder.append(email).append(",");});
            emailBuilder.deleteCharAt(emailBuilder.length() - 1);
            Map<String, String> variables = getVariables(emailSubscriptionDTO);
            SendRequest sendRequest = SendRequest.builder()
                    .code("send")
                    .messageTemplateId(messageTemplateId)
                    .messageParam(MessageParam.builder()
                            .receivers(emailBuilder.toString())
                            .variables(variables).build())
                    .recallMessageIdList(null).build();
            bittenServiceFeignClient.send(sendRequest);
        }

    }

    private Map<String, String> getVariables(EmailSubscriptionDTO emailSubscriptionDTO) {
        Map<String, String> variables = new HashMap<>();
        String title = emailSubscriptionDTO.getRealTimeData().getTitle();
        String keyword = emailSubscriptionDTO.getKeyword();
        if (title != null && keyword != null && !keyword.isBlank() && title.contains(keyword)) {
            String boldKeyword = "<b>" + keyword + "</b>";
            title = title.replaceAll("(?i)" + Pattern.quote(keyword), boldKeyword);
        }
        variables.put("title", title);
        variables.put("keyword", keyword);
        variables.put("timestamp", emailSubscriptionDTO.getRealTimeData().getTimestamp());
        variables.put("url", emailSubscriptionDTO.getRealTimeData().getUrl());
        Map<String, String> sourcesMap = sourceUtils.getSourcesMap();
        variables.put("source", sourcesMap.get(emailSubscriptionDTO.getSource()));
        return variables;
    }
}
