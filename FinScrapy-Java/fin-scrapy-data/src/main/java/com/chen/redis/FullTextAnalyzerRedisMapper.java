package com.chen.redis;

import com.chen.entity.FullTextData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class FullTextAnalyzerRedisMapper {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String FULL_TEXT_DATA_REDIS_KEY_PREFIX = "full_text:";

    public void storeFullTextDataToRedis(String keyword, LinkedHashMap<String, List<FullTextData>> timelineMap) {
        String key = FULL_TEXT_DATA_REDIS_KEY_PREFIX + keyword;
        for (Map.Entry<String, List<FullTextData>> entry : timelineMap.entrySet()) {
            redisTemplate.opsForZSet().add(key, entry.getValue().toString(), getScore(entry.getKey()));
        }
    }

    private double getScore(String date) {
        StringBuilder score = new StringBuilder();
        for (char c : date.toCharArray()) {
            if (c >= '0' && c <= '9') {
                score.append(c);
            }
        }
        return Double.parseDouble(score.toString());
    }
}
